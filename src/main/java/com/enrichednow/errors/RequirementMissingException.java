package com.enrichednow.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@code 409 requirement_missing}: the formula needs something this account or deployment does not have.
 * {@link #missing()} names each: {@code product:data} for the Datasets product, a scraper's id for a source. No run was
 * created.
 */
public class RequirementMissingException extends ConflictException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public RequirementMissingException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /** @return what is missing, e.g. {@code product:data}; empty when the answer did not say */
    public List<String> missing() {
        Object value = details().get("missing");
        if (!(value instanceof List)) {
            return Collections.emptyList();
        }
        List<String> out = new ArrayList<>();
        for (Object item : (List<?>) value) {
            out.add(String.valueOf(item));
        }
        return Collections.unmodifiableList(out);
    }
}
