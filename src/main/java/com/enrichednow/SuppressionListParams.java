package com.enrichednow;

/** Which entries of the suppression list to list. */
public final class SuppressionListParams {
    private static final SuppressionListParams NONE = builder().build();

    final SuppressionKind kind;
    final Integer limit;
    final String cursor;

    private SuppressionListParams(Builder builder) {
        this.kind = builder.kind;
        this.limit = builder.limit;
        this.cursor = builder.cursor;
    }

    /** @return no filter: the whole list */
    public static SuppressionListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SuppressionListParams}. */
    public static final class Builder {
        private SuppressionKind kind;
        private Integer limit;
        private String cursor;

        private Builder() {
        }

        /**
         * @param kind only entries of this kind
         * @return this builder
         */
        public Builder kind(SuppressionKind kind) {
            this.kind = kind;
            return this;
        }

        /**
         * @param limit entries per page: 1 to 1000, default 100
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
        public SuppressionListParams build() {
            return new SuppressionListParams(this);
        }
    }
}
