package com.enrichednow;

/** Which webhook deliveries to list. */
public final class WebhookDeliveryListParams {
    private static final WebhookDeliveryListParams NONE = builder().build();

    final Integer limit;
    final String cursor;
    final String endpointId;
    final String runId;
    final String status;

    private WebhookDeliveryListParams(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
        this.endpointId = builder.endpointId;
        this.runId = builder.runId;
        this.status = builder.status;
    }

    /** @return no filter: every delivery, newest first */
    public static WebhookDeliveryListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link WebhookDeliveryListParams}. */
    public static final class Builder {
        private Integer limit;
        private String cursor;
        private String endpointId;
        private String runId;
        private String status;

        private Builder() {
        }

        /**
         * @param limit deliveries per page: 1 to 100
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param cursor the {@code nextCursor} of an earlier page
         * @return this builder
         */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /**
         * @param endpointId only deliveries to this endpoint
         * @return this builder
         */
        public Builder endpointId(String endpointId) {
            this.endpointId = endpointId;
            return this;
        }

        /**
         * @param runId only deliveries about this run
         * @return this builder
         */
        public Builder runId(String runId) {
            this.runId = runId;
            return this;
        }

        /**
         * @param status {@code pending}, {@code succeeded} or {@code failed}
         * @return this builder
         */
        public Builder status(String status) {
            this.status = status;
            return this;
        }

        /** @return the parameters */
        public WebhookDeliveryListParams build() {
            return new WebhookDeliveryListParams(this);
        }
    }
}
