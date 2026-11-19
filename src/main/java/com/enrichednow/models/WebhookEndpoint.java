package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/**
 * A URL of yours that is sent a signed {@code POST} when a run ends with one of the events it asked for. As it is
 * created or its secret rotated, {@link #secret()} holds its signing secret: the only time it is shown.
 */
public final class WebhookEndpoint {
    /** Every event an endpoint can ask for: how a run ended. {@code run.succeeded} is the status {@code ok}. */
    public static final List<String> EVENTS = List.of("run.succeeded", "run.partial", "run.failed", "run.unsupported", "run.cancelled");

    private final String id;
    private final String url;
    private final String description;
    private final List<String> events;
    private final boolean enabled;
    private final String disabledReason;
    private final Instant disabledAt;
    private final Instant lastSuccessAt;
    private final Instant lastFailureAt;
    private final Instant createdAt;
    private final String secret;

    @JsonCreator
    WebhookEndpoint(@JsonProperty("id") String id,
                    @JsonProperty("url") String url,
                    @JsonProperty("description") String description,
                    @JsonProperty("events") List<String> events,
                    @JsonProperty("enabled") boolean enabled,
                    @JsonProperty("disabled_reason") String disabledReason,
                    @JsonProperty("disabled_at") String disabledAt,
                    @JsonProperty("last_success_at") String lastSuccessAt,
                    @JsonProperty("last_failure_at") String lastFailureAt,
                    @JsonProperty("created_at") String createdAt,
                    @JsonProperty("secret") String secret) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.events = Values.list(events);
        this.enabled = enabled;
        this.disabledReason = disabledReason;
        this.disabledAt = Values.instant(disabledAt);
        this.lastSuccessAt = Values.instant(lastSuccessAt);
        this.lastFailureAt = Values.instant(lastFailureAt);
        this.createdAt = Values.instant(createdAt);
        this.secret = secret;
    }

    /** @return the endpoint's id, {@code whk_…} */
    public String id() {
        return id;
    }

    /** @return the https URL that is sent the events */
    public String url() {
        return url;
    }

    /** @return your note on it */
    public String description() {
        return description;
    }

    /** @return the events it is told about */
    public List<String> events() {
        return events;
    }

    /** @return false when it was switched off, by you or after repeated failures ({@link #disabledReason()}) */
    public boolean enabled() {
        return enabled;
    }

    /** @return why it was switched off, or null */
    public String disabledReason() {
        return disabledReason;
    }

    /** @return when it was switched off, or null */
    public Instant disabledAt() {
        return disabledAt;
    }

    /** @return when a delivery last succeeded, or null */
    public Instant lastSuccessAt() {
        return lastSuccessAt;
    }

    /** @return when a delivery last failed, or null */
    public Instant lastFailureAt() {
        return lastFailureAt;
    }

    /** @return when it was added */
    public Instant createdAt() {
        return createdAt;
    }

    /** @return the signing secret ({@code whsec_…}) as the endpoint was created or rotated; null otherwise */
    public String secret() {
        return secret;
    }

    @Override
    public String toString() {
        return "WebhookEndpoint{id=" + id + ", url=" + url + ", events=" + events + ", enabled=" + enabled + '}';
    }
}
