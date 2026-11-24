package com.enrichednow;

import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import com.enrichednow.models.Run;
import com.fasterxml.jackson.databind.JavaType;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Map;

/** Runs and their records. Reached through {@link EnrichedNowClient#runs()}. */
public final class RunService {
    private final Transport transport;
    private final Requests requests;

    RunService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * One run, as it is now: {@code GET /v1/runs/{id}}.
     *
     * @param runId the run's id
     * @return the run
     * @throws com.enrichednow.errors.NotFoundException when there is no such run in the account
     */
    public Run get(String runId) {
        return transport.execute(requests.run(runId));
    }

    /**
     * One run, when it has ended or once {@code wait} has passed: {@code GET /v1/runs/{id}?wait=}. The API holds the
     * request open in the meantime, so one call does what a few polls would.
     *
     * @param runId the run's id
     * @param wait  from 0 to 300 seconds
     * @return the run
     */
    public Run get(String runId, Duration wait) {
        return transport.execute(requests.run(runId, wait));
    }

    /**
     * One page of a run's records written by the API, as CSV or NDJSON: {@code GET /v1/runs/{id}/records?format=}.
     *
     * <p>The CSV has a column for each field of the scraper's record schema (a nested object becomes
     * {@code parent.child} columns, a list one cell of JSON, a cell a spreadsheet would run as a formula starts with an
     * apostrophe), and every page begins with the header row. {@link RecordsExport#nextCursor()} continues;
     * {@link #exportAll} follows it for you.
     *
     * @param runId  the run's id
     * @param format CSV or NDJSON
     * @param limit  records a page, 1 to 1000; null for the API's 100
     * @param cursor the {@code nextCursor} of the page before, or null
     * @return the page
     */
    public RecordsExport exportPage(String runId, RecordFormat format, Integer limit, String cursor) {
        return transport.execute(requests.export(runId, format, limit, cursor));
    }

    /**
     * Writes every record of a run to {@code out}, as CSV or NDJSON, 1000 a page: the result is one file (a CSV has
     * its header row once). The stream is not closed.
     *
     * @param runId  the run's id
     * @param format CSV or NDJSON
     * @param out    where to write
     * @throws com.enrichednow.errors.EnrichedNowException when the records are gone (kept 7 days after a run ends)
     */
    public void exportAll(String runId, RecordFormat format, OutputStream out) {
        Exports.writeAll(format, out, cursor -> exportPage(runId, format, 1000, cursor),
                "The records of run " + runId + " have expired: they are kept 7 days after a run ends.");
    }

    /**
     * A file the run produced (a screenshot, say), read with this client's key: {@code GET /v1/runs/{id}/files/{name}}.
     * A record also carries the file's own address, which works without a key for an hour.
     *
     * @param runId the run's id
     * @param name  the file's name, e.g. {@code screenshot.png}
     * @return the file's bytes
     */
    public byte[] file(String runId, String name) {
        return transport.execute(requests.file(runId, name));
    }

    /**
     * The account's runs, newest first: {@code GET /v1/runs}.
     *
     * @return every run, fetched a page at a time as it is iterated
     */
    public PagedIterable<Run> list() {
        return list(RunListParams.none());
    }

    /**
     * The account's runs that match, newest first: {@code GET /v1/runs}.
     *
     * @param params the filter and the page size
     * @return the runs, fetched a page at a time as they are iterated
     */
    public PagedIterable<Run> list(RunListParams params) {
        return new PagedIterable<>(() -> listPage(params));
    }

    /**
     * One page of the account's runs.
     *
     * @param params the filter, the page size and, to continue an earlier list, the cursor
     * @return the page
     */
    public Page<Run> listPage(RunListParams params) {
        return transport.execute(requests.runs(params, null));
    }

    /**
     * Asks a run to stop: {@code POST /v1/runs/{id}/cancel}.
     *
     * <p>A queued run ends {@code cancelled} at once. A running one is asked to stop and usually still says
     * {@code running} in the answer; one that was already finishing may still complete. A run that has ended is
     * returned unchanged. {@link #waitFor(String)} shows how it ended.
     *
     * @param runId the run's id
     * @return the run, as it is at that moment
     */
    public Run cancel(String runId) {
        return transport.execute(requests.cancelRun(runId));
    }

