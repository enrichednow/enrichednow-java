package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 402 limit_reached}: the run would go past a spend limit the account set itself: its monthly limit
 * ({@link #scope()} {@code "account"}) or the API key's ({@code "key"}, with {@link #keyId()}). Units may be left on
 * the plan: raising the limit, in the console, is what helps. No run was created. It is a
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

    /** @return {@code "account"} or {@code "key"}: whose limit was reached; {@code null} when the answer did not say */
    public String scope() {
        Object value = details().get("scope");
        return value instanceof String ? (String) value : null;
    }

    /** @return the API key whose limit was reached, when {@link #scope()} is {@code "key"}; otherwise {@code null} */
    public String keyId() {
        Object value = details().get("key_id");
        return value instanceof String ? (String) value : null;
    }

    /** @return the limit, in units a calendar month, or {@code null} when the answer did not say */
    public Long limitUnits() {
        return detailNumber("limit_units");
    }
}
