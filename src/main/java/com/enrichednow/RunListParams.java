package com.enrichednow;

import com.enrichednow.models.RunStatus;

/** Which runs to list. */
public final class RunListParams {
    private static final RunListParams NONE = builder().build();

    final Integer limit;
    final String cursor;
    final String scraperId;
    final RunStatus status;

    private RunListParams(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
        this.scraperId = builder.scraperId;
        this.status = builder.status;
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

        private Builder() {
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
