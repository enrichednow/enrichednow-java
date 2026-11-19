package com.enrichednow;

import java.time.Duration;

/** How to start a run. */
public final class RunOptions {
    private static final RunOptions NONE = new RunOptions(null, null, null);
    /** The longest the API holds a request open for a run to end. */
    public static final Duration MAX_WAIT = Duration.ofSeconds(300);
    /** How long {@code scrape} asks the API to hold its first request before it polls. */
    static final Duration SCRAPE_WAIT = Duration.ofSeconds(60);

    private final Duration wait;
    private final String idempotencyKey;
    private final String webhookUrl;

    private RunOptions(Duration wait, String idempotencyKey, String webhookUrl) {
        this.wait = wait;
        this.idempotencyKey = idempotencyKey;
        this.webhookUrl = webhookUrl;
    }

    /** @return the https URL told when this run ends, or {@code null} */
    public String webhookUrl() {
        return webhookUrl;
    }

    static void checkWait(Duration wait) {
        if (wait != null && (wait.isNegative() || wait.compareTo(MAX_WAIT) > 0)) {
            throw new IllegalArgumentException("wait must be between 0 and 300 seconds");
        }
    }

    /** @return the defaults: the call returns as soon as the run is queued */
    public static RunOptions none() {
        return NONE;
    }

    /**
     * Ask the API to hold the request open until the run ends or this much time is up, whichever comes first. A run
     * that ends in time comes back finished; otherwise it comes back {@code queued} or {@code running} and carries on.
     *
     * @param wait from 0 to 300 seconds; fractions of a second are accepted
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

    /** @return the caller's idempotency key, or {@code null} when the library makes one for the call */
    public String idempotencyKey() {
        return idempotencyKey;
    }

    /** Builds {@link RunOptions}. */
    public static final class Builder {
        private Duration wait;
        private String idempotencyKey;
        private String webhookUrl;

        private Builder() {
        }

        /**
         * @param wait how long the API holds the request open for the run to end: 0 to 300 seconds
         * @return this builder
         */
        public Builder wait(Duration wait) {
            checkWait(wait);
            this.wait = wait;
            return this;
        }

        /**
         * An {@code https} URL that is sent a signed {@code POST} when this run ends, whichever way it ends, beside the
         * account's webhook endpoints. The account needs a secret for these first:
         * {@code client.webhooks().createRunSecret()}.
         *
         * @param webhookUrl the URL
         * @return this builder
         */
        public Builder webhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
            return this;
        }

        /**
         * Names this call so that it can be repeated without starting a second run: for 24 hours the same key with
         * the same request gives the run the first call started, and with a different request is refused (409
         * {@code idempotency_conflict}). Without one, the library makes a key for each call, which is what makes its
         * retries safe.
         *
         * @param idempotencyKey any text of yours, up to 255 characters, no spaces
         * @return this builder
         */
        public Builder idempotencyKey(String idempotencyKey) {
            if (idempotencyKey != null && !idempotencyKey.matches("[\\x21-\\x7e]{1,255}")) {
                throw new IllegalArgumentException("idempotencyKey must be 1 to 255 visible ASCII characters, without spaces");
            }
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        /** @return the options */
        public RunOptions build() {
            return new RunOptions(wait, idempotencyKey, webhookUrl);
        }
    }
}
