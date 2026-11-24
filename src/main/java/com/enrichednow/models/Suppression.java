package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/** One entry of the account's suppression list: a domain, an email address or a company name formula runs leave out. */
public final class Suppression {
    private final String kind;
    private final String value;
    private final String source;
    private final Instant createdAt;

    @JsonCreator
    Suppression(@JsonProperty("kind") String kind,
                @JsonProperty("value") String value,
                @JsonProperty("source") String source,
                @JsonProperty("created_at") String createdAt) {
        this.kind = kind;
        this.value = value;
        this.source = source;
        this.createdAt = Values.instant(createdAt);
    }

    /** @return {@code domain}, {@code email} or {@code company_name} */
    public String kind() {
        return kind;
    }

    /** @return the value in the form runs match it: a bare domain, a lower-case address, a company name without its suffix */
    public String value() {
        return value;
    }

    /** @return how it was added: {@code manual} or {@code upload} */
    public String source() {
        return source;
    }

    /** @return when it was added */
    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Suppression{kind=" + kind + ", value=" + value + '}';
    }
}
