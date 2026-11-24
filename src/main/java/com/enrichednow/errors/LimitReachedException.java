package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 402 limit_reached}: the run would go past a spend limit set on the account, not the plan's allowance:
 * {@link #scope()} says whose. {@code "account"}: the account's own monthly limit. {@code "key"}: the API key's, with
 * {@link #keyId()}. {@code "saved_formula"}: a saved formula's monthly unit cap, with {@link #savedFormulaId()} (raise
 * it with {@code SavedFormulaChange}, or wait for next month). {@code "formula_run"}: a formula run met a price that
 * changed while it ran. Units may be left on the plan: raising the limit is what helps. No run was created. It is a
 * {@link QuotaExceededException}, so code that catches that one catches this too.
 */
public class LimitReachedException extends QuotaExceededException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public LimitReachedException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /**
     * @return whose limit was reached: {@code "account"}, {@code "key"}, {@code "saved_formula"} or
     *     {@code "formula_run"}; {@code null} when the answer did not say
     */
    public String scope() {
        return detailText("scope");
    }

    /** @return the API key whose limit was reached, when {@link #scope()} is {@code "key"}; otherwise {@code null} */
    public String keyId() {
        return detailText("key_id");
    }

    /** @return the saved formula whose monthly cap was reached, when {@link #scope()} is {@code "saved_formula"}; otherwise {@code null} */
    public String savedFormulaId() {
        return detailText("saved_formula_id");
    }

    /** @return the limit, in units a calendar month, or {@code null} when the answer did not say */
    public Long limitUnits() {
        return detailNumber("limit_units");
    }
}
