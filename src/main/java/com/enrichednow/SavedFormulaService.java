package com.enrichednow;

import com.enrichednow.formulas.FormulaParams;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.PlanLimits;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import com.enrichednow.models.SavedFormula;
import com.enrichednow.models.SeenReset;
import com.enrichednow.models.Snapshot;
import java.io.OutputStream;
import java.util.List;

/**
 * Saved formulas: a formula kept with its parameters and caps, run by hand or on a schedule. Each run delivers only rows
 * it has not delivered before, so polling {@link #rows(String, FormulaRowListParams)} with {@code since} gives what is
 * new. Reached through {@link EnrichedNowClient#savedFormulas()}.
 *
 * <pre>{@code
 * SavedFormula saved = client.savedFormulas().create(SavedFormulaCreate.builder(params)
 *         .schedule(Schedule.weekdays("07:30", "Europe/London"))
 *         .build());
 * for (FormulaRow row : client.savedFormulas().rows(saved.id(), FormulaRowListParams.builder().since(lastPoll).build())) { ... }
 * }</pre>
 */
public final class SavedFormulaService {
    private final Transport transport;
    private final Requests requests;

    SavedFormulaService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * Saves a formula: {@code POST /v1/saved-formulas}. It is not sent again after a failure that may have reached the
     * API, so that one call never saves two.
     *
     * @param create the formula, its parameters, and what else to set
     * @return the saved formula
     * @throws com.enrichednow.errors.PlanLimitReachedException    when the plan does not allow the schedule
     * @throws com.enrichednow.errors.AttestationRequiredException when a schedule is given for a formula that returns
     *                                                             people and the terms are not accepted
     */
    public SavedFormula create(SavedFormulaCreate create) {
        return transport.execute(requests.createSavedFormula(create));
    }

    /**
     * Saves a formula with its parameters and nothing else: no schedule, the formula's caps.
     *
     * @param formulaId the formula's id
     * @param params    its parameters: a {@code Map}, a {@link FormulaParams} of that formula, or any object written as
     *                  the JSON object its {@code parameters()} describe
     * @return the saved formula
     */
    public SavedFormula create(String formulaId, Object params) {
        return create(SavedFormulaCreate.builder(formulaId, params).build());
    }

    /**
     * Saves the formula these parameters are for, with nothing else: no schedule, the formula's caps.
     *
     * @param params the parameters of one formula
     * @return the saved formula
     */
    public SavedFormula create(FormulaParams params) {
        return create(SavedFormulaCreate.builder(params).build());
    }

    /**
     * The account's saved formulas that are not archived, newest first: {@code GET /v1/saved-formulas}.
     *
     * @return every one, fetched a page at a time as they are iterated
     */
    public PagedIterable<SavedFormula> list() {
        return list(SavedFormulaListParams.none());
    }

    /**
     * The account's saved formulas that match, newest first.
     *
     * @param params the formula, the status ({@code ARCHIVED} lists the archived ones) and the page size
     * @return the saved formulas, fetched a page at a time as they are iterated
     */
    public PagedIterable<SavedFormula> list(SavedFormulaListParams params) {
        return new PagedIterable<>(() -> listPage(params));
    }

    /**
     * One page of the account's saved formulas.
     *
     * @param params the filter, the page size and, to continue an earlier list, the cursor
     * @return the page
     */
    public Page<SavedFormula> listPage(SavedFormulaListParams params) {
        return transport.execute(requests.savedFormulas(params, null));
    }

    /**
     * One saved formula: {@code GET /v1/saved-formulas/{id}}.
     *
     * @param savedFormulaId its id, {@code sf_…}
     * @return the saved formula
     */
    public SavedFormula get(String savedFormulaId) {
        return transport.execute(requests.savedFormula(savedFormulaId));
    }

    /**
     * Changes a saved formula: {@code PATCH /v1/saved-formulas/{id}}. Only what the change sets is sent; it can also
     * remove the schedule or the monthly cap, and give the name back to the parameters. Safe to send again.
     *
     * @param savedFormulaId its id
     * @param change         what to change
     * @return the saved formula as it is now
     * @throws IllegalArgumentException                              when the change sets nothing; nothing is sent
     * @throws com.enrichednow.errors.SavedFormulaArchivedException when it is archived
     */
    public SavedFormula update(String savedFormulaId, SavedFormulaChange change) {
        return transport.execute(requests.updateSavedFormula(savedFormulaId, change));
    }

