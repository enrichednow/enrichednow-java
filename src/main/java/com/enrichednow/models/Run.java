package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;

/**
 * One execution of one scraper with one input. It works in the background and ends with a status and a set of
 * records.
 */
public final class Run {
    private final String id;
    private final String scraperId;
    private final int scraperVersion;
    private final String rawStatus;
    private final RunStatus status;
    private final String reason;
    private final Map<String, Object> input;
    private final long recordCount;
    private final long units;
    private final Diagnostics diagnostics;
    private final Instant createdAt;
    private final Instant startedAt;
    private final Instant finishedAt;

    @JsonCreator
    Run(@JsonProperty("id") String id,
        @JsonProperty("scraper_id") String scraperId,
        @JsonProperty("scraper_version") int scraperVersion,
        @JsonProperty("status") String status,
        @JsonProperty("reason") String reason,
        @JsonProperty("input") Map<String, Object> input,
        @JsonProperty("record_count") long recordCount,
        @JsonProperty("units") long units,
        @JsonProperty("diagnostics") Diagnostics diagnostics,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("started_at") String startedAt,
        @JsonProperty("finished_at") String finishedAt) {
        this.id = id;
        this.scraperId = scraperId;
        this.scraperVersion = scraperVersion;
        this.rawStatus = status;
        this.status = RunStatus.fromValue(status);
        this.reason = reason;
        this.input = Values.map(input);
        this.recordCount = recordCount;
        this.units = units;
        this.diagnostics = diagnostics;
        this.createdAt = Values.instant(createdAt);
        this.startedAt = Values.instant(startedAt);
        this.finishedAt = Values.instant(finishedAt);
    }

    /** @return the run's id */
    public String id() {
        return id;
    }

    /** @return the id of the scraper that ran */
    public String scraperId() {
        return scraperId;
    }

    /** @return the version of the scraper the run ran with */
    public int scraperVersion() {
        return scraperVersion;
    }

    /** @return the run's status */
    public RunStatus status() {
        return status;
    }

    /** @return the status exactly as the API wrote it */
    public String rawStatus() {
        return rawStatus;
    }

    /**
     * @return a stable code whenever the status is not {@code ok}, e.g. {@code render_required}, {@code fetch_failed},
     *     {@code cancelled}; {@code null} when it is
     */
    public String reason() {
        return reason;
    }

    /** @return the input the run was started with */
    public Map<String, Object> input() {
        return input;
    }

    /** @return how many records the run stored */
    public long recordCount() {
        return recordCount;
    }

    /** @return the units charged: 0 until the run ends {@code ok} or {@code partial} */
    public long units() {
        return units;
    }

    /** @return what the run did; {@code null} until it ends, and for a run that ended before reporting anything */
    public Diagnostics diagnostics() {
        return diagnostics;
    }

    /** @return when the run was created */
    public Instant createdAt() {
        return createdAt;
    }

    /** @return when the run started working, or {@code null} */
    public Instant startedAt() {
        return startedAt;
    }

    /** @return when the run ended, or {@code null} while it is in progress */
    public Instant finishedAt() {
        return finishedAt;
    }

    /** @return whether the run has ended, with any final status */
    public boolean isFinished() {
        return status.isFinal();
    }

    /** @return whether the run ended {@code ok} or {@code partial} */
    public boolean isSuccessful() {
        return status.isSuccessful();
    }

    @Override
    public String toString() {
        return "Run{id=" + id + ", scraperId=" + scraperId + ", status=" + rawStatus + ", reason=" + reason
                + ", recordCount=" + recordCount + ", units=" + units + '}';
    }

    /** What a run did. */
    public static final class Diagnostics {
        private final long requests;
        private final Long bytes;
        private final Long wallMs;
        private final Map<String, Object> facts;

        @JsonCreator
        Diagnostics(@JsonProperty("requests") long requests,
                    @JsonProperty("bytes") Long bytes,
                    @JsonProperty("wall_ms") Long wallMs,
                    @JsonProperty("facts") Map<String, Object> facts) {
            this.requests = requests;
            this.bytes = bytes;
            this.wallMs = wallMs;
            this.facts = Values.map(facts);
        }

        /** @return the requests the run made, in total */
        public long requests() {
            return requests;
        }

        /** @return the bytes fetched, or {@code null} when they were not measured */
        public Long bytes() {
            return bytes;
        }

        /** @return how long the run took in milliseconds, or {@code null} */
        public Long wallMs() {
            return wallMs;
        }

        /**
         * @return what the scraper learned about the target that a caller can act on, e.g. {@code careers_url},
         *     {@code ats}, {@code pages_read}; which facts depends on the scraper
         */
        public Map<String, Object> facts() {
            return facts;
        }

        @Override
        public String toString() {
            return "Diagnostics{requests=" + requests + ", bytes=" + bytes + ", wallMs=" + wallMs + ", facts=" + facts + '}';
        }
    }
}
