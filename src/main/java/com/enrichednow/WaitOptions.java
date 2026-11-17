package com.enrichednow;

import java.time.Duration;
import java.util.Objects;

/** How to wait for a run to end. */
public final class WaitOptions {
    static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(10);
    static final Duration DEFAULT_POLL_INTERVAL = Duration.ofSeconds(2);
    private static final WaitOptions DEFAULTS = new WaitOptions(DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);

    private final Duration timeout;
    private final Duration pollInterval;

    private WaitOptions(Duration timeout, Duration pollInterval) {
        this.timeout = timeout;
        this.pollInterval = pollInterval;
    }

    /** @return the defaults: give up after 10 minutes, ask every 2 seconds */
    public static WaitOptions defaults() {
        return DEFAULTS;
    }

    /**
     * @param timeout how long to wait before giving up
     * @return options with this timeout and the default poll interval
     */
    public static WaitOptions timeout(Duration timeout) {
        return builder().timeout(timeout).build();
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** @return how long to wait before giving up */
    public Duration timeout() {
        return timeout;
    }

    /** @return the pause between two looks at the run */
    public Duration pollInterval() {
        return pollInterval;
    }

    /** Builds {@link WaitOptions}. */
    public static final class Builder {
        private Duration timeout = DEFAULT_TIMEOUT;
        private Duration pollInterval = DEFAULT_POLL_INTERVAL;

        private Builder() {
        }

        /**
         * @param timeout how long to wait before giving up with a {@link com.enrichednow.errors.RunTimeoutException};
         *                default 10 minutes
         * @return this builder
         */
        public Builder timeout(Duration timeout) {
            this.timeout = positive(timeout, "timeout");
            return this;
        }

        /**
         * @param pollInterval the pause between two looks at the run; default 2 seconds
         * @return this builder
         */
        public Builder pollInterval(Duration pollInterval) {
            this.pollInterval = positive(pollInterval, "pollInterval");
            return this;
        }

        /** @return the options */
        public WaitOptions build() {
            return new WaitOptions(timeout, pollInterval);
        }
    }

    static Duration positive(Duration value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isNegative() || value.isZero()) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return value;
    }
}
