package com.enrichednow;

import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import java.io.OutputStream;
import java.time.Duration;

/** Formula runs and their rows. Reached through {@link EnrichedNowClient#formulaRuns()}. */
public final class FormulaRunService {
    /** The longest one look at a run asks the API to hold the request. */
    static final Duration LOOK_WAIT = Duration.ofSeconds(60);
    /** Rows a page when reading them all. */
    static final int PAGE = 1000;

    private final Transport transport;
    private final Requests requests;

    FormulaRunService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * The account's formula runs, newest first: {@code GET /v1/formula-runs}.
     *
     * @return every run, fetched a page at a time as it is iterated
     */
    public PagedIterable<FormulaRun> list() {
        return list(FormulaRunListParams.none());
    }

    /**
     * The account's formula runs that match, newest first: {@code GET /v1/formula-runs}.
     *
     * @param params the filter and the page size
     * @return the runs, fetched a page at a time as they are iterated
     */
    public PagedIterable<FormulaRun> list(FormulaRunListParams params) {
        return new PagedIterable<>(() -> listPage(params));
    }

    /**
     * One page of the account's formula runs. A run in a list has no {@code steps()}: {@link #get(String)} has them.
     *
     * @param params the filter, the page size and, to continue an earlier list, the cursor
     * @return the page
     */
    public Page<FormulaRun> listPage(FormulaRunListParams params) {
        return transport.execute(requests.formulaRuns(params, null));
    }

    /**
     * One formula run, as it is now, with what each step did: {@code GET /v1/formula-runs/{id}}.
     *
     * @param runId the run's id, {@code frun_…}
     * @return the run
     * @throws com.enrichednow.errors.NotFoundException when there is no such run in the account
     */
    public FormulaRun get(String runId) {
        return transport.execute(requests.formulaRun(runId));
    }

    /**
     * One formula run, when it has ended or once {@code wait} has passed: {@code GET /v1/formula-runs/{id}?wait=}. The
     * API holds the request open in the meantime and answers the moment the run ends.
     *
     * @param runId the run's id
     * @param wait  from 0 to 300 seconds
     * @return the run
     */
    public FormulaRun get(String runId, Duration wait) {
        return transport.execute(requests.formulaRun(runId, wait));
    }

    /**
     * Waits for a formula run to end, for up to 10 minutes.
     *
     * @param runId the run's id
     * @return the run, with a final status: read it, a run can end {@code failed}
     * @throws RunTimeoutException when the run had not ended in time; it carries on
     */
    public FormulaRun waitFor(String runId) {
        return waitFor(runId, WaitOptions.defaults());
    }

    /**
     * Waits for a formula run to end, however it ends.
     *
     * <p>Each look asks the API to hold the request until the run ends (up to 60 seconds, never past the timeout), so
     * the run is seen the moment it ends. An answer that comes back sooner than {@link WaitOptions#pollInterval()} is
     * followed by a pause for the rest of it.
     *
     * @param runId   the run's id
     * @param options how long to wait, and the least time between two looks
     * @return the run, with a final status
     * @throws RunTimeoutException when the run had not ended in time ({@link RunTimeoutException#formulaRun()}); it
     *                             carries on
     */
    public FormulaRun waitFor(String runId, WaitOptions options) {
        WaitOptions wait = options == null ? WaitOptions.defaults() : options;
        return awaitEnd(runId, null, System.nanoTime() + wait.timeout().toNanos(), wait.pollInterval());
    }

    /** Looks at the run until it has ended or the deadline has passed; {@code known} is the run as last seen, or null. */
    FormulaRun awaitEnd(String runId, FormulaRun known, long deadlineNanos, Duration pollInterval) {
        FormulaRun run = known;
        while (run == null || !run.isFinished()) {
            long remaining = deadlineNanos - System.nanoTime();
            if (run != null && remaining <= 0) {
                throw new RunTimeoutException(run);
            }
            long asked = System.nanoTime();
            run = get(runId, lookWait(remaining));
            if (run.isFinished()) {
                break;
            }
            long left = deadlineNanos - System.nanoTime();
            long rest = pollInterval.toNanos() - (System.nanoTime() - asked);
            if (rest > 0 && left > 0) {
                Transport.sleep(Duration.ofNanos(Math.min(rest, left)));
            }
        }
        return run;
    }

