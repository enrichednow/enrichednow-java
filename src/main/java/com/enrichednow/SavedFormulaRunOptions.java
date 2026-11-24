package com.enrichednow;

import java.time.Duration;

/** How to run a saved formula by hand: with its saved parameters and caps. */
public final class SavedFormulaRunOptions {
    private static final SavedFormulaRunOptions NONE = builder().build();

    private final boolean preview;
    private final boolean includeSeen;
    private final Duration wait;
    private final String idempotencyKey;

    private SavedFormulaRunOptions(Builder builder) {
        this.preview = builder.preview;
        this.includeSeen = builder.includeSeen;
        this.wait = builder.wait;
        this.idempotencyKey = builder.idempotencyKey;
    }

    /** @return the defaults: a full run, delivering only rows not delivered before, returning as soon as it is queued */
    public static SavedFormulaRunOptions none() {
        return NONE;
    }

    /**
     * @param wait how long the API holds the request open for the run to end: 0 to 300 seconds
     * @return the options
     */
    public static SavedFormulaRunOptions waitUpTo(Duration wait) {
        return builder().wait(wait).build();
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** @return whether the run is a preview */
    public boolean isPreview() {
        return preview;
    }

    /** @return whether the run delivers rows already delivered too */
    public boolean includeSeen() {
        return includeSeen;
    }

    /** @return how long the API holds the request open, or {@code null} */
    public Duration waitDuration() {
        return wait;
    }

    /** @return the caller's idempotency key, or {@code null} when the library makes one for the call */
    public String idempotencyKey() {
        return idempotencyKey;
    }

    /** Builds {@link SavedFormulaRunOptions}. */
    public static final class Builder {
        private boolean preview;
        private boolean includeSeen;
        private Duration wait;
        private String idempotencyKey;

        private Builder() {
        }

        /**
         * @param preview true for a preview: at most 10 rows and 50 units, nothing remembered as delivered
         * @return this builder
         */
        public Builder preview(boolean preview) {
            this.preview = preview;
            return this;
        }

        /**
         * @param includeSeen true to deliver rows already delivered too; they are still remembered
         * @return this builder
         */
        public Builder includeSeen(boolean includeSeen) {
            this.includeSeen = includeSeen;
            return this;
        }

        /**
         * @param wait how long the API holds the request open for the run to end: 0 to 300 seconds
         * @return this builder
         */
        public Builder wait(Duration wait) {
            RunOptions.checkWait(wait);
            this.wait = wait;
            return this;
        }

        /**
         * @param idempotencyKey makes this call repeatable without starting a second run, as on
         *                       {@link FormulaRunOptions.Builder#idempotencyKey(String)}
         * @return this builder
         */
        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = RunOptions.checkIdempotencyKey(idempotencyKey);
            return this;
        }

        /** @return the options */
        public SavedFormulaRunOptions build() {
            return new SavedFormulaRunOptions(this);
        }
    }
}
