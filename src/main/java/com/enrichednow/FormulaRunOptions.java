package com.enrichednow;

import java.time.Duration;

/**
 * How to run a formula: a preview or a full run, its caps, and for {@link FormulaService#run} the wait and the
 * idempotency key; for {@link EnrichedNowClient#runFormula} also how long to wait for it and whether a run that failed
 * throws. {@link FormulaService#estimate} estimates the run these options would start.
 *
 * <pre>{@code
 * FormulaRunOptions.builder()
 *         .unitCap(2000)                        // the most it may spend
 *         .rowCap(100)                          // the most rows it may return
 *         .timeout(Duration.ofMinutes(15))      // runFormula: give up waiting after this
 *         .build();
 * }</pre>
 */
public final class FormulaRunOptions {
    private static final FormulaRunOptions NONE = builder().build();
    private static final FormulaRunOptions PREVIEW = builder().preview(true).build();

    private final boolean preview;
    private final Integer unitCap;
    private final Integer rowCap;
    private final Duration wait;
    private final String idempotencyKey;
    private final Duration timeout;
    private final Duration pollInterval;
    private final boolean throwOnFailure;

    private FormulaRunOptions(Builder builder) {
        this.preview = builder.preview;
        this.unitCap = builder.unitCap;
        this.rowCap = builder.rowCap;
        this.wait = builder.wait;
        this.idempotencyKey = builder.idempotencyKey;
        this.timeout = builder.timeout;
        this.pollInterval = builder.pollInterval;
        this.throwOnFailure = builder.throwOnFailure;
    }

    /** @return the defaults: a full run with the formula's caps, returning as soon as it is queued */
    public static FormulaRunOptions none() {
        return NONE;
    }

    /** @return a preview run: at most 10 rows and 50 units, charged as usual, nothing remembered as delivered */
    public static FormulaRunOptions preview() {
        return PREVIEW;
    }

    /**
     * Ask the API to hold the request open until the run ends or this much time is up, whichever comes first.
     *
     * @param wait from 0 to 300 seconds; fractions of a second are accepted
     * @return the options
     */
    public static FormulaRunOptions waitUpTo(Duration wait) {
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

    /** @return the most the run may spend, or {@code null} for the formula's default */
    public Integer unitCap() {
        return unitCap;
    }

    /** @return the most rows the run may return, or {@code null} for the default */
    public Integer rowCap() {
        return rowCap;
    }

    /** @return how long the API holds the request that starts the run open, or {@code null} */
    public Duration waitDuration() {
        return wait;
    }

    /** @return the caller's idempotency key, or {@code null} when the library makes one for the call */
    public String idempotencyKey() {
        return idempotencyKey;
    }

    /** @return {@code runFormula}: how long to wait for the run before giving up */
    public Duration timeout() {
        return timeout;
    }

    /** @return {@code runFormula}: the least time between two looks at the run */
    public Duration pollInterval() {
        return pollInterval;
    }

    /** @return {@code runFormula}: whether a run that ended {@code failed}, {@code unsupported} or {@code cancelled} throws */
    public boolean throwOnFailure() {
        return throwOnFailure;
    }

    /** The same options with this wait on starting the run. */
    FormulaRunOptions withWait(Duration first) {
        Builder copy = builder().preview(preview).wait(first).idempotencyKey(idempotencyKey).timeout(timeout)
                .pollInterval(pollInterval).throwOnFailure(throwOnFailure);
        copy.unitCap = unitCap;
        copy.rowCap = rowCap;
        return copy.build();
    }

    /** Builds {@link FormulaRunOptions}. */
    public static final class Builder {
        private boolean preview;
        private Integer unitCap;
        private Integer rowCap;
        private Duration wait;
        private String idempotencyKey;
        private Duration timeout = WaitOptions.DEFAULT_TIMEOUT;
        private Duration pollInterval = WaitOptions.DEFAULT_POLL_INTERVAL;
        private boolean throwOnFailure = true;

        private Builder() {
        }

        /**
         * @param preview true for a preview: at most 10 rows and 50 units, charged as usual, nothing remembered as
         *                delivered; default false
         * @return this builder
         */
        public Builder preview(boolean preview) {
            this.preview = preview;
            return this;
        }

        /**
         * @param unitCap the most the run may spend; it cannot be overspent: it stops at the cap, keeps what it has and
         *                ends {@code partial}. Default: the formula's
         * @return this builder
         */
        public Builder unitCap(int unitCap) {
            this.unitCap = positive(unitCap, "unitCap");
            return this;
        }

        /**
         * @param rowCap the most rows the run may return; default: its {@code max_results}, or the formula's
         * @return this builder
         */
        public Builder rowCap(int rowCap) {
            this.rowCap = positive(rowCap, "rowCap");
            return this;
        }

        /**
         * {@link FormulaService#run}: how long the API holds the request open for the run to end. {@code runFormula}
         * holds its first request for this long too, or for 60 seconds when it is not set; never past its timeout.
         *
         * @param wait 0 to 300 seconds
         * @return this builder
         */
        public Builder wait(Duration wait) {
            RunOptions.checkWait(wait);
            this.wait = wait;
            return this;
        }

        /**
         * Names this call so that it can be repeated without starting a second run: for 24 hours the same key with the
         * same request gives the run the first call started, and with a different request is refused (a
         * {@link com.enrichednow.errors.ConflictException}, {@code idempotency_conflict}). Without one, the library
         * makes a key for each call, which is what makes its retries safe.
         *
         * @param idempotencyKey any text of yours, up to 255 characters, no spaces
         * @return this builder
         */
        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = RunOptions.checkIdempotencyKey(idempotencyKey);
            return this;
        }

        /**
         * @param timeout {@code runFormula}: how long to wait for the run before giving up with a
         *                {@link com.enrichednow.errors.RunTimeoutException}; default 10 minutes
         * @return this builder
         */
        public Builder timeout(Duration timeout) {
            this.timeout = WaitOptions.positive(timeout, "timeout");
            return this;
        }

        /**
         * @param pollInterval {@code runFormula}: the least time between two looks at the run; default 2 seconds. Each
         *                     look asks the API to hold the request until the run ends, so this only paces an answer
         *                     that came back early
         * @return this builder
         */
        public Builder pollInterval(Duration pollInterval) {
            this.pollInterval = WaitOptions.positive(pollInterval, "pollInterval");
            return this;
        }

        /**
         * @param throwOnFailure {@code runFormula}: {@code false} to get the result back whatever status the run ended
         *                       with, instead of a {@link com.enrichednow.errors.FormulaRunFailedException}; default
         *                       {@code true}
         * @return this builder
         */
        public Builder throwOnFailure(boolean throwOnFailure) {
            this.throwOnFailure = throwOnFailure;
            return this;
        }

        /** @return the options */
        public FormulaRunOptions build() {
            return new FormulaRunOptions(this);
        }
    }

    static int positive(int value, String name) {
        if (value < 1) {
            throw new IllegalArgumentException(name + " must be at least 1");
        }
        return value;
    }
}
