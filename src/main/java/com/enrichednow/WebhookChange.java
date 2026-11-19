package com.enrichednow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** What {@link WebhookService#update} changes of an endpoint; what is not set is left as it is. */
public final class WebhookChange {
    private final Map<String, Object> body;

    private WebhookChange(Map<String, Object> body) {
        this.body = Collections.unmodifiableMap(body);
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> body() {
        return body;
    }

    /** Builds a {@link WebhookChange}. */
    public static final class Builder {
        private final Map<String, Object> body = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * @param url the new https URL
         * @return this builder
         */
        public Builder url(String url) {
            body.put("url", url);
            return this;
        }

        /**
         * @param events the events it is told about, from {@link com.enrichednow.models.WebhookEndpoint#EVENTS}
         * @return this builder
         */
        public Builder events(List<String> events) {
            body.put("events", new ArrayList<>(events));
            return this;
        }

        /**
         * @param description your note on it
         * @return this builder
         */
        public Builder description(String description) {
            body.put("description", description);
            return this;
        }

        /**
         * @param enabled true switches an endpoint that was switched off back on
         * @return this builder
         */
        public Builder enabled(boolean enabled) {
            body.put("enabled", enabled);
            return this;
        }

        /** @return the change */
        public WebhookChange build() {
            return new WebhookChange(new LinkedHashMap<>(body));
        }
    }
}
