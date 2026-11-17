package com.enrichednow;

import java.time.Duration;

/** How {@link EnrichedNowClient#scrape(String, Object, ScrapeOptions)} waits and what it does with a run that failed. */
public final class ScrapeOptions {
    private static final ScrapeOptions DEFAULTS = new ScrapeOptions(WaitOptions.DEFAULT_TIMEOUT, WaitOptions.DEFAULT_POLL_INTERVAL, true);

    private final Duration timeout;
    private final Duration pollInterval;
    private final boolean throwOnFailure;

    private ScrapeOptions(Duration timeout, Duration pollInterval, boolean throwOnFailure) {
        this.timeout = timeout;
        this.pollInterval = pollInterval;
        this.throwOnFailure = throwOnFailure;
    }

    /** @return the defaults: give up after 10 minutes, ask every 2 seconds, throw when the run did not succeed */
    public static ScrapeOptions defaults() {
        return DEFAULTS;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** @return how long to wait for the run before giving up */
    public Duration timeout() {
        return timeout;
    }

    /** @return the pause between two looks at the run */
    public Duration pollInterval() {
        return pollInterval;
    }

    /** @return whether a run that ended {@code failed}, {@code unsupported} or {@code cancelled} throws */
    public boolean throwOnFailure() {
        return throwOnFailure;
    }

    /** Builds {@link ScrapeOptions}. */
    public static final class Builder {
        private Duration timeout = WaitOptions.DEFAULT_TIMEOUT;
        private Duration pollInterval = WaitOptions.DEFAULT_POLL_INTERVAL;
        private boolean throwOnFailure = true;

        private Builder() {
        }

        /**
         * @param timeout how long to wait for the run before giving up with a
         *                {@link com.enrichednow.errors.RunTimeoutException}; default 10 minutes
         * @return this builder
         */
        public Builder timeout(Duration timeout) {
            this.timeout = WaitOptions.positive(timeout, "timeout");
            return this;
        }

        /**
         * @param pollInterval the pause between two looks at the run; default 2 seconds
         * @return this builder
         */
        public Builder pollInterval(Duration pollInterval) {
            this.pollInterval = WaitOptions.positive(pollInterval, "pollInterval");
            return this;
        }

        /**
         * @param throwOnFailure {@code false} to get the result back whatever status the run ended with, instead of a
         *                       {@link com.enrichednow.errors.RunFailedException}; default {@code true}
         * @return this builder
         */
        public Builder throwOnFailure(boolean throwOnFailure) {
            this.throwOnFailure = throwOnFailure;
            return this;
        }

        /** @return the options */
        public ScrapeOptions build() {
            return new ScrapeOptions(timeout, pollInterval, throwOnFailure);
        }
    }
}
