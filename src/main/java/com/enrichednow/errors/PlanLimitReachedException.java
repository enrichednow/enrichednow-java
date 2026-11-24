package com.enrichednow.errors;

import java.util.Map;

/**
 * {@code 403 plan_limit_reached}: the account's plan does not allow this many active schedules, a cadence this short,
 * or this many destinations ({@link #limit()}). Pause one, choose a longer cadence, or change the plan on the Billing
 * page. It is a {@link PermissionDeniedException}, so code that catches that one catches this too.
 */
public class PlanLimitReachedException extends PermissionDeniedException {
    private static final long serialVersionUID = 1L;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public PlanLimitReachedException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(statusCode, code, apiMessage, details, requestId);
    }

    /** @return which limit: {@code schedules}, {@code min_interval_minutes} or {@code destinations}; or {@code null} */
    public String limit() {
        return detailText("limit");
    }

    /** @return what the plan allows of it, or {@code null} when the answer did not say */
    public Long allowed() {
        return detailNumber("allowed");
    }

    /** @return how many there are now, for {@code schedules} and {@code destinations}; otherwise {@code null} */
    public Long current() {
        return detailNumber("current");
    }

    /** @return what was asked for, for {@code min_interval_minutes}: the interval in minutes; otherwise {@code null} */
    public Long asked() {
        return detailNumber("asked");
    }
}
