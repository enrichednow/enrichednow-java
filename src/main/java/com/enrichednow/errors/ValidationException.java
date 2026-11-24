package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 422 invalid_input}: the request does not fit. {@link #fieldErrors()} lists every problem with its path.
 * The same request will get the same answer; fix it before sending it again.
 *
 * <p>{@code 422 unit_cap_too_low} is one too: a formula run's unit cap is below what the formula needs to return
 * something useful, {@link #minUnitCap()}.
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

    /** @return for {@code unit_cap_too_low}, the least unit cap the formula takes; otherwise {@code null} */
    public Long minUnitCap() {
        return detailNumber("min_unit_cap");
    }
}
