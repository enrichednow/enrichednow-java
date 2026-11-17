package com.enrichednow;

import com.enrichednow.errors.ApiException;
import com.enrichednow.errors.ApiTimeoutException;
import com.enrichednow.errors.ConnectionException;
import com.enrichednow.errors.EnrichedNowException;
import com.enrichednow.errors.RateLimitException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/** Sends requests: authentication, timeouts, retries, and turning error answers into exceptions. */
final class Transport {
    /** However long a Retry-After header asks for, a retry waits no longer than this. */
    static final Duration MAX_RETRY_AFTER = Duration.ofSeconds(60);
    private static final TypeReference<Map<String, Object>> MAP = new TypeReference<Map<String, Object>>() { };

    final ObjectMapper mapper;
    private final HttpClient http;
    private final String baseUrl;
    private final String apiKey;
    private final Duration timeout;
    private final int maxRetries;
    private final Duration initialRetryDelay;
    private final Duration maxRetryDelay;
    private final String userAgent;

    Transport(HttpClient http, String baseUrl, String apiKey, Duration timeout, int maxRetries, Duration initialRetryDelay,
              Duration maxRetryDelay) {
        this.http = http;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.initialRetryDelay = initialRetryDelay;
        this.maxRetryDelay = maxRetryDelay;
        this.userAgent = "enrichednow-java/" + Version.VALUE;
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // --- blocking -----------------------------------------------------------------------------------------------------

    <T> T execute(ApiRequest<T> request) {
        HttpRequest httpRequest = build(request);
        for (int attempt = 0; ; attempt++) {
            EnrichedNowException failure;
            try {
                return handle(request, http.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ConnectionException("The request to " + request.path + " was interrupted.", e);
            } catch (IOException e) {
                failure = translate(request, e);
            } catch (EnrichedNowException e) {
                failure = e;
            }
            Duration delay = retryDelay(request, attempt, failure);
            if (delay == null) {
                throw failure;
            }
            sleep(delay);
        }
    }

    static void sleep(Duration delay) {
        try {
            Thread.sleep(Math.max(0, delay.toMillis()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EnrichedNowException("Interrupted while waiting.", e);
        }
    }

    // --- asynchronous -------------------------------------------------------------------------------------------------

    <T> CompletableFuture<T> executeAsync(ApiRequest<T> request) {
        CompletableFuture<T> result = new CompletableFuture<>();
        try {
            attemptAsync(request, build(request), 0, result);
        } catch (RuntimeException e) {
            result.completeExceptionally(e);
        }
        return result;
    }

    private <T> void attemptAsync(ApiRequest<T> request, HttpRequest httpRequest, int attempt, CompletableFuture<T> result) {
        if (result.isDone()) {                                   // cancelled by the caller between attempts
            return;
        }
        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray()).whenComplete((response, error) -> {
            EnrichedNowException failure;
            try {
                if (error == null) {
                    result.complete(handle(request, response));
                    return;
                }
                Throwable cause = error instanceof CompletionException && error.getCause() != null ? error.getCause() : error;
                if (!(cause instanceof IOException)) {
                    result.completeExceptionally(cause);
                    return;
                }
                failure = translate(request, (IOException) cause);
            } catch (EnrichedNowException e) {
                failure = e;
            } catch (Throwable e) {
                result.completeExceptionally(e);
                return;
            }
            Duration delay = retryDelay(request, attempt, failure);
            if (delay == null) {
                result.completeExceptionally(failure);
                return;
            }
            CompletableFuture.delayedExecutor(Math.max(0, delay.toMillis()), TimeUnit.MILLISECONDS)
                    .execute(() -> attemptAsync(request, httpRequest, attempt + 1, result));
        });
    }

    // --- shared -------------------------------------------------------------------------------------------------------

    private HttpRequest build(ApiRequest<?> request) {
        Duration allowed = request.minTimeout != null && request.minTimeout.compareTo(timeout) > 0 ? request.minTimeout : timeout;
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + request.path + request.query))
                .timeout(allowed)
                .header("Authorization", "Bearer " + apiKey)
                .header("User-Agent", userAgent)
                .header("Accept", "application/json");
        if (request.body != null) {
            byte[] body;
            try {
                body = mapper.writeValueAsBytes(request.body);
            } catch (IOException e) {
                throw new IllegalArgumentException("The request body cannot be written as JSON: " + e.getMessage(), e);
            }
            builder.header("Content-Type", "application/json").method(request.method, HttpRequest.BodyPublishers.ofByteArray(body));
        } else {
            builder.method(request.method, HttpRequest.BodyPublishers.noBody());
        }
        return builder.build();
    }

    private <T> T handle(ApiRequest<T> request, HttpResponse<byte[]> response) {
        int status = response.statusCode();
        if (status >= 200 && status < 300) {
            try {
                return request.decoder.decode(mapper, response.body());
            } catch (IOException | IllegalArgumentException e) {
                throw new EnrichedNowException("The answer of " + request.method + " " + request.path + " could not be read: "
                        + e.getMessage(), e);
            }
        }
        throw error(response);
    }

    private ApiException error(HttpResponse<byte[]> response) {
        String code = null;
        String message = null;
        Map<String, Object> details = null;
        try {
            JsonNode error = mapper.readTree(response.body()).path("error");
            if (error.isObject()) {
                code = error.path("code").isTextual() ? error.path("code").asText() : null;
                message = error.path("message").isTextual() ? error.path("message").asText() : null;
                if (error.path("details").isObject()) {
                    details = mapper.convertValue(error.path("details"), MAP);
                }
            }
        } catch (IOException | IllegalArgumentException notJson) {
            // An answer that is not the API's own (a gateway in between, say): the status is all there is.
        }
        String requestId = response.headers().firstValue("x-request-id")
                .orElseGet(() -> response.headers().firstValue("request-id").orElse(null));
        Duration retryAfter = response.headers().firstValue("retry-after").map(Transport::parseRetryAfter).orElse(null);
        return ApiException.of(response.statusCode(), code, message, details, requestId, retryAfter);
    }

    /** A Retry-After header: a number of seconds, or an HTTP date. Null when it is neither. */
    static Duration parseRetryAfter(String value) {
        String text = value.trim();
        try {
            double seconds = Double.parseDouble(text);
            return seconds >= 0 ? Duration.ofMillis((long) (seconds * 1000)) : null;
        } catch (NumberFormatException notSeconds) {
            try {
                Duration until = Duration.between(ZonedDateTime.now(), ZonedDateTime.parse(text, DateTimeFormatter.RFC_1123_DATE_TIME));
                return until.isNegative() ? Duration.ZERO : until;
            } catch (DateTimeParseException notDate) {
                return null;
            }
        }
    }

    private static ConnectionException translate(ApiRequest<?> request, IOException e) {
        String what = request.method + " " + request.path;
        String hint = request.idempotent ? "" : " The request may have been carried out all the same: check before sending it again.";
        if (e instanceof HttpConnectTimeoutException) {
            return new ApiTimeoutException("Connecting for " + what + " timed out.", e);
        }
        if (e instanceof HttpTimeoutException) {
            return new ApiTimeoutException(what + " timed out." + hint, e);
        }
        if (e instanceof ConnectException) {
            return new ConnectionException("Could not connect for " + what + ": " + e.getMessage(), e);
        }
        return new ConnectionException(what + " failed: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()) + "." + hint, e);
    }

    /**
     * How long to wait before trying again, or null when this failure is not retried.
     *
     * <p>Retried: a 429 (the API refused before doing anything) and a connection that was never made, for every
     * request; a 5xx and a failure in flight only for a request that is safe to repeat. Starting a run is not: the API
     * has no idempotency key, so a second POST could start, and charge for, a second run.
     */
    Duration retryDelay(ApiRequest<?> request, int attempt, EnrichedNowException failure) {
        if (attempt >= maxRetries) {
            return null;
        }
        if (failure instanceof ApiException) {
            int status = ((ApiException) failure).statusCode();
            if (status == 429) {
                Duration asked = ((RateLimitException) failure).retryAfter();
                if (asked != null) {
                    return asked.compareTo(MAX_RETRY_AFTER) > 0 ? MAX_RETRY_AFTER : asked;
                }
                return backoff(attempt);
            }
            return status >= 500 && request.idempotent ? backoff(attempt) : null;
        }
        if (failure instanceof ConnectionException) {
            Throwable cause = failure.getCause();
            boolean neverSent = cause instanceof ConnectException || cause instanceof HttpConnectTimeoutException;
            return neverSent || request.idempotent ? backoff(attempt) : null;
        }
        return null;
    }

    /** Exponential backoff with jitter: between half of and the whole of initial × 2^attempt, capped. */
    private Duration backoff(int attempt) {
        double ceiling = Math.min((double) maxRetryDelay.toMillis(), initialRetryDelay.toMillis() * Math.pow(2, Math.min(attempt, 30)));
        return Duration.ofMillis((long) (ceiling * (0.5 + ThreadLocalRandom.current().nextDouble() * 0.5)));
    }
}
