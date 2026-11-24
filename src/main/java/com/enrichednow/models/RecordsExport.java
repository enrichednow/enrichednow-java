package com.enrichednow.models;

import java.nio.charset.StandardCharsets;

/** One page of a run's records, or of a formula's rows, as the API wrote it, as CSV or NDJSON. */
public final class RecordsExport {
    private final RecordFormat format;
    private final byte[] body;
    private final String nextCursor;
    private final boolean expired;

    /**
     * @param format     how the page is written
     * @param body       the page's bytes
     * @param nextCursor where the next page starts, or null on the last page
     * @param expired    whether the records are gone
     */
    public RecordsExport(RecordFormat format, byte[] body, String nextCursor, boolean expired) {
        this.format = format;
        this.body = body == null ? new byte[0] : body.clone();
        this.nextCursor = nextCursor == null || nextCursor.isEmpty() ? null : nextCursor;
        this.expired = expired;
    }

    /** @return how the page is written */
    public RecordFormat format() {
        return format;
    }

    /** @return the page's bytes, UTF-8 text */
    public byte[] body() {
        return body.clone();
    }

    /** @return the page as text */
    public String text() {
        return new String(body, StandardCharsets.UTF_8);
    }

    /** @return where the next page starts (the {@code X-Next-Cursor} header), or null on the last page */
    public String nextCursor() {
        return nextCursor;
    }

    /** @return true when a scraper run's records are gone: they are kept 7 days after it ends. Always false for rows */
    public boolean expired() {
        return expired;
    }

    @Override
    public String toString() {
        return "RecordsExport{format=" + format + ", bytes=" + body.length + ", nextCursor=" + nextCursor + ", expired=" + expired + '}';
    }
}
