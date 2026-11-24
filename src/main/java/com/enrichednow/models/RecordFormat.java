package com.enrichednow.models;

/** How the API writes a run's records, or a formula's rows, for an export. */
public enum RecordFormat {
    /**
     * A header row, then a line a record or row: a column for each field of the scraper's record schema, or for each
     * column of the formula.
     */
    CSV("csv", "text/csv"),
    /** One JSON object a line: a record, or a row's columns. */
    NDJSON("ndjson", "application/x-ndjson");

    private final String value;
    private final String mediaType;

    RecordFormat(String value, String mediaType) {
        this.value = value;
        this.mediaType = mediaType;
    }

    /** @return the format as the API names it */
    public String value() {
        return value;
    }

    /** @return the media type the API answers with */
    public String mediaType() {
        return mediaType;
    }
}
