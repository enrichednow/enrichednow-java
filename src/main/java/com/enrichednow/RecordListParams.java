package com.enrichednow;

/** How to page through a run's records. */
public final class RecordListParams {
    private static final RecordListParams NONE = builder().build();

    final Integer limit;
    final String cursor;

    private RecordListParams(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
    }

    /** @return the defaults: 100 records a page, from the first record */
    public static RecordListParams none() {
        return NONE;
    }

    /**
     * @param limit records per page: 1 to 1000
     * @return parameters with this page size
     */
    public static RecordListParams limit(int limit) {
        return builder().limit(limit).build();
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link RecordListParams}. */
    public static final class Builder {
        private Integer limit;
        private String cursor;

        private Builder() {
        }

        /**
         * @param limit records per page: 1 to 1000, default 100
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

        /** @return the parameters */
        public RecordListParams build() {
            return new RecordListParams(this);
        }
    }
}
