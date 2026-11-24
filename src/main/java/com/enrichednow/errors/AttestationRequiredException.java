package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 409 attestation_required}: the formula returns people, and an owner or an admin of the account must first
 * accept the terms for it, in the console. An API key cannot accept them; {@code client.attestations().list()} shows
 * the text and whether it is accepted. No run was created.
 */
public class AttestationRequiredException extends ConflictException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public AttestationRequiredException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /** @return which terms must be accepted, e.g. {@code personal_data}; {@code null} when the answer did not say */
    public String kind() {
        return detailText("kind");
    }

    /** @return the version of the text to accept, or {@code null} when the answer did not say */
    public String textVersion() {
        return detailText("text_version");
    }
}
