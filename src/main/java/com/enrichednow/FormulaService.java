package com.enrichednow;

import com.enrichednow.formulas.FormulaParams;
import com.enrichednow.models.Formula;
import com.enrichednow.models.FormulaDetails;
import com.enrichednow.models.FormulaEstimate;
import com.enrichednow.models.FormulaRun;
import java.util.List;

/**
 * The catalogue of formulas, what a run would cost, and starting one. Reached through
 * {@link EnrichedNowClient#formulas()}.
 *
 * <p>A formula is a ready-made run: answer a few parameters and get a finished list of rows. Parameters are a
 * {@code Map}, or the typed class of the formula in {@link com.enrichednow.formulas} (one each, with a builder):
 *
 * <pre>{@code
 * JobOrderHunterParams params = JobOrderHunterParams.builder().jobTitles("Java Developer").locations("London").build();
 * FormulaEstimate cost = client.formulas().estimate(params);
 * FormulaRun run = client.formulas().run(params);
 * }</pre>
 */
public final class FormulaService {
    private final Transport transport;
    private final Requests requests;

    FormulaService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * The catalogue: {@code GET /v1/formulas}. Free to read.
     *
     * @return the formulas listed for this account
     */
    public List<Formula> list() {
        return list(FormulaListParams.none());
    }

    /**
     * The formulas that match: {@code GET /v1/formulas}.
     *
     * @param params words to search for, a category, a persona or a status
     * @return the formulas that match
     */
    public List<Formula> list(FormulaListParams params) {
        return transport.execute(requests.formulas(params));
    }

    /**
     * One formula, with the JSON Schema of its parameters, its presets and columns, what it needs and whether this
     * account has it, and sample rows: {@code GET /v1/formulas/{id}}. Free to read.
     *
     * @param formulaId the formula's id, e.g. {@code job-order-hunter}
     * @return the formula
     * @throws com.enrichednow.errors.NotFoundException when there is no such formula ({@code formula_not_found})
     */
    public FormulaDetails get(String formulaId) {
        return transport.execute(requests.formula(formulaId));
    }

    /**
     * What a run with these parameters would cost: {@code POST /v1/formulas/{id}/estimate}. It spends nothing.
     *
     * @param formulaId the formula's id
     * @param params    its parameters: a {@code Map}, a {@link FormulaParams} of that formula, or any object written as
     *                  the JSON object its {@code parameters()} describe; null for none
     * @return the estimate; parameters that cannot run are not an error: {@link FormulaEstimate#valid()} is false and
     *     {@link FormulaEstimate#errors()} says what to change
     * @throws IllegalArgumentException when {@code params} is a {@link FormulaParams} of another formula
     */
    public FormulaEstimate estimate(String formulaId, Object params) {
        return estimate(formulaId, params, FormulaRunOptions.none());
    }

    /**
     * What a run with these parameters and options would cost: {@code POST /v1/formulas/{id}/estimate}. Of the
     * options, the preview and the caps count.
     *
     * @param formulaId the formula's id
     * @param params    its parameters
     * @param options   the run to estimate: a preview, a unit cap, a row cap
     * @return the estimate
     * @throws IllegalArgumentException when {@code params} is a {@link FormulaParams} of another formula
     */
    public FormulaEstimate estimate(String formulaId, Object params, FormulaRunOptions options) {
        return transport.execute(requests.estimateFormula(formulaId, params, options));
    }

    /**
     * What a run with these parameters would cost; the formula is theirs.
     *
     * @param params the parameters of one formula
     * @return the estimate
     */
    public FormulaEstimate estimate(FormulaParams params) {
        return estimate(params, FormulaRunOptions.none());
    }

    /**
     * What a run with these parameters and options would cost; the formula is theirs.
     *
     * @param params  the parameters of one formula
     * @param options the run to estimate: a preview, a unit cap, a row cap
     * @return the estimate
     */
    public FormulaEstimate estimate(FormulaParams params, FormulaRunOptions options) {
        return estimate(formulaOf(params), params, options);
    }

    /**
     * Starts a run and returns as soon as it is queued: {@code POST /v1/formulas/{id}/runs}.
     *
     * @param formulaId the formula's id
     * @param params    its parameters: a {@code Map}, a {@link FormulaParams} of that formula, or any object written as
     *                  the JSON object its {@code parameters()} describe
     * @return the run, usually {@code queued}
     * @see #run(String, Object, FormulaRunOptions)
     */
    public FormulaRun run(String formulaId, Object params) {
        return run(formulaId, params, FormulaRunOptions.none());
    }

    /**
     * Starts a run: {@code POST /v1/formulas/{id}/runs}.
     *
     * <p>With a wait ({@link FormulaRunOptions#waitUpTo}) the call holds on for the run to end. A run that ended in
     * time comes back with a final status; otherwise it comes back {@code queued} or {@code running}, and
     * {@link FormulaRunService#waitFor(String)} picks it up. Either way read its status: a run that found nothing is
     * still a successful call. Its rows can be read while it runs.
     *
     * <p>The request carries an {@code Idempotency-Key}, so it is safe to send again: after a failure in flight or a
     * server error it is retried with the same key, and the API answers with the run the first attempt started.
     *
     * @param formulaId the formula's id
     * @param params    its parameters
     * @param options   a preview, the caps, the wait, the idempotency key
     * @return the run
     * @throws IllegalArgumentException                                  when {@code params} is a {@link FormulaParams}
     *                                                                   of another formula; nothing is sent
     * @throws com.enrichednow.errors.ValidationException                when the parameters do not fit, or the unit cap
     *                                                                   is too low ({@code unit_cap_too_low})
     * @throws com.enrichednow.errors.AttestationRequiredException       when the terms of a formula that returns people
     *                                                                   are not accepted yet
     * @throws com.enrichednow.errors.RequirementMissingException        when the account lacks what the formula needs
     * @throws com.enrichednow.errors.FormulaUnavailableException        when the formula cannot be run now
     * @throws com.enrichednow.errors.QuotaExceededException             when the account cannot pay its least cost
     */
    public FormulaRun run(String formulaId, Object params, FormulaRunOptions options) {
        return transport.execute(requests.startFormulaRun(formulaId, params, options));
    }

    /**
     * Starts a run of the formula these parameters are for, and returns as soon as it is queued.
     *
     * @param params the parameters of one formula
     * @return the run, usually {@code queued}
     */
    public FormulaRun run(FormulaParams params) {
        return run(params, FormulaRunOptions.none());
    }

    /**
     * Starts a run of the formula these parameters are for.
     *
     * @param params  the parameters of one formula
     * @param options a preview, the caps, the wait, the idempotency key
     * @return the run
     * @see #run(String, Object, FormulaRunOptions)
     */
    public FormulaRun run(FormulaParams params, FormulaRunOptions options) {
        return run(formulaOf(params), params, options);
    }

    static String formulaOf(FormulaParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params must not be null: give the formula's id and a Map instead");
        }
        return params.formulaId();
    }
}