    /**
     * Waits for a run to end, asking every 2 seconds for up to 10 minutes.
     *
     * @param runId the run's id
     * @return the run, with a final status: read it, a run can end {@code failed}
     * @throws RunTimeoutException when the run had not ended in time; it carries on
     */
    public Run waitFor(String runId) {
        return waitFor(runId, WaitOptions.defaults());
    }

    /**
     * Waits for a run to end.
     *
     * @param runId   the run's id
     * @param options how long to wait and how often to ask
     * @return the run, with a final status: read it, a run can end {@code failed}
     * @throws RunTimeoutException when the run had not ended in time; it carries on
     */
    public Run waitFor(String runId, WaitOptions options) {
        WaitOptions wait = options == null ? WaitOptions.defaults() : options;
        long deadline = System.nanoTime() + wait.timeout().toNanos();
        return awaitEnd(get(runId), deadline, wait.pollInterval());
    }

    Run awaitEnd(Run run, long deadlineNanos, Duration pollInterval) {
        while (!run.isFinished()) {
            long remaining = deadlineNanos - System.nanoTime();
            if (remaining <= 0) {
                throw new RunTimeoutException(run);
            }
            Transport.sleep(Duration.ofNanos(Math.min(remaining, pollInterval.toNanos())));
            run = get(run.id());
        }
        return run;
    }

    /**
     * A run's records, in the order the run stored them: {@code GET /v1/runs/{id}/records}.
     *
     * <p>Each record is a {@code Map} of the JSON object the scraper's {@code recordSchema} describes. Records exist
     * for runs that ended {@code ok} or {@code partial}, and for a cancelled run that had found some. They are kept
     * for 7 days after the run ended.
     *
     * @param runId the run's id
     * @return the records, fetched a page at a time as they are iterated
     */
    public PagedIterable<Map<String, Object>> records(String runId) {
        return records(runId, RecordListParams.none());
    }

    /**
     * A run's records, with a page size of your choosing.
     *
     * @param runId  the run's id
     * @param params the page size (1 to 1000) and, to continue an earlier list, the cursor
     * @return the records, fetched a page at a time as they are iterated
     */
    public PagedIterable<Map<String, Object>> records(String runId, RecordListParams params) {
        return new PagedIterable<>(() -> recordsPage(runId, params));
    }

    /**
     * A run's records, each read as your own class.
     *
     * <pre>{@code
     * for (Job job : client.runs().records(run.id(), Job.class)) { ... }
     * }</pre>
     *
     * <p>The class is read with Jackson; a field of the record your class does not have is ignored. Pass
     * {@code JsonNode.class} for a JSON tree.
     *
     * @param runId the run's id
     * @param type  the class of one record
     * @param <T>   the class of one record
     * @return the records, fetched a page at a time as they are iterated
     */
    public <T> PagedIterable<T> records(String runId, Class<T> type) {
        return records(runId, RecordListParams.none(), type);
    }

    /**
     * A run's records, each read as your own class, with a page size of your choosing.
     *
     * @param runId  the run's id
     * @param params the page size (1 to 1000) and, to continue an earlier list, the cursor
     * @param type   the class of one record
     * @param <T>    the class of one record
     * @return the records, fetched a page at a time as they are iterated
     */
    public <T> PagedIterable<T> records(String runId, RecordListParams params, Class<T> type) {
        JavaType javaType = requests.type(type);
        return new PagedIterable<>(() -> transport.execute(requests.<T>records(runId, params, javaType, null)));
    }

    /**
     * One page of a run's records.
     *
     * @param runId  the run's id
     * @param params the page size and, to continue an earlier list, the cursor
     * @return the page; {@link RecordPage#expired()} says whether the records are gone
     */
    public RecordPage<Map<String, Object>> recordsPage(String runId, RecordListParams params) {
        return transport.execute(requests.<Map<String, Object>>records(runId, params, requests.mapType(), null));
    }

    /**
     * One page of a run's records, each read as your own class.
     *
     * @param runId  the run's id
     * @param params the page size and, to continue an earlier list, the cursor
     * @param type   the class of one record
     * @param <T>    the class of one record
     * @return the page
     */
    public <T> RecordPage<T> recordsPage(String runId, RecordListParams params, Class<T> type) {
        return transport.execute(requests.<T>records(runId, params, requests.type(type), null));
    }
}