    /**
     * Archives a saved formula: {@code DELETE /v1/saved-formulas/{id}}. Its schedule stops and it does not run again;
     * its runs and rows are kept as long as they would be. It can still be duplicated.
     *
     * @param savedFormulaId its id
     * @return the saved formula, archived
     */
    public SavedFormula archive(String savedFormulaId) {
        return transport.execute(requests.archiveSavedFormula(savedFormulaId));
    }

    /**
     * Makes a copy, named after the original, with nothing delivered and no schedule:
     * {@code POST /v1/saved-formulas/{id}/duplicate}.
     *
     * @param savedFormulaId the original's id; it may be archived
     * @return the copy
     */
    public SavedFormula duplicate(String savedFormulaId) {
        return duplicate(savedFormulaId, DuplicateOptions.none());
    }

    /**
     * Makes a copy: {@code POST /v1/saved-formulas/{id}/duplicate}. It is not sent again after a failure that may have
     * reached the API, so that one call never makes two copies.
     *
     * @param savedFormulaId the original's id
     * @param options        the copy's name, whether it starts with what the original delivered, its schedule
     * @return the copy
     */
    public SavedFormula duplicate(String savedFormulaId, DuplicateOptions options) {
        return transport.execute(requests.duplicateSavedFormula(savedFormulaId, options));
    }

    /**
     * Runs a saved formula now, with its saved parameters and caps, and returns as soon as the run is queued.
     *
     * @param savedFormulaId its id
     * @return the run, usually {@code queued}
     */
    public FormulaRun run(String savedFormulaId) {
        return run(savedFormulaId, SavedFormulaRunOptions.none());
    }

    /**
     * Runs a saved formula now: {@code POST /v1/saved-formulas/{id}/runs}. Its monthly cap lowers the unit cap to what
     * is left of it. A paused saved formula can still be run by hand. The request carries an {@code Idempotency-Key},
     * as starting a formula run does, so it is safe to send again.
     *
     * @param savedFormulaId its id
     * @param options        a preview, rows already delivered too, the wait, the idempotency key
     * @return the run
     * @throws com.enrichednow.errors.LimitReachedException          when its monthly cap is reached ({@code scope()}
     *                                                               {@code saved_formula})
     * @throws com.enrichednow.errors.SavedFormulaArchivedException when it is archived
     */
    public FormulaRun run(String savedFormulaId, SavedFormulaRunOptions options) {
        return transport.execute(requests.startSavedFormulaRun(savedFormulaId, options));
    }

    /**
     * Pauses its schedule: no scheduled run starts until it is resumed. It can still be run by hand. Safe to send again.
     *
     * @param savedFormulaId its id
     * @return the saved formula as it is now
     */
    public SavedFormula pause(String savedFormulaId) {
        return transport.execute(requests.pauseSavedFormula(savedFormulaId));
    }

    /**
     * Resumes its schedule. Safe to send again.
     *
     * @param savedFormulaId its id
     * @return the saved formula as it is now, with its next run
     * @throws com.enrichednow.errors.PlanLimitReachedException when the plan does not allow one more active schedule
     */
    public SavedFormula resume(String savedFormulaId) {
        return transport.execute(requests.resumeSavedFormula(savedFormulaId));
    }

    /**
     * Forgets what a saved formula delivered, so its next run may deliver those rows again:
     * {@code POST /v1/saved-formulas/{id}/seen/reset}. Owners and admins only.
     *
     * @param savedFormulaId its id
     * @return how many rows were forgotten, and the saved formula
     */
    public SeenReset resetSeen(String savedFormulaId) {
        return transport.execute(requests.resetSeen(savedFormulaId));
    }

