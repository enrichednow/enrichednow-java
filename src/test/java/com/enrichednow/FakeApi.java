package com.enrichednow;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        exchange.getResponseHeaders().add("Content-Type", "application/json");
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
