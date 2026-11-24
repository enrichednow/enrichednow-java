package com.enrichednow;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * One entry to add to the suppression list. The API reads a value in the form formula runs match it: a URL becomes
 * its domain, an address is lower-cased, {@code Acme, Inc.} is {@code acme}.
 */
public final class SuppressionEntry {
    private final SuppressionKind kind;
    private final String value;

    private SuppressionEntry(SuppressionKind kind, String value) {
        this.kind = Objects.requireNonNull(kind, "kind");
        this.value = Objects.requireNonNull(value, "value");
    }

    /**
     * @param kind  what the value is
     * @param value the domain, address or company name, 3 to 320 characters
     * @return the entry
     */
    public static SuppressionEntry of(SuppressionKind kind, String value) {
        return new SuppressionEntry(kind, value);
    }

    /**
     * @param domain a company's domain, or a URL on it
     * @return the entry
     */
    public static SuppressionEntry domain(String domain) {
        return new SuppressionEntry(SuppressionKind.DOMAIN, domain);
    }

    /**
     * @param email an email address
     * @return the entry
     */
    public static SuppressionEntry email(String email) {
        return new SuppressionEntry(SuppressionKind.EMAIL, email);
    }

    /**
     * @param companyName a company's name
     * @return the entry
     */
    public static SuppressionEntry companyName(String companyName) {
        return new SuppressionEntry(SuppressionKind.COMPANY_NAME, companyName);
    }

    /** @return what the value is */
    public SuppressionKind kind() {
        return kind;
    }

    /** @return the value as given */
    public String value() {
        return value;
    }

    Map<String, Object> toMap() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("kind", kind.value());
        out.put("value", value);
        return out;
    }

    @Override
    public String toString() {
        return kind + ":" + value;
    }
}
