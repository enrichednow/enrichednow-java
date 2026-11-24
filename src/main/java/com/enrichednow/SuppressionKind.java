package com.enrichednow;

/** What an entry of the suppression list matches. */
public enum SuppressionKind {
    /** A company's domain: any address or website on it. */
    DOMAIN("domain"),
    /** One email address. */
    EMAIL("email"),
    /** A company name, matched without its legal suffix: {@code Acme, Inc.} is {@code acme}. */
    COMPANY_NAME("company_name");

    private final String value;

    SuppressionKind(String value) {
        this.value = value;
    }

    /** @return the kind as the API writes it */
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
