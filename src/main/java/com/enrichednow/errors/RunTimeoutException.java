package com.enrichednow.errors;

import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.Run;

/**
 * Waiting for a run gave up before the run ended. The run carries on: read it later with
 * {@code client.runs().get(id)}, or stop it with {@code client.runs().cancel(id)}; for a formula run,
 * {@code client.formulaRuns().get(id)} and {@code cancel(id)}.
 */
public class RunTimeoutException extends EnrichedNowException {
    private static final long serialVersionUID = 1L;

    private final transient Run run;
    private final transient FormulaRun formulaRun;
    private final String runId;

    /**
     * @param run the run as it was when the wait gave up
     */
    public RunTimeoutException(Run run) {
        super("Run " + run.id() + " had not ended when the wait gave up; it is " + run.rawStatus());
        this.run = run;
        this.formulaRun = null;
        this.runId = run.id();
    }

    /**
     * @param run the formula run as it was when the wait gave up
     */
    public RunTimeoutException(FormulaRun run) {
        super("Formula run " + run.id() + " had not ended when the wait gave up; it is " + run.rawStatus());
        this.run = null;
        this.formulaRun = run;
        this.runId = run.id();
    }

    /** @return the run as it was when the wait gave up; {@code null} when the wait was for a formula run */
    public Run run() {
        return run;
    }

    /** @return the formula run as it was when the wait gave up; {@code null} when the wait was for a scraper run */
    public FormulaRun formulaRun() {
        return formulaRun;
    }

    /** @return the id of the run */
    public String runId() {
        return runId;
    }
}
