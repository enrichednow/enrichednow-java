package com.enrichednow.models;

/**
 * The status of a run. {@code queued} and {@code running} are in progress; the other five are final.
 */
public enum RunStatus {
    /** Accepted, not started yet. */
    QUEUED("queued"),
    /** Working. */
    RUNNING("running"),
    /** Ended with its result. Charged. */
    OK("ok"),
    /** Ended with part of its result; {@code reason} says what is missing. Charged. */
    PARTIAL("partial"),
    /** Ended without a result; {@code reason} says why. Not charged. */
    FAILED("failed"),
    /** The scraper cannot read this target; {@code reason} says why. Not charged. */
    UNSUPPORTED("unsupported"),
    /** Stopped on request. Not charged. */
    CANCELLED("cancelled"),
    /** A status this version of the SDK does not know; {@link Run#rawStatus()} has the API's own word. */
    UNKNOWN("unknown");

    private final String value;

    RunStatus(String value) {
        this.value = value;
    }

    /** @return the status as the API writes it */
    public String value() {
        return value;
    }

    /** @return whether the run has ended: any status but {@code queued}, {@code running} and an unknown one */
    public boolean isFinal() {
        return this != QUEUED && this != RUNNING && this != UNKNOWN;
    }

    /** @return whether the run ended with records to read: {@code ok} or {@code partial} */
    public boolean isSuccessful() {
        return this == OK || this == PARTIAL;
    }

    /**
     * @param value the status as the API writes it
     * @return the status, or {@link #UNKNOWN} for a word this SDK does not know
     */
    public static RunStatus fromValue(String value) {
        for (RunStatus status : values()) {
            if (status != UNKNOWN && status.value.equals(value)) {
                return status;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return value;
    }
}
