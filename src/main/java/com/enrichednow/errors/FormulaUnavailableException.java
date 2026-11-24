package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 409 formula_unavailable}: the formula cannot be run now. Either it is deprecated or not offered
 * ({@link #formulaStatus()}), formulas are switched off for the moment, or the version a saved formula is pinned to is
 * no longer offered ({@link #formulaId()} and {@link #major()}: save it again from the current version).
 */
public class FormulaUnavailableException extends ConflictException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public FormulaUnavailableException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /** @return the formula's status, e.g. {@code deprecated}, when that is why; otherwise {@code null} */
    public String formulaStatus() {
        return detailText("status");
    }

    /** @return for a saved formula, the formula whose version is no longer offered; otherwise {@code null} */
    public String formulaId() {
        return detailText("formula_id");
    }

    /** @return for a saved formula, the major version it is pinned to; otherwise {@code null} */
    public Long major() {
        return detailNumber("major");
    }
}
