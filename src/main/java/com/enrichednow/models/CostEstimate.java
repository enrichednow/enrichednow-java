package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * About what a formula run costs and returns. An estimate, not a quote: {@link #maxUnits()} is the only promise, and a
 * run never spends past its unit cap.
 */
public final class CostEstimate {
    private final long minUnits;
    private final long expectedUnits;
    private final long maxUnits;
    private final long expectedRows;
    private final String basis;

    @JsonCreator
    CostEstimate(@JsonProperty("min_units") long minUnits,
                 @JsonProperty("expected_units") long expectedUnits,
                 @JsonProperty("max_units") long maxUnits,
                 @JsonProperty("expected_rows") long expectedRows,
                 @JsonProperty("basis") String basis) {
        this.minUnits = minUnits;
        this.expectedUnits = expectedUnits;
        this.maxUnits = maxUnits;
        this.expectedRows = expectedRows;
        this.basis = basis;
    }

    /** @return the least the run can cost: an account that cannot pay this much is refused */
    public long minUnits() {
        return minUnits;
    }

    /** @return about what it will cost */
    public long expectedUnits() {
        return expectedUnits;
    }

    /** @return the most it can cost: never above its unit cap */
    public long maxUnits() {
        return maxUnits;
    }

    /** @return about how many rows it will return */
    public long expectedRows() {
        return expectedRows;
    }

    /** @return what drives the numbers, in one sentence */
    public String basis() {
        return basis;
    }

    @Override
    public String toString() {
        return "CostEstimate{minUnits=" + minUnits + ", expectedUnits=" + expectedUnits + ", maxUnits=" + maxUnits
                + ", expectedRows=" + expectedRows + '}';
    }
}
