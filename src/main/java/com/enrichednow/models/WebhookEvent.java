package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/** What a webhook request carries: {@code Webhooks.parse} returns it from a request that verified. */
public final class WebhookEvent {
    private final String id;
    private final String type;
    private final Instant createdAt;
    private final Run run;

    @JsonCreator
    WebhookEvent(@JsonProperty("id") String id,
                 @JsonProperty("type") String type,
                 @JsonProperty("created_at") String createdAt,
                 @JsonProperty("data") Data data) {
        this.id = id;
        this.type = type;
        this.createdAt = Values.instant(createdAt);
        this.run = data == null ? null : data.run;
    }

    /** @return the event's id, {@code evt_…}: the same on every attempt and when sent again, so use it to ignore a repeat */
    public String id() {
        return id;
    }

    /** @return how the run ended, e.g. {@code run.succeeded} */
    public String type() {
        return type;
    }

    /** @return when the event was made */
    public Instant createdAt() {
        return createdAt;
    }

    /** @return the run that ended, as {@code client.runs().get} returns it */
    public Run run() {
        return run;
    }

    static final class Data {
        final Run run;

        @JsonCreator
        Data(@JsonProperty("run") Run run) {
            this.run = run;
        }
    }

    @Override
    public String toString() {
        return "WebhookEvent{id=" + id + ", type=" + type + ", run=" + (run == null ? null : run.id()) + '}';
    }
}
