package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * One run of a formula with one set of parameters. It works in the background and ends with a status and a list of
 * rows; its rows can be read while it is still going.
 *
 * <p>Its status is a {@link RunStatus}, as a scraper run's is: {@code queued} and {@code running} are in progress;
 * {@code ok}, {@code partial}, {@code failed}, {@code unsupported} and {@code cancelled} are final.
 */
public final class FormulaRun {
    private final String id;
    private final String formulaId;
    private final String version;
    private final String savedFormulaId;
    private final String rawStatus;
    private final RunStatus status;
    private final String reason;
    private final List<Reason> reasons;
    private final String mode;
    private final String trigger;
    private final Map<String, Object> params;
    private final CostEstimate estimate;
    private final long unitCap;
    private final long rowCap;
    private final long unitsSpent;
    private final long rowsTotal;
    private final boolean includeSeen;
    private final Instant scheduledFor;
    private final List<FormulaRunStep> steps;
    private final Instant createdAt;
    private final Instant startedAt;
    private final Instant finishedAt;

    @JsonCreator
    FormulaRun(@JsonProperty("id") String id,
               @JsonProperty("formula_id") String formulaId,
               @JsonProperty("version") String version,
               @JsonProperty("saved_formula_id") String savedFormulaId,
               @JsonProperty("status") String status,
               @JsonProperty("reason") String reason,
               @JsonProperty("reasons") List<Reason> reasons,
               @JsonProperty("mode") String mode,
               @JsonProperty("trigger") String trigger,
               @JsonProperty("params") Map<String, Object> params,
               @JsonProperty("estimate") CostEstimate estimate,
               @JsonProperty("unit_cap") long unitCap,
               @JsonProperty("row_cap") long rowCap,
               @JsonProperty("units_spent") long unitsSpent,
               @JsonProperty("rows_total") long rowsTotal,
               @JsonProperty("include_seen") boolean includeSeen,
               @JsonProperty("scheduled_for") String scheduledFor,
               @JsonProperty("steps") List<FormulaRunStep> steps,
               @JsonProperty("created_at") String createdAt,
               @JsonProperty("started_at") String startedAt,
               @JsonProperty("finished_at") String finishedAt) {
        this.id = id;
        this.formulaId = formulaId;
        this.version = version;
        this.savedFormulaId = savedFormulaId;
        this.rawStatus = status;
        this.status = RunStatus.fromValue(status);
        this.reason = reason;
        this.reasons = Values.list(reasons);
        this.mode = mode;
        this.trigger = trigger;
        this.params = Values.map(params);
        this.estimate = estimate;
        this.unitCap = unitCap;
        this.rowCap = rowCap;
        this.unitsSpent = unitsSpent;
        this.rowsTotal = rowsTotal;
        this.includeSeen = includeSeen;
        this.scheduledFor = Values.instant(scheduledFor);
        this.steps = Values.list(steps);
        this.createdAt = Values.instant(createdAt);
        this.startedAt = Values.instant(startedAt);
        this.finishedAt = Values.instant(finishedAt);
    }

    /** @return the run's id, {@code frun_…} */
    public String id() {
        return id;
    }

    /** @return the id of the formula that ran */
    public String formulaId() {
        return formulaId;
    }

    /** @return the version of the formula it ran with */
    public String version() {
        return version;
    }

    /** @return the saved formula it is a run of, or null for a run started on its own */
    public String savedFormulaId() {
        return savedFormulaId;
    }

    /** @return the run's status */
    public RunStatus status() {
        return status;
    }

    /** @return the status exactly as the API wrote it */
    public String rawStatus() {
        return rawStatus;
    }

    /**
     * @return why it is not plainly {@code ok}, e.g. {@code unit_cap_reached}, {@code contacts_not_connected},
     *     {@code source_failed:<source>}; null when it is, or while it is going
     */
    public String reason() {
        return reason;
    }

    /** @return every reason, when there is more than one, with the source or step each concerns */
    public List<Reason> reasons() {
        return reasons;
    }

    /** @return {@code full}, or {@code preview}: at most 10 rows and 50 units, nothing remembered as delivered */
    public String mode() {
        return mode;
    }

    /** @return whether it is a preview run */
    public boolean isPreview() {
        return "preview".equals(mode);
    }

    /** @return what started it: {@code api}, {@code console}, {@code mcp}, or {@code schedule} for a saved formula's schedule */
    public String trigger() {
        return trigger;
    }

    /** @return the parameters it ran with, the formula's defaults filled in */
    public Map<String, Object> params() {
        return params;
    }

    /** @return what it was estimated to cost when it started, or null */
    public CostEstimate estimate() {
        return estimate;
    }

    /** @return the most it may spend: it stops there, keeps what it has and ends {@code partial} */
    public long unitCap() {
        return unitCap;
    }

    /** @return the most rows it may return */
    public long rowCap() {
        return rowCap;
    }

    /** @return the units charged so far */
    public long unitsSpent() {
        return unitsSpent;
    }

    /** @return the rows it has written so far */
    public long rowsTotal() {
        return rowsTotal;
    }

    /** @return for a saved formula's run, whether it was asked to deliver rows already delivered too */
    public boolean includeSeen() {
        return includeSeen;
    }

    /** @return the slot of the schedule it ran for, or null for a run that was not scheduled */
    public Instant scheduledFor() {
        return scheduledFor;
    }

    /** @return what each step did; empty in a list of runs, where the API leaves the steps out */
    public List<FormulaRunStep> steps() {
        return steps;
    }

    /** @return when it was created */
    public Instant createdAt() {
        return createdAt;
    }

    /** @return when it started working, or null */
    public Instant startedAt() {
        return startedAt;
    }

    /** @return when it ended, or null while it is in progress */
    public Instant finishedAt() {
        return finishedAt;
    }

    /** @return whether it has ended, with any final status */
    public boolean isFinished() {
        return status.isFinal();
    }

    /** @return whether it ended {@code ok} or {@code partial}: with rows to read */
    public boolean isSuccessful() {
        return status.isSuccessful();
    }

    @Override
    public String toString() {
        return "FormulaRun{id=" + id + ", formulaId=" + formulaId + ", status=" + rawStatus + ", reason=" + reason
                + ", rowsTotal=" + rowsTotal + ", unitsSpent=" + unitsSpent + '}';
    }

    /** One reason a run is not plainly {@code ok}, with what it concerns. */
    public static final class Reason {
        private final String code;
        private final Map<String, Object> values;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        Reason(Map<String, Object> values) {
            this.values = Values.map(values);
            this.code = Values.text(values, "code");
        }

        /** @return the reason's code, e.g. {@code source_not_offered} */
        public String code() {
            return code;
        }

        /** @return everything the API wrote of it, {@code code} included: e.g. the {@code step} and {@code scraper} it concerns */
        public Map<String, Object> values() {
            return values;
        }

        @Override
        public String toString() {
            return values.toString();
        }
    }
}
