package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/** One event sent to one URL, with every attempt made at it. */
public final class WebhookDelivery {
    private final String id;
    private final String endpointId;
    private final String runId;
    private final String eventId;
    private final String event;
    private final String url;
    private final String status;
    private final int attempts;
    private final int maxAttempts;
    private final Instant nextAttemptAt;
    private final Integer responseStatus;
    private final String error;
    private final Integer durationMs;
    private final Instant lastAttemptAt;
    private final Instant createdAt;
    private final Instant finishedAt;
    private final String resendOf;

    @JsonCreator
    WebhookDelivery(@JsonProperty("id") String id,
                    @JsonProperty("endpoint_id") String endpointId,
                    @JsonProperty("run_id") String runId,
                    @JsonProperty("event_id") String eventId,
                    @JsonProperty("event") String event,
                    @JsonProperty("url") String url,
                    @JsonProperty("status") String status,
                    @JsonProperty("attempts") int attempts,
                    @JsonProperty("max_attempts") int maxAttempts,
                    @JsonProperty("next_attempt_at") String nextAttemptAt,
                    @JsonProperty("response_status") Integer responseStatus,
                    @JsonProperty("error") String error,
                    @JsonProperty("duration_ms") Integer durationMs,
                    @JsonProperty("last_attempt_at") String lastAttemptAt,
                    @JsonProperty("created_at") String createdAt,
                    @JsonProperty("finished_at") String finishedAt,
                    @JsonProperty("resend_of") String resendOf) {
        this.id = id;
        this.endpointId = endpointId;
        this.runId = runId;
        this.eventId = eventId;
        this.event = event;
        this.url = url;
        this.status = status;
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.nextAttemptAt = Values.instant(nextAttemptAt);
        this.responseStatus = responseStatus;
        this.error = error;
        this.durationMs = durationMs;
        this.lastAttemptAt = Values.instant(lastAttemptAt);
        this.createdAt = Values.instant(createdAt);
        this.finishedAt = Values.instant(finishedAt);
        this.resendOf = resendOf;
    }

    /** @return the delivery's id, {@code whd_…} */
    public String id() {
        return id;
    }

    /** @return the endpoint it went to; null for a run's own {@code webhook_url} */
    public String endpointId() {
        return endpointId;
    }

    /** @return the run that ended */
    public String runId() {
        return runId;
    }

    /** @return the event's id: the same on every attempt and when sent again, so use it to ignore a repeat */
    public String eventId() {
        return eventId;
    }

    /** @return the event, e.g. {@code run.succeeded} */
    public String event() {
        return event;
    }

    /** @return where it was sent */
    public String url() {
        return url;
    }

    /** @return {@code pending}, {@code succeeded} or {@code failed} */
    public String status() {
        return status;
    }

    /** @return attempts made */
    public int attempts() {
        return attempts;
    }

    /** @return attempts it may have in all */
    public int maxAttempts() {
        return maxAttempts;
    }

    /** @return when the next attempt is due, or null */
    public Instant nextAttemptAt() {
        return nextAttemptAt;
    }

    /** @return the HTTP status of the last answer, or null */
    public Integer responseStatus() {
        return responseStatus;
    }

    /** @return what went wrong with the last attempt, or null */
    public String error() {
        return error;
    }

    /** @return how long the last attempt took, in milliseconds, or null */
    public Integer durationMs() {
        return durationMs;
    }

    /** @return when the last attempt was made, or null */
    public Instant lastAttemptAt() {
        return lastAttemptAt;
    }

    /** @return when the delivery was made */
    public Instant createdAt() {
        return createdAt;
    }

    /** @return when it succeeded or gave up, or null */
    public Instant finishedAt() {
        return finishedAt;
    }

    /** @return the delivery this one was sent again from, or null */
    public String resendOf() {
        return resendOf;
    }

    @Override
    public String toString() {
        return "WebhookDelivery{id=" + id + ", event=" + event + ", runId=" + runId + ", status=" + status + ", attempts=" + attempts + '}';
    }
}
