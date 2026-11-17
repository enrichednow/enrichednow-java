package com.enrichednow.errors;

import java.util.Map;

/**
 * A {@code 5xx} answer: {@code internal} (500) or, for the data API, {@code data_unavailable} (503). Safe requests
 * are retried automatically before this is thrown.
 */
public class ServerException extends ApiException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public ServerException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }
}
