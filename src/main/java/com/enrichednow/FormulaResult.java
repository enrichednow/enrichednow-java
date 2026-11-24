package com.enrichednow;

import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.RecordFormat;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * What {@link EnrichedNowClient#runFormula} returns: the run as it ended, and its rows. Iterating it walks every row,
 * best first, 1000 a page, fetched as they are reached.
 *
 * <pre>{@code
 * for (FormulaRow row : result) {
 *     System.out.println(row.get("company_name") + " " + row.get("contact_email"));
 * }
 * }</pre>
 */
public final class FormulaResult implements Iterable<FormulaRow> {
    private final FormulaRun run;
    private final FormulaRunService runs;

    FormulaResult(FormulaRun run, FormulaRunService runs) {
        this.run = run;
        this.runs = runs;
    }

    /** @return the run, with its final status, reason, rows and units */
    public FormulaRun run() {
        return run;
    }

    /**
     * The run's rows, best first. They are fetched a page at a time as they are iterated, not when the result is made.
     *
     * @return the rows
     */
    public PagedIterable<FormulaRow> rows() {
        return rows(FormulaRowListParams.none());
    }

    /**
     * The run's rows that match, with only the columns asked for, or one row per company.
     *
     * @param params the columns, the least score, the view; the page size (default 1000)
     * @return the rows
     */
    public PagedIterable<FormulaRow> rows(FormulaRowListParams params) {
        return runs.rows(run.id(), (params == null ? FormulaRowListParams.none() : params).orLimit(FormulaRunService.PAGE));
    }

    /** @return every row, fetched a page at a time as it is reached */
    @Override
    public Iterator<FormulaRow> iterator() {
        return rows().iterator();
    }

    /**
     * Writes every row of the run to {@code out}, as CSV or NDJSON, as one file (a CSV has its header row once). The
     * stream is not closed.
     *
     * @param format CSV or NDJSON
     * @param out    where to write
     */
    public void exportAll(RecordFormat format, OutputStream out) {
        runs.exportAll(run.id(), format, out);
    }

    /**
     * Writes every row of the run that matches to {@code out}, as CSV or NDJSON, as one file. The stream is not closed.
     *
     * @param format CSV or NDJSON
     * @param params the columns, the least score, the view; the page size (default 1000)
     * @param out    where to write
     */
    public void exportAll(RecordFormat format, FormulaRowListParams params, OutputStream out) {
        runs.exportAll(run.id(), format, params, out);
    }

    @Override
    public String toString() {
        return "FormulaResult{run=" + run + '}';
    }
}
