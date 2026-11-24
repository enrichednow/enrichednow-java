package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** How a formula has been running, as last measured: its sources, and its runs of the last 7 days. */
public final class FormulaHealth {
    private final String status;
    private final List<String> degradedSources;
    private final Double successRate7d;
    private final Double medianSeconds7d;

    @JsonCreator
    FormulaHealth(@JsonProperty("status") String status,
                  @JsonProperty("degraded_sources") List<String> degradedSources,
                  @JsonProperty("success_rate_7d") Double successRate7d,
                  @JsonProperty("median_seconds_7d") Double medianSeconds7d) {
        this.status = status;
        this.degradedSources = Values.list(degradedSources);
        this.successRate7d = successRate7d;
        this.medianSeconds7d = medianSeconds7d;
    }

    /** @return {@code ok}, or {@code degraded} when a source it reads is failing or returning much less than usual */
    public String status() {
        return status;
    }

    /** @return the sources behind a {@code degraded} status */
    public List<String> degradedSources() {
        return degradedSources;
    }

    /** @return the share of its runs of the last 7 days that ended {@code ok} or {@code partial}, or null without enough runs */
    public Double successRate7d() {
        return successRate7d;
    }

    /** @return how long its runs took, the median of the last 7 days in seconds, or null without enough runs */
    public Double medianSeconds7d() {
        return medianSeconds7d;
    }

    /** @return whether the status is {@code ok} */
    public boolean isOk() {
        return "ok".equals(status);
    }

    @Override
    public String toString() {
        return "FormulaHealth{status=" + status + ", degradedSources=" + degradedSources + '}';
    }
}
