package com.enrichednow;

/** Which formulas of the catalogue to list. Every filter given must hold. */
public final class FormulaListParams {
    private static final FormulaListParams NONE = builder().build();

    final String query;
    final String category;
    final String persona;
    final String status;

    private FormulaListParams(Builder builder) {
        this.query = builder.query;
        this.category = builder.category;
        this.persona = builder.persona;
        this.status = builder.status;
    }

    /** @return no filter: every formula listed for this account */
    public static FormulaListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link FormulaListParams}. */
    public static final class Builder {
        private String query;
        private String category;
        private String persona;
        private String status;

        private Builder() {
        }

        /**
         * @param query what you want, in your own words, e.g. {@code companies hiring Java developers in London}: the
         *     formulas that match come best first
         * @return this builder
         */
        public Builder query(String query) {
            this.query = query;
            return this;
        }

        /**
         * @param category only formulas of this category, e.g. {@code recruiting}
         * @return this builder
         */
        public Builder category(String category) {
            this.category = category;
            return this;
        }

        /**
         * @param persona only formulas for this persona, e.g. {@code recruiter}
         * @return this builder
         */
        public Builder persona(String persona) {
            this.persona = persona;
            return this;
        }

        /**
         * @param status only formulas with this status: {@code beta} or {@code live}
         * @return this builder
         */
        public Builder status(String status) {
            this.status = status;
            return this;
        }

        /** @return the parameters */
        public FormulaListParams build() {
            return new FormulaListParams(this);
        }
    }
}
