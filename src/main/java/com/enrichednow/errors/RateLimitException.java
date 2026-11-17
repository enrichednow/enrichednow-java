package com.enrichednow.errors;

import java.time.Duration;
import java.util.Map;

/**
 * A {@code 429} answer. Two codes share it: {@code too_many_runs} (the account is at its limit of runs going at the
 * same time; it clears when one of them ends) and {@code rate_limited} (too many attempts; {@link #retryAfter()} says
 * how long to wait). Either way no run was created.
 */
public class RateLimitException extends ApiException {
    private static final long serialVersionUID = 1L;

    private final Duration retryAfter;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     * @param retryAfter the wait the {@code Retry-After} header named, or {@code null}
     */
    public RateLimitException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId,
                              Duration retryAfter) {
        super(statusCode, code, apiMessage, details, requestId);
        this.retryAfter = retryAfter;
    }

    /** @return how long the {@code Retry-After} header said to wait, or {@code null} when there was none */
    public Duration retryAfter() {
        return retryAfter;
    }

    /** @return whether this is {@code too_many_runs}: wait for one of your runs to end, then try again */
    public boolean isTooManyRuns() {
        return "too_many_runs".equals(code());
    }

    /** @return the runs in progress when the request was refused, or {@code null} when the answer did not say */
    public Long activeRuns() {
        return detailNumber("active_runs");
    }

    /** @return the account's limit of runs at the same time, or {@code null} when the answer did not say */
    public Long maxConcurrentRuns() {
        return detailNumber("max_concurrent_runs");
    }
}
