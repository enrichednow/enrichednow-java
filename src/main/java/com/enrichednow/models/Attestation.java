package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Terms some formulas need accepted before they run: a formula whose {@code needsAttestation()} is true runs only once
 * an owner or an admin of the account has accepted the current version of the text. A person accepts them, in the
 * console; an API key cannot.
 */
public final class Attestation {
    private final String kind;
    private final String title;
    private final String text;
    private final String textVersion;
    private final boolean accepted;
    private final Instant acceptedAt;
    private final String acceptedBy;

    @JsonCreator
    Attestation(@JsonProperty("kind") String kind,
                @JsonProperty("title") String title,
                @JsonProperty("text") String text,
                @JsonProperty("text_version") String textVersion,
                @JsonProperty("accepted") boolean accepted,
                @JsonProperty("accepted_at") String acceptedAt,
                @JsonProperty("accepted_by") String acceptedBy) {
        this.kind = kind;
        this.title = title;
        this.text = text;
        this.textVersion = textVersion;
        this.accepted = accepted;
        this.acceptedAt = Values.instant(acceptedAt);
        this.acceptedBy = acceptedBy;
    }

    /** @return which terms, e.g. {@code personal_data} */
    public String kind() {
        return kind;
    }

    /** @return their title */
    public String title() {
        return title;
    }

    /** @return the text accepted */
    public String text() {
        return text;
    }

    /** @return the version of the text */
    public String textVersion() {
        return textVersion;
    }

    /** @return whether an owner or an admin accepted this version of the text */
    public boolean accepted() {
        return accepted;
    }

    /** @return when it was accepted, or null */
    public Instant acceptedAt() {
        return acceptedAt;
    }

    /** @return who accepted it, or null */
    public String acceptedBy() {
        return acceptedBy;
    }

    @Override
    public String toString() {
        return "Attestation{kind=" + kind + ", textVersion=" + textVersion + ", accepted=" + accepted + '}';
    }
}
