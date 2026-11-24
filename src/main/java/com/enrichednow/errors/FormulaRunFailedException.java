package com.enrichednow.errors;

import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.Run;

/**
 * A formula run ended {@code failed}, {@code unsupported} or {@code cancelled}: the request worked, the run did not.
 * Thrown by {@code client.runFormula(...)} unless it was told not to. A run that ended {@code partial} is not a
 * failure: it has rows.
 *
 * <p>It is a {@link RunFailedException}, so code that catches that one catches this too; {@link #formulaRun()} is the
 * run, and {@link #reason()} says why it failed, e.g. {@code source_failed:<source>}.
 */
public class FormulaRunFailedException extends RunFailedException {
    private static final long serialVersionUID = 1L;

    private final transient FormulaRun formulaRun;
    private final String formulaId;

    /**
     * @param run the formula run as it ended
     */
    public FormulaRunFailedException(FormulaRun run) {
        super("Formula run " + run.id() + " of " + run.formulaId() + " ended " + run.rawStatus()
                + (run.reason() != null ? " (" + run.reason() + ")" : ""), run.status(), run.reason(), run.id());
        this.formulaRun = run;
        this.formulaId = run.formulaId();
    }

    /** @return the formula run as it ended */
    public FormulaRun formulaRun() {
        return formulaRun;
    }

    /** @return the id of the formula that ran */
    public String formulaId() {
        return formulaId;
    }

    /** @return {@code null}: the run is a formula run, {@link #formulaRun()} */
    @Override
    public Run run() {
        return null;
    }
}
