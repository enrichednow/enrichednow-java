package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/** A company of the dataset, known by its domain. */
public final class Company {
    private final String domain;
    private final String name;
    private final List<Name> names;
    private final String careersUrl;
    private final String ats;
    private final Instant firstSeenAt;
    private final Instant lastSeenAt;
    private final LastRead lastRead;
    private final PostingCounts postings;

    @JsonCreator
    Company(@JsonProperty("domain") String domain,
            @JsonProperty("name") String name,
            @JsonProperty("names") List<Name> names,
            @JsonProperty("careers_url") String careersUrl,
            @JsonProperty("ats") String ats,
            @JsonProperty("first_seen_at") String firstSeenAt,
            @JsonProperty("last_seen_at") String lastSeenAt,
            @JsonProperty("last_read") LastRead lastRead,
            @JsonProperty("postings") PostingCounts postings) {
        this.domain = domain;
        this.name = name;
        this.names = Values.list(names);
        this.careersUrl = careersUrl;
        this.ats = ats;
        this.firstSeenAt = Values.instant(firstSeenAt);
        this.lastSeenAt = Values.instant(lastSeenAt);
        this.lastRead = lastRead;
        this.postings = postings;
    }

    /** @return the company's domain */
    public String domain() {
        return domain;
    }

    /** @return the company's name, or {@code null} */
    public String name() {
        return name;
    }

    /** @return the names the company was seen under */
    public List<Name> names() {
        return names;
    }

    /** @return the address of the company's careers page, or {@code null} */
    public String careersUrl() {
        return careersUrl;
    }

    /** @return the applicant tracking system detected, or {@code null} */
    public String ats() {
        return ats;
    }

    /** @return when the company was first seen */
    public Instant firstSeenAt() {
        return firstSeenAt;
    }

    /** @return when the company was last seen */
    public Instant lastSeenAt() {
        return lastSeenAt;
    }

    /** @return how the last reading of the company's postings went */
    public LastRead lastRead() {
        return lastRead;
    }

    /** @return how many of the company's trusted postings are open and closed */
    public PostingCounts postings() {
        return postings;
    }

    @Override
    public String toString() {
        return "Company{domain=" + domain + ", name=" + name + ", ats=" + ats + '}';
    }

    /** A name a company was seen under. */
    public static final class Name {
        private final String name;
        private final Instant firstSeenAt;
        private final Instant lastSeenAt;

        @JsonCreator
        Name(@JsonProperty("name") String name,
             @JsonProperty("first_seen_at") String firstSeenAt,
             @JsonProperty("last_seen_at") String lastSeenAt) {
            this.name = name;
            this.firstSeenAt = Values.instant(firstSeenAt);
            this.lastSeenAt = Values.instant(lastSeenAt);
        }

        /** @return the name */
        public String name() {
            return name;
        }

        /** @return when the name was first seen */
        public Instant firstSeenAt() {
            return firstSeenAt;
        }

        /** @return when the name was last seen */
        public Instant lastSeenAt() {
            return lastSeenAt;
        }
    }

    /** How the last reading of a company's postings went. */
    public static final class LastRead {
        private final Instant at;
        private final String status;
        private final Instant lastCompleteAt;

        @JsonCreator
        LastRead(@JsonProperty("at") String at,
                 @JsonProperty("status") String status,
                 @JsonProperty("last_complete_at") String lastCompleteAt) {
            this.at = Values.instant(at);
            this.status = status;
            this.lastCompleteAt = Values.instant(lastCompleteAt);
        }

        /** @return when the company was last read, or {@code null} */
        public Instant at() {
            return at;
        }

        /** @return {@code complete}, {@code incomplete} or {@code failed}, or {@code null} */
        public String status() {
            return status;
        }

        /** @return when the company was last read completely, or {@code null} */
        public Instant lastCompleteAt() {
            return lastCompleteAt;
        }
    }

    /** How many postings a company has. */
    public static final class PostingCounts {
        private final long open;
        private final long closed;

        @JsonCreator
        PostingCounts(@JsonProperty("open") long open, @JsonProperty("closed") long closed) {
            this.open = open;
            this.closed = closed;
        }

        /** @return the postings that are open */
        public long open() {
            return open;
        }

        /** @return the postings that have closed */
        public long closed() {
            return closed;
        }
    }
}