    /**
     * Every row of a saved formula's runs, in the order they were written: {@code GET /v1/saved-formulas/{id}/rows}.
     * Rows the suppression list now holds are left out.
     *
     * @param savedFormulaId its id
     * @return the rows, fetched a page at a time as they are iterated
     */
    public PagedIterable<FormulaRow> rows(String savedFormulaId) {
        return rows(savedFormulaId, FormulaRowListParams.none());
    }

    /**
     * A saved formula's rows that match: with {@code since}, what is new since the last poll.
     *
     * @param savedFormulaId its id
     * @param params         since, the order, the columns, the least score and the page size
     * @return the rows, fetched a page at a time as they are iterated
     */
    public PagedIterable<FormulaRow> rows(String savedFormulaId, FormulaRowListParams params) {
        return new PagedIterable<>(() -> rowsPage(savedFormulaId, params));
    }

    /**
     * One page of a saved formula's rows.
     *
     * @param savedFormulaId its id
     * @param params         since, the order, the columns, the least score, the page size and the cursor
     * @return the page
     * @throws IllegalArgumentException when {@code params} has a {@code view}: that is for a run's rows
     */
    public Page<FormulaRow> rowsPage(String savedFormulaId, FormulaRowListParams params) {
        return transport.execute(requests.savedFormulaRows(savedFormulaId, params, null));
    }

    /**
     * One page of a saved formula's rows written by the API, as CSV or NDJSON.
     *
     * @param savedFormulaId its id
     * @param format         CSV or NDJSON
     * @param params         since, the order, the columns, the least score, the page size and the cursor
     * @return the page; {@link RecordsExport#nextCursor()} continues
     */
    public RecordsExport exportPage(String savedFormulaId, RecordFormat format, FormulaRowListParams params) {
        return transport.execute(requests.exportSavedFormulaRows(savedFormulaId, format, params, null));
    }

    /**
     * Writes every row of a saved formula to {@code out}, as CSV or NDJSON, 1000 a page: one file (a CSV has its header
     * row once). The stream is not closed.
     *
     * @param savedFormulaId its id
     * @param format         CSV or NDJSON
     * @param out            where to write
     */
    public void exportAll(String savedFormulaId, RecordFormat format, OutputStream out) {
        exportAll(savedFormulaId, format, FormulaRowListParams.none(), out);
    }

    /**
     * Writes every row of a saved formula that matches to {@code out}, as CSV or NDJSON: one file. The stream is not
     * closed.
     *
     * @param savedFormulaId its id
     * @param format         CSV or NDJSON
     * @param params         since, the order, the columns, the least score; the page size (default 1000)
     * @param out            where to write
     */
    public void exportAll(String savedFormulaId, RecordFormat format, FormulaRowListParams params, OutputStream out) {
        FormulaRowListParams paged = (params == null ? FormulaRowListParams.none() : params).orLimit(FormulaRunService.PAGE);
        Exports.writeAll(format, out,
                cursor -> transport.execute(requests.exportSavedFormulaRows(savedFormulaId, format, paged, cursor)), null);
    }

    /**
     * What a saved formula observed, each time an entity's tracked values changed, newest first:
     * {@code GET /v1/saved-formulas/{id}/snapshots}. For a formula that watches things (prices, stock, postings).
     *
     * @param savedFormulaId its id
     * @return the snapshots, up to 200
     */
    public List<Snapshot> snapshots(String savedFormulaId) {
        return snapshots(savedFormulaId, null, null);
    }

    /**
     * What a saved formula observed of one entity, or of all: its history.
     *
     * @param savedFormulaId its id
     * @param entityKey      only this entity, e.g. its address; null for all
     * @param limit          how many, 1 to 1000; null for 200
     * @return the snapshots, newest first
     */
    public List<Snapshot> snapshots(String savedFormulaId, String entityKey, Integer limit) {
        return transport.execute(requests.snapshots(savedFormulaId, entityKey, limit));
    }

    /**
     * What the account's plan allows of saved formulas: {@code GET /v1/saved-formulas/limits}.
     *
     * @return the schedules allowed and active, the shortest interval, how long rows are kept, destinations
     */
    public PlanLimits limits() {
        return transport.execute(requests.savedFormulaLimits());
    }
}
