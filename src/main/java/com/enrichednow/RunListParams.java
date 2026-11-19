package com.enrichednow;

import com.enrichednow.models.RunStatus;
import java.time.Instant;

/** Which runs to list. */
public final class RunListParams {
    private static final RunListParams NONE = builder().build();

    final Integer limit;
    final String cursor;
    final String scraperId;
    final RunStatus status;
    final String apiKeyId;
    final Instant from;
    final Instant to;

    private RunListParams(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
        this.scraperId = builder.scraperId;
        this.status = builder.status;
        this.apiKeyId = builder.apiKeyId;
        this.from = builder.from;
        this.to = builder.to;
    }

    /** @return no filter: every run of the account, newest first */
    public static RunListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link RunListParams}. */
    public static final class Builder {
        private Integer limit;
        private String cursor;
        private String scraperId;
        private RunStatus status;
        private String apiKeyId;
        private Instant from;
        private Instant to;

        private Builder() {
        }

        /**
         * @param apiKeyId only runs started with this API key ({@code key_…})
         * @return this builder
         */
        public Builder apiKeyId(String apiKeyId) {
            this.apiKeyId = apiKeyId;
            return this;
        }

        /**
         * @param from only runs created at or after this time
         * @return this builder
         */
        public Builder from(Instant from) {
            this.from = from;
            return this;
        }

        /**
         * @param to only runs created before this time
         * @return this builder
         */
        public Builder to(Instant to) {
            this.to = to;
            return this;
        }

        /**
         * @param limit runs per page: 1 to 100, default 20
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param cursor the {@code nextCursor} of an earlier page, to start after it
         * @return this builder
         */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /**
         * @param scraperId only runs of this scraper
         * @return this builder
         */
        public Builder scraperId(String scraperId) {
            this.scraperId = scraperId;
            return this;
        }

        /**
         * @param status only runs with this status
         * @return this builder
         */
        public Builder status(RunStatus status) {
            if (status == RunStatus.UNKNOWN) {
                throw new IllegalArgumentException("UNKNOWN is not a status the API can filter by");
            }
            this.status = status;
            return this;
        }

        /** @return the parameters */
        public RunListParams build() {
            return new RunListParams(this);
        }
    }
}
