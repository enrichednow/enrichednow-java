package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The answer of {@code GET /v1/health}. */
public final class Health {
    private final String status;

    @JsonCreator
    Health(@JsonProperty("status") String status) {
        this.status = status;
    }

    /** @return {@code ok} when the service is up */
    public String status() {
        return status;
    }

    /** @return whether the service said it is up */
    public boolean isOk() {
        return "ok".equals(status);
    }

    @Override
    public String toString() {
        return "Health{status=" + status + '}';
    }
}
