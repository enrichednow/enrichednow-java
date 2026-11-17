package com.enrichednow;

import java.time.Duration;

/** How to start a run. */
public final class RunOptions {
    private static final RunOptions NONE = new RunOptions(null);
    static final Duration MAX_WAIT = Duration.ofSeconds(60);

    private final Duration wait;

    private RunOptions(Duration wait) {
        this.wait = wait;
    }

    /** @return the defaults: the call returns as soon as the run is queued */
    public static RunOptions none() {
        return NONE;
    }

    /**
     * Ask the API to hold the request open until the run ends or this much time is up, whichever comes first. A run
     * that ends in time comes back finished; otherwise it comes back {@code queued} or {@code running} and carries on.
     *
     * @param wait from 0 to 60 seconds; fractions of a second are accepted
     * @return the options
     */
    public static RunOptions waitUpTo(Duration wait) {
        return builder().wait(wait).build();
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** @return how long the API holds the request open, or {@code null} for not at all */
    public Duration waitDuration() {
        return wait;
    }

    /** Builds {@link RunOptions}. */
    public static final class Builder {
        private Duration wait;

        private Builder() {
        }

        /**
         * @param wait how long the API holds the request open for the run to end: 0 to 60 seconds
         * @return this builder
         */
        public Builder wait(Duration wait) {
            if (wait != null && (wait.isNegative() || wait.compareTo(MAX_WAIT) > 0)) {
                throw new IllegalArgumentException("wait must be between 0 and 60 seconds");
            }
            this.wait = wait;
            return this;
        }

        /** @return the options */
        public RunOptions build() {
            return new RunOptions(wait);
        }
    }
}
