package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/** One posting with its description, classification and history, as {@code GET /v1/data/postings/{id}} returns it. */
public final class PostingDetail extends Posting {
    private final String description;
    private final Classification classification;
    private final History history;

    @JsonCreator
    PostingDetail(@JsonProperty("id") String id,
                  @JsonProperty("title") String title,
                  @JsonProperty("title_clean") String titleClean,
                  @JsonProperty("company") CompanyRef company,
                  @JsonProperty("location") Location location,
                  @JsonProperty("employment_type") String employmentType,
                  @JsonProperty("seniority") String seniority,
                  @JsonProperty("function") String function,
                  @JsonProperty("salary") Salary salary,
                  @JsonProperty("url") String url,
                  @JsonProperty("apply_url") String applyUrl,
                  @JsonProperty("source") Source source,
                  @JsonProperty("status") String status,
                  @JsonProperty("posted_at") String postedAt,
                  @JsonProperty("valid_through") String validThrough,
                  @JsonProperty("first_seen_at") String firstSeenAt,
                  @JsonProperty("last_seen_at") String lastSeenAt,
                  @JsonProperty("closed_at") String closedAt,
                  @JsonProperty("description") String description,
                  @JsonProperty("classification") Classification classification,
                  @JsonProperty("history") History history) {
        super(id, title, titleClean, company, location, employmentType, seniority, function, salary, url, applyUrl, source,
                status, postedAt, validThrough, firstSeenAt, lastSeenAt, closedAt);
        this.description = description;
        this.classification = classification;
        this.history = history;
    }

    /** @return the posting's description, or {@code null} */
    public String description() {
        return description;
    }

    /** @return which rules gave the posting its seniority and function */
    public Classification classification() {
        return classification;
    }

    /** @return what happened to the posting over time */
    public History history() {
        return history;
    }

    /** Which rules gave a posting its labels. */
    public static final class Classification {
        private final String seniorityRule;
        private final String functionRule;

        @JsonCreator
        Classification(@JsonProperty("seniority_rule") String seniorityRule, @JsonProperty("function_rule") String functionRule) {
            this.seniorityRule = seniorityRule;
            this.functionRule = functionRule;
        }

        /** @return the rule that gave the seniority, or {@code null} */
        public String seniorityRule() {
            return seniorityRule;
        }

        /** @return the rule that gave the function, or {@code null} */
        public String functionRule() {
            return functionRule;
        }
    }

    /** What happened to a posting over time. */
    public static final class History {
        private final List<Event> events;
        private final List<Change> changes;
        private final long reopenedCount;
        private final Sightings sightings;

        @JsonCreator
        History(@JsonProperty("events") List<Event> events,
                @JsonProperty("changes") List<Change> changes,
                @JsonProperty("reopened_count") long reopenedCount,
                @JsonProperty("sightings") Sightings sightings) {
            this.events = Values.list(events);
            this.changes = Values.list(changes);
            this.reopenedCount = reopenedCount;
            this.sightings = sightings;
        }

        /** @return the posting's events: first seen, closed, reopened */
        public List<Event> events() {
            return events;
        }

        /** @return the fields of the posting that changed */
        public List<Change> changes() {
            return changes;
        }

        /** @return how many times the posting reopened */
        public long reopenedCount() {
            return reopenedCount;
        }

        /** @return when the posting was seen */
        public Sightings sightings() {
            return sightings;
        }
    }

    /** One event in a posting's life. */
    public static final class Event {
        private final String kind;
        private final Instant at;

        @JsonCreator
        Event(@JsonProperty("kind") String kind, @JsonProperty("at") String at) {
            this.kind = kind;
            this.at = Values.instant(at);
        }

        /** @return {@code first_seen}, {@code seen_earlier}, {@code closed} or {@code reopened} */
        public String kind() {
            return kind;
        }

        /** @return when it happened */
        public Instant at() {
            return at;
        }
    }

    /** One change to a field of a posting. */
    public static final class Change {
        private final String field;
        private final Object oldValue;
        private final Object newValue;
        private final Instant at;

        @JsonCreator
        Change(@JsonProperty("field") String field,
               @JsonProperty("old") Object oldValue,
               @JsonProperty("new") Object newValue,
               @JsonProperty("at") String at) {
            this.field = field;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.at = Values.instant(at);
        }

        /** @return the field that changed */
        public String field() {
            return field;
        }

        /** @return the value before, as JSON gave it (a string, number, boolean, list, map or {@code null}) */
        public Object oldValue() {
            return oldValue;
        }

        /** @return the value after, as JSON gave it */
        public Object newValue() {
            return newValue;
        }

        /** @return when the change was seen */
        public Instant at() {
            return at;
        }
    }

    /** When a posting was seen. */
    public static final class Sightings {
        private final long count;
        private final List<Instant> recent;

        @JsonCreator
        Sightings(@JsonProperty("count") long count, @JsonProperty("recent") List<String> recent) {
            this.count = count;
            this.recent = Values.instants(recent);
        }

        /** @return how many times the posting was seen */
        public long count() {
            return count;
        }

        /** @return the most recent times it was seen */
        public List<Instant> recent() {
            return recent;
        }
    }
}
