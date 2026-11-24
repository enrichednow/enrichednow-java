package com.enrichednow;

import com.enrichednow.formulas.FormulaParams;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A formula to save, with its parameters, caps and schedule: what {@link SavedFormulaService#create} sends. What is not
 * set is left to the API's default.
 *
 * <pre>{@code
 * SavedFormulaCreate.builder(JobOrderHunterParams.builder().jobTitles("Java Developer").locations("London").build())
 *         .schedule(Schedule.weekdays("07:30", "Europe/London"))
 *         .notify(Notify.DIGEST)
 *         .build();
 * }</pre>
 */
public final class SavedFormulaCreate {
    private final String formulaId;
    private final Map<String, Object> body;

    private SavedFormulaCreate(String formulaId, Map<String, Object> body) {
        this.formulaId = formulaId;
        this.body = Collections.unmodifiableMap(body);
    }

    /**
     * @param formulaId the formula to save, e.g. {@code job-order-hunter}
     * @param params    its parameters: a {@code Map}, a {@link FormulaParams} of that formula, or any object written as
     *                  the JSON object its {@code parameters()} schema describes; null for none
     * @return a builder
     * @throws IllegalArgumentException when {@code params} is a {@link FormulaParams} of another formula
     */
    public static Builder builder(String formulaId, Object params) {
        return new Builder(formulaId, params);
    }

    /**
     * @param params the parameters of the formula to save; the formula is theirs
     * @return a builder
     */
    public static Builder builder(FormulaParams params) {
        Objects.requireNonNull(params, "params");
        return new Builder(params.formulaId(), params);
    }

    /** @return the formula to save */
    public String formulaId() {
        return formulaId;
    }

    Map<String, Object> body() {
        return body;
    }

    @Override
    public String toString() {
        return "SavedFormulaCreate" + body;
    }

    /** Builds a {@link SavedFormulaCreate}. */
    public static final class Builder {
        private final String formulaId;
        private final Map<String, Object> body = new LinkedHashMap<>();

        private Builder(String formulaId, Object params) {
            if (formulaId == null || formulaId.isEmpty()) {
                throw new IllegalArgumentException("formulaId must not be empty");
            }
            this.formulaId = formulaId;
            body.put("formula_id", formulaId);
            body.put("params", Requests.params(formulaId, params));
        }

        /**
         * @param name its name, 1 to 200 characters; default: made from its parameters and cadence, and kept in step
         *             with them
         * @return this builder
         */
        public Builder name(String name) {
            body.put("name", Objects.requireNonNull(name, "name"));
            return this;
        }

        /**
         * @param schedule when it runs by itself; default: only by hand
         * @return this builder
         */
        public Builder schedule(Schedule schedule) {
            body.put("schedule", Objects.requireNonNull(schedule, "schedule").toMap());
            return this;
        }

        /**
         * @param unitCap the most one run may spend; default: the formula's. Above it: owners and admins only
         * @return this builder
         */
        public Builder unitCap(int unitCap) {
            body.put("unit_cap", FormulaRunOptions.positive(unitCap, "unitCap"));
            return this;
        }

        /**
         * @param rowCap the most rows one run may return
         * @return this builder
         */
        public Builder rowCap(int rowCap) {
            body.put("row_cap", FormulaRunOptions.positive(rowCap, "rowCap"));
            return this;
        }

        /**
         * @param monthlyUnitCap the most its runs may spend in a calendar month (UTC); a run is capped to what is left
         * @return this builder
         */
        public Builder monthlyUnitCap(int monthlyUnitCap) {
            body.put("monthly_unit_cap", FormulaRunOptions.positive(monthlyUnitCap, "monthlyUnitCap"));
            return this;
        }

        /**
         * @param notify when mail is sent about scheduled runs that delivered new rows; default {@link Notify#EACH_RUN}
         * @return this builder
         */
        public Builder notify(Notify notify) {
            body.put("notify", Objects.requireNonNull(notify, "notify").value());
            return this;
        }

        /** @return what to save */
        public SavedFormulaCreate build() {
            return new SavedFormulaCreate(formulaId, new LinkedHashMap<>(body));
        }
    }
}
