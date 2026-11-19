package com.enrichednow;

import com.enrichednow.models.Standing;

import com.enrichednow.models.Usage;

/** Units used. Reached through {@link EnrichedNowClient#usage()}. */
public final class UsageService {
    private final Transport transport;
    private final Requests requests;

    UsageService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * The units used in the current calendar month (UTC) up to today, a row a day: {@code GET /v1/usage}.
     *
     * @return the report
     */
    public Usage get() {
        return get(UsageParams.none());
    }

    /**
     * The units used over a period: {@code GET /v1/usage}.
     *
     * @param params the period, the grouping and the product
     * @return the report
     */
    public Usage get(UsageParams params) {
        return transport.execute(requests.usage(params));
    }

    /**
     * Where the account stands now: the month's units used and left, the top-up units, when the allowance starts
     * again, and runs in progress. Read from the headers of the smallest call that carries them.
     *
     * @return the standing
     */
    public Standing standing() {
        transport.execute(requests.standing());
        Standing seen = transport.standing();
        return seen != null ? seen : Standing.fromHeaders(java.util.Map.of("X-Units-Limit", java.util.List.of("")));
    }
}
