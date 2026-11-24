package com.enrichednow;

/** When mail is sent about a saved formula's scheduled runs that delivered new rows. */
public enum Notify {
    /** No mail. */
    NONE("none"),
    /** A mail after each scheduled run that delivered new rows. The default. */
    EACH_RUN("each_run"),
    /** One mail a day about them all. */
    DIGEST("digest");

    private final String value;

    Notify(String value) {
        this.value = value;
    }

    /** @return the value as the API writes it */
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
