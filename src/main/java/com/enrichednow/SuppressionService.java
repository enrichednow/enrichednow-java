package com.enrichednow;

import com.enrichednow.models.Suppression;
import com.enrichednow.models.SuppressionUpload;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * The account's suppression list: domains, email addresses and company names that formula runs leave out, before any
 * paid step, before rows are written and when rows are sent. Reached through {@link EnrichedNowClient#suppressions()}.
 */
public final class SuppressionService {
    private final Transport transport;
    private final Requests requests;

    SuppressionService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * The whole list: {@code GET /v1/suppressions}.
     *
     * @return every entry, fetched a page at a time as it is iterated
     */
    public PagedIterable<Suppression> list() {
        return list(SuppressionListParams.none());
    }

    /**
     * The entries that match.
     *
     * @param params the kind and the page size
     * @return the entries, fetched a page at a time as they are iterated
     */
    public PagedIterable<Suppression> list(SuppressionListParams params) {
        return new PagedIterable<>(() -> listPage(params));
    }

    /**
     * One page of the list.
     *
     * @param params the kind, the page size and, to continue an earlier list, the cursor
     * @return the page
     */
    public Page<Suppression> listPage(SuppressionListParams params) {
        return transport.execute(requests.suppressions(params, null));
    }

    /**
     * Adds entries to the list: {@code POST /v1/suppressions}. An entry the list already holds is left as it is, so
     * this is safe to send again.
     *
     * @param entries 1 to 10,000 entries; for more, {@link #upload(Path)} a CSV file
     * @return how many were new to the list
     */
    public int add(List<SuppressionEntry> entries) {
        return add(entries, null);
    }

    /**
     * Adds entries to the list.
     *
     * @param entries 1 to 10,000 entries
     * @return how many were new to the list
     */
    public int add(SuppressionEntry... entries) {
        return add(Arrays.asList(entries), null);
    }

    /**
     * Adds entries to the list, saying where they came from.
     *
     * @param entries 1 to 10,000 entries
     * @param source  {@code manual} (the default) or {@code upload}; null for the default
     * @return how many were new to the list
     */
    public int add(List<SuppressionEntry> entries, String source) {
        return transport.execute(requests.addSuppressions(entries, source));
    }

    /**
     * Adds a CSV file to the list: {@code POST /v1/suppressions/upload}, at most 2 MB and 50,000 lines.
     *
     * <p>With a header row holding {@code kind} and {@code value}, each line says its kind. Otherwise every value of
     * the first column is read as an address when it holds {@code @}, a domain when it looks like one, and a company
     * name otherwise. Lines that cannot be read are listed in {@link SuppressionUpload#invalid()}; the rest are added.
     * An entry the list already holds is left as it is, so a repeat is safe (it counts as {@code already}).
     *
     * @param csv the file, UTF-8
     * @return what was added, what was there already, and the lines that could not be read
     */
    public SuppressionUpload upload(byte[] csv) {
        return upload(csv, null);
    }

    /**
     * Adds a CSV file to the list, every value of its first column taken as one kind (unless a header row names a
     * {@code kind} column).
     *
     * @param csv  the file, UTF-8
     * @param kind what each value is; null to read each as it looks
     * @return what was added, what was there already, and the lines that could not be read
     */
    public SuppressionUpload upload(byte[] csv, SuppressionKind kind) {
        return transport.execute(requests.uploadSuppressions(csv, kind));
    }

    /**
     * Adds CSV text to the list.
     *
     * @param csv the file's text
     * @return what was added, what was there already, and the lines that could not be read
     * @see #upload(byte[])
     */
    public SuppressionUpload upload(String csv) {
        return upload(csv, null);
    }

    /**
     * Adds CSV text to the list, every value taken as one kind.
     *
     * @param csv  the file's text
     * @param kind what each value is; null to read each as it looks
     * @return what was added, what was there already, and the lines that could not be read
     */
    public SuppressionUpload upload(String csv, SuppressionKind kind) {
        return upload(csv == null ? null : csv.getBytes(StandardCharsets.UTF_8), kind);
    }

    /**
     * Adds a CSV file to the list.
     *
     * @param file the file, UTF-8
     * @return what was added, what was there already, and the lines that could not be read
     * @throws UncheckedIOException when the file cannot be read
     * @see #upload(byte[])
     */
    public SuppressionUpload upload(Path file) {
        return upload(file, null);
    }

    /**
     * Adds a CSV file to the list, every value taken as one kind.
     *
     * @param file the file, UTF-8
     * @param kind what each value is; null to read each as it looks
     * @return what was added, what was there already, and the lines that could not be read
     * @throws UncheckedIOException when the file cannot be read
     */
    public SuppressionUpload upload(Path file, SuppressionKind kind) {
        try {
            return upload(Files.readAllBytes(file), kind);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Adds a CSV file to the list, read from a stream to its end. The stream is not closed.
     *
     * @param csv the file, UTF-8
     * @return what was added, what was there already, and the lines that could not be read
     * @throws UncheckedIOException when the stream cannot be read
     * @see #upload(byte[])
     */
    public SuppressionUpload upload(InputStream csv) {
        return upload(csv, null);
    }

    /**
     * Adds a CSV file to the list, read from a stream to its end, every value taken as one kind. The stream is not
     * closed.
     *
     * @param csv  the file, UTF-8
     * @param kind what each value is; null to read each as it looks
     * @return what was added, what was there already, and the lines that could not be read
     * @throws UncheckedIOException when the stream cannot be read
     */
    public SuppressionUpload upload(InputStream csv, SuppressionKind kind) {
        try {
            return upload(csv.readAllBytes(), kind);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Removes an entry from the list: {@code DELETE /v1/suppressions/{kind}/{value}}.
     *
     * @param kind  what the value is
     * @param value the value, in any form the list matches (it is read as {@link #add} reads it)
     * @throws com.enrichednow.errors.NotFoundException when the list does not hold it
     */
    public void delete(SuppressionKind kind, String value) {
        transport.execute(requests.deleteSuppression(kind, value));
    }
}
