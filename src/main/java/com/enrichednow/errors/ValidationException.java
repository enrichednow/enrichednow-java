package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 422 invalid_input}: the request does not fit. {@link #fieldErrors()} lists every problem with its path.
 * The same request will get the same answer; fix it before sending it again.
 */
public class ValidationException extends ApiException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public ValidationException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }
}
