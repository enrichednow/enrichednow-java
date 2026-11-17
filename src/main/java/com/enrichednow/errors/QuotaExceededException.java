package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 402 quota_exceeded}: this month's allowance is used up: the unit allowance when starting a run (no run was
 * created), the data allowance on a data request.
 */
public class QuotaExceededException extends ApiException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public QuotaExceededException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /** @return the units used this month, or {@code null} when the answer did not say */
    public Long usedUnits() {
        return detailNumber("used_units");
    }

    /** @return the allowance of the month, or {@code null} when the answer did not say */
    public Long allowanceUnits() {
        return detailNumber("allowance_units");
    }
}
