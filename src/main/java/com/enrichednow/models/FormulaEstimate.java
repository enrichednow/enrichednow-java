package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * What a run of a formula with a set of parameters would cost, as {@code POST /v1/formulas/{id}/estimate} answers. It
 * spends nothing. Parameters that cannot run are not an error: {@link #valid()} is false and {@link #errors()} says
 * what to change.
 */
public final class FormulaEstimate {
    private final boolean valid;
    private final List<Problem> errors;
    private final CostEstimate estimate;
    private final List<String> missing;
    private final List<String> notOffered;

    @JsonCreator
    FormulaEstimate(@JsonProperty("valid") boolean valid,
                    @JsonProperty("errors") List<Problem> errors,
                    @JsonProperty("estimate") CostEstimate estimate,
                    @JsonProperty("missing") List<String> missing,
                    @JsonProperty("not_offered") List<String> notOffered) {
        this.valid = valid;
        this.errors = Values.list(errors);
        this.estimate = estimate;
        this.missing = Values.list(missing);
        this.notOffered = Values.list(notOffered);
    }

    /** @return whether the parameters can run; when false, {@link #errors()} says why and {@link #estimate()} is null */
    public boolean valid() {
        return valid;
    }

    /** @return what is wrong with the parameters, one problem each; empty when they are valid */
    public List<Problem> errors() {
        return errors;
    }

    /** @return the estimate, or null when the parameters are not valid */
    public CostEstimate estimate() {
        return estimate;
    }

    /** @return what this account or deployment lacks for these parameters, e.g. {@code product:data}; empty when nothing */
    public List<String> missing() {
        return missing;
    }

    /** @return sources these parameters ask for that this deployment does not offer: the run goes on without them */
    public List<String> notOffered() {
        return notOffered;
    }

    @Override
    public String toString() {
        return "FormulaEstimate{valid=" + valid + ", errors=" + errors + ", estimate=" + estimate + ", missing=" + missing + '}';
    }

    /** One thing wrong with the parameters. */
    public static final class Problem {
        private final String path;
        private final String message;

        @JsonCreator
        Problem(@JsonProperty("path") String path, @JsonProperty("message") String message) {
            this.path = path;
            this.message = message;
        }

        /** @return where the problem is, e.g. {@code params.job_titles} or {@code unit_cap} */
        public String path() {
            return path;
        }

        /** @return what is wrong there */
        public String message() {
            return message;
        }

        @Override
        public String toString() {
            return path + ": " + message;
        }
    }
}
