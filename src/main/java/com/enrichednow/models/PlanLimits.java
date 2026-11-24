package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** What the account's plan allows of saved formulas. */
public final class PlanLimits {
    private final int schedules;
    private final int schedulesActive;
    private final int minIntervalMinutes;
    private final int rowRetentionDays;
    private final int destinations;

    @JsonCreator
    PlanLimits(@JsonProperty("schedules") int schedules,
               @JsonProperty("schedules_active") int schedulesActive,
               @JsonProperty("min_interval_minutes") int minIntervalMinutes,
               @JsonProperty("row_retention_days") int rowRetentionDays,
               @JsonProperty("destinations") int destinations) {
        this.schedules = schedules;
        this.schedulesActive = schedulesActive;
        this.minIntervalMinutes = minIntervalMinutes;
        this.rowRetentionDays = rowRetentionDays;
        this.destinations = destinations;
    }

    /** @return how many saved formulas may have an active schedule at once */
    public int schedules() {
        return schedules;
    }

    /** @return how many have one now */
    public int schedulesActive() {
        return schedulesActive;
    }

    /** @return the shortest interval between scheduled runs, in minutes: 60 is hourly, 1440 daily */
    public int minIntervalMinutes() {
        return minIntervalMinutes;
    }

    /** @return how many days rows are kept */
    public int rowRetentionDays() {
        return rowRetentionDays;
    }

    /** @return how many destinations one saved formula may have */
    public int destinations() {
        return destinations;
    }

    @Override
    public String toString() {
        return "PlanLimits{schedules=" + schedules + ", schedulesActive=" + schedulesActive + ", minIntervalMinutes=" + minIntervalMinutes
                + ", rowRetentionDays=" + rowRetentionDays + ", destinations=" + destinations + '}';
    }
}
