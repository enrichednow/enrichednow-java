package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** A local stand-in for the API: answers with what a test queued, in order, and records what it was sent. */
final class FakeApi implements AutoCloseable {
    static final String KEY = "en_live_test_0123456789abcdef0123456789abcdef";

    /** One request the server received. */
    static final class Received {
        final String method;
        final String path;
        final String query;
        final Map<String, String> headers;
        final String body;

        Received(String method, String path, String query, Map<String, String> headers, String body) {
            this.method = method;
            this.path = path;
            this.query = query;
            this.headers = headers;
            this.body = body;
        }

        String header(String name) {
            return headers.get(name.toLowerCase());
        }

        @Override
        public String toString() {
            return method + " " + path + (query == null ? "" : "?" + query);
        }
    }

    /** One answer to give. */
    static final class Reply {
        final int status;
        final String body;
        final Map<String, String> headers = new LinkedHashMap<>();
        long delayMillis;

        Reply(int status, String body) {
            this.status = status;
            this.body = body;
        }

        Reply header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        Reply delay(long millis) {
            this.delayMillis = millis;
            return this;
        }
    }

    private final HttpServer server;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Deque<Reply> replies = new ArrayDeque<>();
    private final List<Received> received = new ArrayList<>();

    FakeApi() {
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        server.createContext("/", this::handle);
        server.setExecutor(executor);
        server.start();
    }

