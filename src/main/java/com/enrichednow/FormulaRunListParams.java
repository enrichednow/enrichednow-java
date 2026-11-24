package com.enrichednow;

import com.enrichednow.models.RunStatus;

/** Which formula runs to list. */
public final class FormulaRunListParams {
    private static final FormulaRunListParams NONE = builder().build();

    final Integer limit;
    final String cursor;
    final String formulaId;
    final String savedFormulaId;
    final RunStatus status;

    private FormulaRunListParams(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
        this.formulaId = builder.formulaId;
        this.savedFormulaId = builder.savedFormulaId;
        this.status = builder.status;
    }

    /** @return no filter: every formula run of the account, newest first */
    public static FormulaRunListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link FormulaRunListParams}. */
    public static final class Builder {
        private Integer limit;
        private String cursor;
        private String formulaId;
        private String savedFormulaId;
        private RunStatus status;

        private Builder() {
        }

        /**
         * @param formulaId only runs of this formula
         * @return this builder
         */
        public Builder formulaId(String formulaId) {
            this.formulaId = formulaId;
            return this;
        }

        /**
         * @param savedFormulaId only runs of this saved formula
         * @return this builder
         */
        public Builder savedFormulaId(String savedFormulaId) {
            this.savedFormulaId = savedFormulaId;
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

        /** @return the parameters */
        public FormulaRunListParams build() {
            return new FormulaRunListParams(this);
        }
    }
}
