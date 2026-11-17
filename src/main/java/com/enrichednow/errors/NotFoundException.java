package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 404 not_found}: there is no such scraper, run, posting or company, or it is not yours to see.
 */
public class NotFoundException extends ApiException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public NotFoundException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }
}
