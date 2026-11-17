package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 403 forbidden}: the account may not do this. For example a data request from an account that has not been
 * given Datasets, or, with {@link #reason()} {@code email_not_verified}, an account that has not confirmed its email
 * address yet.
 */
public class PermissionDeniedException extends ApiException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public PermissionDeniedException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }
}
