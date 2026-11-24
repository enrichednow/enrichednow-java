package com.enrichednow;

import com.enrichednow.errors.EnrichedNowException;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.function.Function;

/** Writes the pages of an export, one after the other, as one file: a CSV's header row once. */
final class Exports {
    private Exports() {
    }

    /**
     * @param format  CSV or NDJSON
     * @param out     where to write; not closed
     * @param page    fetches the page that starts at a cursor (null: the first)
     * @param expired what to say when a page answers that the records are gone, or null when they cannot be
     */
    static void writeAll(RecordFormat format, OutputStream out, Function<String, RecordsExport> page, String expired) {
        String cursor = null;
        boolean first = true;
        try {
            while (true) {
                RecordsExport current = page.apply(cursor);
                if (expired != null && current.expired()) {
                    throw new EnrichedNowException(expired);
                }
                byte[] body = current.body();
                int start = first || format != RecordFormat.CSV ? 0 : afterFirstLine(body);
                out.write(body, start, body.length - start);
                first = false;
                if (current.nextCursor() == null) {
                    return;
                }
                cursor = current.nextCursor();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Where the second line starts: past the header row a page of CSV begins with. */
    private static int afterFirstLine(byte[] body) {
        for (int i = 0; i < body.length; i++) {
            if (body[i] == '\n') {
                return i + 1;
            }
        }
        return body.length;
    }
}
