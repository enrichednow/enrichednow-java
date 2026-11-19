package com.enrichednow.models;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Where the account stands, as the API says it in the headers of every answer about runs ({@code X-Units-*},
 * {@code X-RateLimit-*}). A number the answer did not carry is {@code null}.
 */
public final class Standing {
    private final Long unitsLimit;
    private final Long unitsUsed;
    private final Long unitsRemaining;
    private final Long unitsTopup;
    private final Instant unitsReset;
    private final Long runsLimit;
    private final Long runsRemaining;
    private final boolean unmetered;

    private Standing(Map<String, String> h) {
        this.unitsLimit = number(h.get("x-units-limit"));
        this.unitsUsed = number(h.get("x-units-used"));
        this.unitsRemaining = number(h.get("x-units-remaining"));
        this.unitsTopup = number(h.get("x-units-topup"));
        Instant reset;
        try {
            reset = Values.instant(h.get("x-units-reset"));
        } catch (RuntimeException notATime) {
            reset = null;
        }
        this.unitsReset = reset;
        this.runsLimit = number(h.get("x-ratelimit-limit"));
        this.runsRemaining = number(h.get("x-ratelimit-remaining"));
        this.unmetered = "true".equals(h.get("x-units-unmetered"));
    }

    /**
     * The standing an answer's headers carry.
     *
     * @param headers the answer's headers, by name (any case)
     * @return the standing, or {@code null} when the answer carries none (an answer not about runs)
     */
    public static Standing fromHeaders(Map<String, List<String>> headers) {
        Map<String, String> lower = new TreeMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
                lower.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue().get(0));
            }
        }
        if (!lower.containsKey("x-units-limit") && !lower.containsKey("x-ratelimit-limit")) {
            return null;
        }
        return new Standing(lower);
    }

    private static Long number(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** @return the month's allowance of units; for an unmetered account, the largest number the API's integers hold */
    public Long unitsLimit() {
        return unitsLimit;
    }

    /** @return units of the month's allowance used so far */
    public Long unitsUsed() {
        return unitsUsed;
    }

    /** @return units of the month's allowance left; top-up units are counted apart */
    public Long unitsRemaining() {
        return unitsRemaining;
    }

    /** @return top-up units left: spent once the month's allowance is used up; they do not expire */
    public Long unitsTopup() {
        return unitsTopup;
    }

    /** @return when the allowance starts again: the first of next month, UTC */
    public Instant unitsReset() {
        return unitsReset;
    }

    /** @return how many runs the account may have in progress at the same time */
    public Long runsLimit() {
        return runsLimit;
    }

    /** @return how many more runs can be started now */
    public Long runsRemaining() {
        return runsRemaining;
    }

    /** @return true for an account with no unit limit */
    public boolean unmetered() {
        return unmetered;
    }

    /** @return units that can still be spent: what is left of the allowance and the top-up units; null when not said */
    public Long unitsAvailable() {
        if (unitsRemaining == null) {
            return null;
        }
        return unitsRemaining + (unitsTopup == null ? 0 : unitsTopup);
    }

    @Override
    public String toString() {
        return "Standing{unitsLimit=" + unitsLimit + ", unitsUsed=" + unitsUsed + ", unitsRemaining=" + unitsRemaining
                + ", unitsTopup=" + unitsTopup + ", unitsReset=" + unitsReset + ", runsLimit=" + runsLimit
                + ", runsRemaining=" + runsRemaining + ", unmetered=" + unmetered + '}';
    }
}
