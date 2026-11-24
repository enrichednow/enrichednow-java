package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/** What one step of a formula run did: the records it took in and gave out, the calls it made and the units they cost. */
public final class FormulaRunStep {
    private final String id;
    private final String kind;
    private final String status;
    private final long recordsIn;
    private final long recordsOut;
    private final long calls;
    private final long cacheHits;
    private final long units;
    private final List<Map<String, Object>> errors;
    private final Instant startedAt;
    private final Instant finishedAt;

    @JsonCreator
    FormulaRunStep(@JsonProperty("id") String id,
                   @JsonProperty("kind") String kind,
                   @JsonProperty("status") String status,
                   @JsonProperty("records_in") long recordsIn,
                   @JsonProperty("records_out") long recordsOut,
                   @JsonProperty("calls") long calls,
                   @JsonProperty("cache_hits") long cacheHits,
                   @JsonProperty("units") long units,
                   @JsonProperty("errors") List<Map<String, Object>> errors,
                   @JsonProperty("started_at") String startedAt,
                   @JsonProperty("finished_at") String finishedAt) {
        this.id = id;
        this.kind = kind;
        this.status = status;
        this.recordsIn = recordsIn;
        this.recordsOut = recordsOut;
        this.calls = calls;
        this.cacheHits = cacheHits;
        this.units = units;
        this.errors = Values.list(errors);
        this.startedAt = Values.instant(startedAt);
        this.finishedAt = Values.instant(finishedAt);
    }

    /** @return the step's id, as the formula's steps name it */
    public String id() {
        return id;
    }

    /** @return what kind of step it is, e.g. {@code scrape}, {@code filter}; empty for a step that was skipped */
    public String kind() {
        return kind;
    }

    /** @return {@code running}, {@code done}, {@code skipped} or {@code failed} */
    public String status() {
        return status;
    }

    /** @return the records it took in */
    public long recordsIn() {
        return recordsIn;
    }

    /** @return the records it gave out */
    public long recordsOut() {
        return recordsOut;
    }

    /** @return the calls it made */
    public long calls() {
        return calls;
    }

    /** @return of those, the answers read from the cache */
    public long cacheHits() {
        return cacheHits;
    }

    /** @return the units it was charged */
    public long units() {
        return units;
    }

    /** @return what went wrong in it, each as the API wrote it; empty when nothing did */
    public List<Map<String, Object>> errors() {
        return errors;
    }

    /** @return when it started, or null for a step that never did */
    public Instant startedAt() {
        return startedAt;
    }

    /** @return when it ended, or null while it is going */
    public Instant finishedAt() {
        return finishedAt;
    }

    @Override
    public String toString() {
        return "FormulaRunStep{id=" + id + ", kind=" + kind + ", status=" + status + ", recordsOut=" + recordsOut + ", units=" + units + '}';
    }
}