    /** How long one look asks the API to hold the request: what is left of the wait, at most a minute. */
    static Duration lookWait(long remainingNanos) {
        return Duration.ofNanos(Math.max(0, Math.min(remainingNanos, LOOK_WAIT.toNanos())));
    }

    /**
     * Asks a formula run to stop: {@code POST /v1/formula-runs/{id}/cancel}. No new work starts and calls in flight are
     * asked to stop; rows already written stay, and what was done stays charged. The answer usually still says
     * {@code running}: {@link #waitFor(String)} shows how it ended. A run that has ended is returned unchanged.
     *
     * @param runId the run's id
     * @return the run, as it is at that moment
     */
    public FormulaRun cancel(String runId) {
        return transport.execute(requests.cancelFormulaRun(runId));
    }

    /**
     * A run's rows, best first: {@code GET /v1/formula-runs/{id}/rows}. They can be read while the run is going, and are
     * kept 90 days.
     *
     * @param runId the run's id
     * @return the rows, fetched a page at a time as they are iterated
     */
    public PagedIterable<FormulaRow> rows(String runId) {
        return rows(runId, FormulaRowListParams.none());
    }

    /**
     * A run's rows that match, with only the columns asked for, or one row per company.
     *
     * @param runId  the run's id
     * @param params the columns, the least score, the view and the page size (1 to 1000)
     * @return the rows, fetched a page at a time as they are iterated
     */
    public PagedIterable<FormulaRow> rows(String runId, FormulaRowListParams params) {
        return new PagedIterable<>(() -> rowsPage(runId, params));
    }

    /**
     * One page of a run's rows.
     *
     * @param runId  the run's id
     * @param params the columns, the least score, the view, the page size and, to continue, the cursor
     * @return the page
     * @throws IllegalArgumentException when {@code params} has {@code since} or {@code order}: those are for a saved
     *                                  formula's rows
     */
    public Page<FormulaRow> rowsPage(String runId, FormulaRowListParams params) {
        return transport.execute(requests.formulaRows(runId, params, null));
    }

    /**
     * One page of a run's rows written by the API, as CSV or NDJSON: {@code GET /v1/formula-runs/{id}/rows?format=}.
     *
     * <p>The CSV has a column for each of the formula's columns (or those of {@code fields}), and every page begins
     * with the header row; a cell a spreadsheet would run as a formula starts with an apostrophe.
     * {@link RecordsExport#nextCursor()} continues; {@link #exportAll} follows it for you.
     *
     * @param runId  the run's id
     * @param format CSV or NDJSON
     * @param params the columns, the least score, the view, the page size and the cursor
     * @return the page
     */
    public RecordsExport exportPage(String runId, RecordFormat format, FormulaRowListParams params) {
        return transport.execute(requests.exportFormulaRows(runId, format, params, null));
    }

    /**
     * Writes every row of a run to {@code out}, as CSV or NDJSON, 1000 a page: the result is one file (a CSV has its
     * header row once). The stream is not closed.
     *
     * @param runId  the run's id
     * @param format CSV or NDJSON
     * @param out    where to write
     */
    public void exportAll(String runId, RecordFormat format, OutputStream out) {
        exportAll(runId, format, FormulaRowListParams.none(), out);
    }

    /**
     * Writes every row of a run that matches to {@code out}, as CSV or NDJSON: one file. The stream is not closed.
     *
     * @param runId  the run's id
     * @param format CSV or NDJSON
     * @param params the columns, the least score and the view; the page size (default 1000) and where to start
     * @param out    where to write
     */
    public void exportAll(String runId, RecordFormat format, FormulaRowListParams params, OutputStream out) {
        FormulaRowListParams paged = (params == null ? FormulaRowListParams.none() : params).orLimit(PAGE);
        Exports.writeAll(format, out, cursor -> transport.execute(requests.exportFormulaRows(runId, format, paged, cursor)), null);
    }
}
