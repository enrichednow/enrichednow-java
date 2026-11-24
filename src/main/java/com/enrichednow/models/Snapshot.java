package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;

/** What a saved formula observed of one entity (a job, a product, a price) at a time its tracked values changed. */
public final class Snapshot {
    private final String entity;
    private final String entityKey;
    private final Map<String, Object> fields;
    private final String runId;
    private final Instant observedAt;

    @JsonCreator
    Snapshot(@JsonProperty("entity") String entity,
             @JsonProperty("entity_key") String entityKey,
             @JsonProperty("fields") Map<String, Object> fields,
             @JsonProperty("run_id") String runId,
             @JsonProperty("observed_at") String observedAt) {
        this.entity = entity;
        this.entityKey = entityKey;
        this.fields = Values.map(fields);
        this.runId = runId;
        this.observedAt = Values.instant(observedAt);
    }

    /** @return what kind of entity it is, e.g. {@code job} */
    public String entity() {
        return entity;
    }

    /** @return which one it is, e.g. its address */
    public String entityKey() {
        return entityKey;
    }

    /** @return the tracked values as they were then, by name */
    public Map<String, Object> fields() {
        return fields;
    }

    /** @return the run that observed them, or null */
    public String runId() {
        return runId;
    }

    /** @return when they were observed */
    public Instant observedAt() {
        return observedAt;
    }

    @Override
    public String toString() {
        return "Snapshot{entity=" + entity + ", entityKey=" + entityKey + ", fields=" + fields + ", observedAt=" + observedAt + '}';
    }
}
