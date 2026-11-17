package com.enrichednow;

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
}
