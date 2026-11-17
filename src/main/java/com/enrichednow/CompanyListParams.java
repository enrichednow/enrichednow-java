package com.enrichednow;

/** Which companies of the dataset to list. */
public final class CompanyListParams {
    private static final CompanyListParams NONE = builder().build();

    final String q;
    final String ats;
    final Integer limit;
    final String cursor;

    private CompanyListParams(Builder builder) {
        this.q = builder.q;
        this.ats = builder.ats;
        this.limit = builder.limit;
        this.cursor = builder.cursor;
    }

    /** @return no filter: every company, by domain */
    public static CompanyListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link CompanyListParams}. */
    public static final class Builder {
        private String q;
        private String ats;
        private Integer limit;
        private String cursor;

        private Builder() {
        }

        /**
         * @param q text that must appear in the domain or in a name the company was seen under
         * @return this builder
         */
        public Builder q(String q) {
            this.q = q;
            return this;
        }

        /**
         * @param ats the applicant tracking system detected, e.g. {@code greenhouse}
         * @return this builder
         */
        public Builder ats(String ats) {
            this.ats = ats;
            return this;
        }

        /**
         * @param limit companies per page: 1 to 200, default 50
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param cursor the {@code nextCursor} of an earlier page of the same list, to start after it
         * @return this builder
         */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /** @return the parameters */
        public CompanyListParams build() {
            return new CompanyListParams(this);
        }
    }
}
