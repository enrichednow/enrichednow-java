package com.enrichednow;

/** Which saved formulas to list. */
public final class SavedFormulaListParams {
    private static final SavedFormulaListParams NONE = builder().build();

    /** The status of a saved formula. */
    public enum Status {
        /** It runs on its schedule, if it has one. */
        ACTIVE("active"),
        /** No scheduled run starts; it can still be run by hand. */
        PAUSED("paused"),
        /** It does not run. Archived ones are listed only when asked for. */
        ARCHIVED("archived");

        final String value;

        Status(String value) {
            this.value = value;
        }
    }

    final Integer limit;
    final String cursor;
    final String formulaId;
    final Status status;

    private SavedFormulaListParams(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
        this.formulaId = builder.formulaId;
        this.status = builder.status;
    }

    /** @return no filter: the account's saved formulas that are not archived, newest first */
    public static SavedFormulaListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SavedFormulaListParams}. */
    public static final class Builder {
        private Integer limit;
        private String cursor;
        private String formulaId;
        private Status status;

        private Builder() {
        }

        /**
         * @param formulaId only saved formulas of this formula
         * @return this builder
         */
        public Builder formulaId(String formulaId) {
            this.formulaId = formulaId;
            return this;
        }

        /**
         * @param status only those with this status; {@link Status#ARCHIVED} lists the archived ones
         * @return this builder
         */
        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        /**
         * @param limit saved formulas per page: 1 to 100, default 50
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
        public SavedFormulaListParams build() {
            return new SavedFormulaListParams(this);
        }
    }
}
