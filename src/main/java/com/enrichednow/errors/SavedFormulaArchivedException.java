package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 409 saved_formula_archived}: the saved formula is archived, so it cannot be run, changed, paused or resumed.
 * Duplicate it to use it again.
 */
public class SavedFormulaArchivedException extends ConflictException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public SavedFormulaArchivedException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /** @return the archived saved formula, when the answer names it (on starting a run); otherwise {@code null} */
    public String savedFormulaId() {
        return detailText("saved_formula_id");
    }
}
