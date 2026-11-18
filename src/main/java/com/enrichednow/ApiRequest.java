package com.enrichednow;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
        return new ApiRequest<>(method, path, query, body, true, minTimeout, decoder, key);
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
