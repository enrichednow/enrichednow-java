package com.enrichednow;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * What {@link SavedFormulaService#update} changes of a saved formula; what is not set is left as it is.
 *
 * <pre>{@code
 * SavedFormulaChange.builder().name("Java in London").monthlyUnitCap(20000).build();
 * SavedFormulaChange.builder().removeSchedule().build();        // runs only by hand from now on
 * SavedFormulaChange.builder().autoName().build();              // back to a name made from its parameters
 * }</pre>
 */
public final class SavedFormulaChange {
    private final Map<String, Object> body;

    private SavedFormulaChange(Map<String, Object> body) {
        this.body = Collections.unmodifiableMap(body);
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** What is sent: each field set, a removal as {@code null}. */
    Map<String, Object> body() {
        return body;
    }

    @Override
    public String toString() {
        return "SavedFormulaChange" + body;
    }

    /** Builds a {@link SavedFormulaChange}. */
    public static final class Builder {
        private final Map<String, Object> body = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * @param name its new name, 1 to 200 characters; it stops following its parameters
         * @return this builder
         * @see #autoName()
         */
        public Builder name(String name) {
            body.put("name", Objects.requireNonNull(name, "name: autoName() goes back to a name made from the parameters"));
            return this;
        }

        /**
         * Goes back to a name made from its parameters and cadence, which follows them when they change.
         *
         * @return this builder
         */
        public Builder autoName() {
            body.put("name", null);
            return this;
        }

        /**
         * New parameters: the whole set, what is left out takes the formula's default. Rows already delivered stay
         * delivered unless {@link #resetSeen(boolean)}.
         *
         * @param params a {@code Map}, a {@link com.enrichednow.formulas.FormulaParams}, or any object written as the
         *               JSON object of the formula's {@code parameters()} schema
         * @return this builder
         */
        public Builder params(Object params) {
            body.put("params", Requests.params(null, Objects.requireNonNull(params, "params")));
            return this;
        }

        /**
         * @param resetSeen with new parameters, true to forget what was delivered, so rows already delivered come again
         *                  (owners and admins)
         * @return this builder
         */
        public Builder resetSeen(boolean resetSeen) {
            body.put("reset_seen", resetSeen);
            return this;
        }

        /**
         * @param unitCap the most one run may spend
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
         * @param monthlyUnitCap the most its runs may spend in a calendar month (UTC)
         * @return this builder
         * @see #removeMonthlyUnitCap()
         */
        public Builder monthlyUnitCap(int monthlyUnitCap) {
            body.put("monthly_unit_cap", FormulaRunOptions.positive(monthlyUnitCap, "monthlyUnitCap"));
            return this;
        }

        /**
         * Removes the monthly unit cap: its runs are held only by their own unit cap and the account's limits.
         *
         * @return this builder
         */
        public Builder removeMonthlyUnitCap() {
            body.put("monthly_unit_cap", null);
            return this;
        }

        /**
         * @param schedule its new schedule
         * @return this builder
         * @see #removeSchedule()
         */
        public Builder schedule(Schedule schedule) {
            body.put("schedule", Objects.requireNonNull(schedule, "schedule: removeSchedule() removes it").toMap());
            return this;
        }

        /**
         * Removes the schedule: it runs only by hand from now on.
         *
         * @return this builder
         */
        public Builder removeSchedule() {
            body.put("schedule", null);
            return this;
        }

        /**
         * @param notify when mail is sent about scheduled runs that delivered new rows
         * @return this builder
         */
        public Builder notify(Notify notify) {
            body.put("notify", Objects.requireNonNull(notify, "notify").value());
            return this;
        }

        /** @return the change */
        public SavedFormulaChange build() {
            return new SavedFormulaChange(new LinkedHashMap<>(body));
        }
    }
}
