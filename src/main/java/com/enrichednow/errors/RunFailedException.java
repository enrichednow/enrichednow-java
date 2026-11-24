package com.enrichednow.errors;

import com.enrichednow.models.Run;
import com.enrichednow.models.RunStatus;

/**
 * A run ended {@code failed}, {@code unsupported} or {@code cancelled}: the request worked, the scrape did not.
 * Thrown by {@code client.scrape(...)} unless it was told not to; its subclass {@link FormulaRunFailedException} by
 * {@code client.runFormula(...)}.
 *
 * <p>{@link #reason()} is a stable code that says why, e.g. {@code render_required} or {@code fetch_failed}.
 */
public class RunFailedException extends EnrichedNowException {
    private static final long serialVersionUID = 1L;

    private final transient Run run;
    private final RunStatus status;
    private final String reason;
    private final String runId;

    /**
     * @param run the run as it ended
     */
    public RunFailedException(Run run) {
        super("Run " + run.id() + " of " + run.scraperId() + " ended " + run.rawStatus()
                + (run.reason() != null ? " (" + run.reason() + ")" : ""));
        this.run = run;
        this.status = run.status();
        this.reason = run.reason();
        this.runId = run.id();
    }

    /**
     * For a subclass about a run of another kind: {@link #run()} is then {@code null}.
     *
     * @param message what happened
     * @param status  the status the run ended with
     * @param reason  the run's reason code, or {@code null}
     * @param runId   the run's id
     */
    protected RunFailedException(String message, RunStatus status, String reason, String runId) {
        super(message);
        this.run = null;
        this.status = status;
        this.reason = reason;
        this.runId = runId;
    }

    /** @return the run as it ended */
    public Run run() {
        return run;
    }

    /** @return the id of the run */
    public String runId() {
        return runId;
    }

    /** @return the status the run ended with */
    public RunStatus status() {
        return status;
    }

    /** @return the run's reason code, or {@code null} */
    public String reason() {
        return reason;
    }
}
