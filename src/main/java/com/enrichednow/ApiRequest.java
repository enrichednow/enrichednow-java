package com.enrichednow;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** One call to the API, described once and sent either blocking or asynchronously. */
final class ApiRequest<T> {
    /** Turns the body of a successful answer into the value the caller asked for. */
    @FunctionalInterface
    interface Decoder<T> {
        T decode(ObjectMapper mapper, byte[] body) throws IOException;
    }

    /** Turns a successful answer that is not JSON into the value the caller asked for: it reads the headers too. */
    @FunctionalInterface
    interface Reader<T> {
        T read(byte[] body, HttpHeaders headers) throws IOException;
    }

    /** The {@code Accept} header; null: JSON. */
    String accept;
    /** When set, reads the answer instead of {@link #decoder}. */
    Reader<T> reader;
    /** When set, the body sent as it is instead of {@link #body} as JSON, with {@link #contentType}. */
    byte[] rawBody;
    /** The {@code Content-Type} of {@link #rawBody}. */
    String contentType;

    static <T> ApiRequest<T> raw(String path, Query query, String accept, Reader<T> reader) {
        ApiRequest<T> request = new ApiRequest<>("GET", path, query, null, true, null, (mapper, body) -> null);
        request.accept = accept;
        request.reader = reader;
        return request;
    }

    static <T> ApiRequest<T> of(String method, String path, Object body, boolean idempotent, Decoder<T> decoder) {
        return new ApiRequest<>(method, path, (Query) null, body, idempotent, null, decoder);
    }

    /** Any method, with a query and a JSON body (or none). */
    static <T> ApiRequest<T> send(String method, String path, Query query, Object body, boolean idempotent, Duration minTimeout,
                                  Class<T> type) {
        return new ApiRequest<>(method, path, query, body, idempotent, minTimeout, (mapper, bytes) -> mapper.readValue(bytes, type));
    }

    /** Any method, with a query and a JSON body (or none), its answer read by {@code decoder}. */
    static <T> ApiRequest<T> send(String method, String path, Query query, Object body, boolean idempotent, Decoder<T> decoder) {
        return new ApiRequest<>(method, path, query, body, idempotent, null, decoder);
    }

    /** The same request, sending {@code body} as it is with this {@code Content-Type} instead of a JSON body. */
    ApiRequest<T> withRawBody(byte[] body, String type) {
        this.rawBody = body;
        this.contentType = type;
        return this;
    }

    static <T> ApiRequest<T> get(String path, Query query, Duration minTimeout, Class<T> type) {
        return new ApiRequest<>("GET", path, query, null, true, minTimeout, (mapper, body) -> mapper.readValue(body, type));
    }

    final String method;
    final String path;
    final String query;
    final Object body;
    /** Whether sending it twice does what sending it once does. Only such requests are retried after a failure in flight. */
    final boolean idempotent;
    /** The least time the request must be allowed, when it is longer than the client's timeout; or null. */
    final Duration minTimeout;
    final Decoder<T> decoder;
    /** Sent as the {@code Idempotency-Key} header, or null: what makes starting a run safe to send again. */
    final String idempotencyKey;

    /** The same request, carrying this idempotency key: with it, the request is safe to repeat. */
    ApiRequest<T> withIdempotencyKey(String key) {
        ApiRequest<T> keyed = new ApiRequest<>(method, path, query, body, true, minTimeout, decoder, key);
        keyed.accept = accept;
        keyed.reader = reader;
        keyed.rawBody = rawBody;
        keyed.contentType = contentType;
        return keyed;
    }

    private ApiRequest(String method, String path, String query, Object body, boolean idempotent, Duration minTimeout,
                       Decoder<T> decoder, String idempotencyKey) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.body = body;
        this.idempotent = idempotent;
        this.minTimeout = minTimeout;
        this.decoder = decoder;
        this.idempotencyKey = idempotencyKey;
    }

    private ApiRequest(String method, String path, Query query, Object body, boolean idempotent, Duration minTimeout,
                       Decoder<T> decoder) {
        this.method = method;
        this.path = path;
        this.query = query == null ? "" : query.toString();
        this.body = body;
        this.idempotent = idempotent;
        this.minTimeout = minTimeout;
        this.decoder = decoder;
        this.idempotencyKey = null;
    }

    static <T> ApiRequest<T> get(String path, Query query, Decoder<T> decoder) {
        return new ApiRequest<>("GET", path, query, null, true, null, decoder);
    }

    static <T> ApiRequest<T> get(String path, Query query, Class<T> type) {
        return get(path, query, (mapper, body) -> mapper.readValue(body, type));
    }

    static <T> ApiRequest<T> post(String path, Query query, Object body, boolean idempotent, Duration minTimeout, Class<T> type) {
        return new ApiRequest<>("POST", path, query, body, idempotent, minTimeout, (mapper, bytes) -> mapper.readValue(bytes, type));
    }

    /** One segment of a path, percent-encoded. */
    static String segment(String value, String what) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(what + " must not be empty");
        }
        return encode(value);
    }

    static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    /** A query string in the making; a null value is left out. */
    static final class Query {
        private final StringBuilder text = new StringBuilder();

        Query add(String name, Object value) {
            if (value != null) {
                text.append(text.length() == 0 ? '?' : '&').append(encode(name)).append('=').append(encode(String.valueOf(value)));
            }
            return this;
        }

        @Override
        public String toString() {
            return text.toString();
        }
    }
}
