package com.enrichednow.errors;

import java.util.Map;

/**
 * A {@code 409} answer: the request conflicts with the state of what it names, and the same request gets the same
 * answer until that state changes. Not retried.
 *
 * <p>{@code idempotency_conflict} (the idempotency key was used for a different request) is this class itself. The
 * formula codes have subclasses: {@link AttestationRequiredException}, {@link FormulaUnavailableException},
 * {@link RequirementMissingException} and {@link SavedFormulaArchivedException}.
 */
public class ConflictException extends ApiException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public ConflictException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }
}