    private void handle(HttpExchange exchange) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();
        exchange.getRequestHeaders().forEach((name, values) -> headers.put(name.toLowerCase(), values.get(0)));
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Reply reply;
        synchronized (this) {
            received.add(new Received(exchange.getRequestMethod(), exchange.getRequestURI().getRawPath(),
                    exchange.getRequestURI().getRawQuery(), headers, body));
            reply = replies.isEmpty() ? new Reply(599, "{\"error\":{\"code\":\"test\",\"message\":\"no reply queued\",\"details\":{}}}")
                    : replies.poll();
        }
        if (reply.delayMillis > 0) {
            try {
                Thread.sleep(reply.delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        byte[] bytes = reply.body.getBytes(StandardCharsets.UTF_8);
        if (reply.headers.keySet().stream().noneMatch(name -> name.equalsIgnoreCase("Content-Type"))) {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
        }
        reply.headers.forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
        try {
            exchange.sendResponseHeaders(reply.status, bytes.length == 0 ? -1 : bytes.length);
            if (bytes.length > 0) {
                try (OutputStream out = exchange.getResponseBody()) {
                    out.write(bytes);
                }
            }
        } catch (IOException gone) {
            // the client gave up (a timeout test)
        } finally {
            exchange.close();
        }
    }

    synchronized FakeApi reply(int status, String body) {
        replies.add(new Reply(status, body));
        return this;
    }

    synchronized FakeApi reply(Reply reply) {
        replies.add(reply);
        return this;
    }

    FakeApi ok(String body) {
        return reply(200, body);
    }

    FakeApi error(int status, String code, String message) {
        return reply(status, errorBody(code, message, "{}"));
    }

    static String errorBody(String code, String message, String details) {
        return "{\"error\":{\"code\":\"" + code + "\",\"message\":\"" + message + "\",\"details\":" + details + "}}";
    }

    synchronized List<Received> received() {
        return new ArrayList<>(received);
    }

    synchronized int count() {
        return received.size();
    }

    synchronized Received last() {
        return received.get(received.size() - 1);
    }

    Received only() {
        List<Received> all = received();
        if (all.size() != 1) {
            throw new AssertionError("expected one request, got " + all);
        }
        return all.get(0);
    }

    String url() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    /** A client of this server with retries that do not slow a test down. */
    EnrichedNowClient.Builder builder() {
        return EnrichedNowClient.builder().apiKey(KEY).baseUrl(url())
                .initialRetryDelay(Duration.ofMillis(1)).maxRetryDelay(Duration.ofMillis(5));
    }

    EnrichedNowClient client() {
        return builder().build();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }

    // --- recorded exchanges ------------------------------------------------------------------------------------------

    /** Queues the answer of a recorded exchange of {@code fixtures/formulas}. */
    FakeApi answer(String fixture) {
        return reply(fixture(fixture).reply());
    }

    static Fixture fixture(String name) {
        return new Fixture(name);
    }

    /**
     * One exchange recorded from the real routes, under {@code src/test/resources/fixtures/formulas}: the request the
     * SDK must send, and the API's answer.
     */
    static final class Fixture {
        private static final ObjectMapper MAPPER = new ObjectMapper();

        final String name;
        final JsonNode request;
        final JsonNode response;

        private Fixture(String name) {
            this.name = name;
            try (InputStream in = FakeApi.class.getResourceAsStream("/fixtures/formulas/" + name + ".json")) {
                if (in == null) {
                    throw new IllegalArgumentException("no fixture " + name);
                }
                JsonNode root = MAPPER.readTree(in);
                this.request = root.path("request");
                this.response = root.path("response");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        /** @return the answer, as the API gave it: its status, headers and body */
        Reply reply() {
            String body = "";
            if (response.has("body")) {
                try {
                    body = MAPPER.writeValueAsString(response.get("body"));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            } else if (response.has("text")) {
                body = response.get("text").asText();
            }
            Reply reply = new Reply(response.path("status").asInt(), body);
            response.path("headers").fields().forEachRemaining(header -> reply.header(header.getKey(), header.getValue().asText()));
            return reply;
        }

        /** @return the answer's JSON body */
        JsonNode body() {
            return response.path("body");
        }

        /** @return the answer's text body (CSV or NDJSON) */
        String text() {
            return response.path("text").asText();
        }

        /** @return the request's query, decoded */
        Map<String, String> query() {
            Map<String, String> query = new TreeMap<>();
            request.path("query").fields().forEachRemaining(field -> query.put(field.getKey(), field.getValue().asText()));
            return query;
        }

        /** @return the answer with its JSON body changed by {@code change} */
        Reply replyWith(java.util.function.Consumer<ObjectNode> change) {
            ObjectNode copy = response.path("body").deepCopy();
            change.accept(copy);
            Reply original = reply();
            Reply reply;
            try {
                reply = new Reply(original.status, MAPPER.writeValueAsString(copy));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            reply.headers.putAll(original.headers);
            return reply;
        }

        /** Asserts the SDK sent the recorded request: its method, path, query, body and the headers it names. */
        void assertSent(Received received) {
            assertSent(received, query());
        }

        /** Asserts the SDK sent the recorded request, with this query instead of the recorded one. */
        void assertSent(Received received, Map<String, String> expectedQuery) {
            assertEquals(request.path("method").asText(), received.method, name + ": method");
            assertEquals(request.path("path").asText(), received.path, name + ": path");
            assertEquals(new TreeMap<>(expectedQuery), queryOf(received), name + ": query");
            if (request.has("body")) {
                try {
                    assertEquals(request.get("body"), MAPPER.readTree(received.body), name + ": body");
                } catch (IOException e) {
                    throw new AssertionError(name + ": the body sent is not JSON: " + received.body, e);
                }
                assertEquals("application/json", received.header("Content-Type"), name + ": content type");
            } else if (request.has("text")) {
                assertEquals(request.get("text").asText(), received.body, name + ": body");
            } else {
                assertEquals("", received.body, name + ": no body");
            }
            request.path("headers").fields().forEachRemaining(header ->
                    assertEquals(header.getValue().asText(), received.header(header.getKey()), name + ": header " + header.getKey()));
        }

        static Map<String, String> queryOf(Received received) {
            Map<String, String> query = new TreeMap<>();
            if (received.query != null && !received.query.isEmpty()) {
                for (String pair : received.query.split("&")) {
                    int equals = pair.indexOf('=');
                    query.put(URLDecoder.decode(pair.substring(0, equals), StandardCharsets.UTF_8),
                            URLDecoder.decode(pair.substring(equals + 1), StandardCharsets.UTF_8));
                }
            }
            return query;
        }
    }

    // --- bodies -------------------------------------------------------------------------------------------------------

    static String run(String id, String status, String reason) {
        boolean ended = !status.equals("queued") && !status.equals("running");
        return "{\"id\":\"" + id + "\",\"scraper_id\":\"ats.board\",\"scraper_version\":3,\"status\":\"" + status + "\","
                + "\"reason\":" + (reason == null ? "null" : "\"" + reason + "\"") + ","
                + "\"input\":{\"board_url\":\"https://boards.greenhouse.io/acme\"},"
                + "\"record_count\":" + (status.equals("ok") ? 3 : 0) + ",\"units\":" + (status.equals("ok") ? 1 : 0) + ","
                + "\"diagnostics\":" + (ended ? "{\"requests\":4,\"bytes\":null,\"wall_ms\":900,\"facts\":{\"ats\":\"greenhouse\",\"pages_read\":2}}" : "null") + ","
                + "\"created_at\":\"2026-11-17T09:30:00Z\",\"started_at\":\"2026-11-17T09:30:00.123456Z\","
                + "\"finished_at\":" + (ended ? "\"2026-11-17T09:30:01+00:00\"" : "null") + ","
                + "\"a_field_added_later\":{\"x\":[1,2]}}";
    }

    static String record(String title) {
        return "{\"title\":\"" + title + "\",\"company_name\":\"Acme\",\"company_domain\":null,"
                + "\"location\":{\"raw\":\"Austin, TX\",\"remote\":false},\"external_id\":\"4567890\"}";
    }

    static String posting(String id) {
        return "{\"id\":\"" + id + "\",\"title\":\"Senior Software Engineer (m/f/d)\",\"title_clean\":\"Senior Software Engineer\","
                + "\"company\":{\"domain\":\"acme.com\",\"name\":\"Acme\"},"
                + "\"location\":{\"raw\":\"Berlin\",\"city\":\"Berlin\",\"region\":null,\"country\":\"Germany\",\"country_code\":\"DE\",\"remote\":null},"
                + "\"employment_type\":\"full_time\",\"seniority\":\"senior\",\"function\":\"engineering\","
                + "\"salary\":{\"raw\":\"80-100k EUR\",\"min\":80000,\"max\":100000.5,\"currency\":\"EUR\",\"period\":\"year\"},"
                + "\"url\":\"https://boards.greenhouse.io/acme/jobs/1\",\"apply_url\":null,"
                + "\"source\":{\"name\":\"greenhouse\",\"provenance\":\"feed\",\"trusted\":true,\"external_id\":\"1\"},"
                + "\"status\":\"open\",\"posted_at\":\"2026-11-03\",\"valid_through\":null,"
                + "\"first_seen_at\":\"2026-11-04T08:00:00Z\",\"last_seen_at\":\"2026-11-17T08:00:00Z\",\"closed_at\":null,"
                + "\"new_field\":1}";
    }

    static String company(String domain) {
        return "{\"domain\":\"" + domain + "\",\"name\":\"Acme\",\"names\":[{\"name\":\"Acme\",\"first_seen_at\":\"2026-10-01T00:00:00Z\","
                + "\"last_seen_at\":\"2026-11-17T00:00:00Z\"}],\"careers_url\":\"https://acme.com/careers\",\"ats\":\"greenhouse\","
                + "\"first_seen_at\":\"2026-10-01T00:00:00Z\",\"last_seen_at\":\"2026-11-17T00:00:00Z\","
                + "\"last_read\":{\"at\":\"2026-11-17T00:00:00Z\",\"status\":\"complete\",\"last_complete_at\":\"2026-11-17T00:00:00Z\"},"
                + "\"postings\":{\"open\":12,\"closed\":30}}";
    }
}
