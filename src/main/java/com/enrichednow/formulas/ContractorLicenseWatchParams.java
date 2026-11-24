// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Contractor Licence Watch ({@code contractor-license-watch}): Contractor businesses whose
 * licence, bond or insurance expires in the next weeks, from official state and city registers.
 *
 * Only {@code register} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * ContractorLicenseWatchParams params = ContractorLicenseWatchParams.builder()
 *     .register("oregon")
 *     .build();
 * }</pre>
 */
public final class ContractorLicenseWatchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "contractor-license-watch";

    private final Map<String, Object> values;

    private ContractorLicenseWatchParams(Map<String, Object> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    /**
     * A builder with nothing set.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String formulaId() {
        return FORMULA_ID;
    }

    @Override
    public Map<String, Object> toMap() {
        return values;
    }

    @Override
    public String toString() {
        return "ContractorLicenseWatchParams" + values;
    }

    /** Builds {@link ContractorLicenseWatchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Register: Washington L&amp;I, Texas TDLR, Oregon CCB (with bond and insurance dates), Chicago home
         * repair, Buffalo, or New York's mold and elevator contractors. One of "washington", "texas", "oregon",
         * "chicago", "buffalo", "new_york_mold", "new_york_elevator". Required.
         *
         * @param register the value
         * @return this builder
         */
        public Builder register(String register) {
            values.put("register", register);
            return this;
        }

        /**
         * Expiring within (days): Licences that run out between today and this many days from now. From 1 to 180.
         * Default: 30.
         *
         * @param expiringWithinDays the value
         * @return this builder
         */
        public Builder expiringWithinDays(int expiringWithinDays) {
            values.put("expiring_within_days", expiringWithinDays);
            return this;
        }

        /**
         * Trades: Words in the licence's type or trade, such as electrical, plumbing or roofing. Each is one
         * search. Empty reads every trade. At most 8 items. Default: [].
         *
         * @param trades the values; not null
         * @return this builder
         */
        public Builder trades(List<String> trades) {
            values.put("trades", List.copyOf(trades));
            return this;
        }

        /**
         * Trades: Words in the licence's type or trade, such as electrical, plumbing or roofing. Each is one
         * search. Empty reads every trade. At most 8 items. Default: [].
         *
         * @param trades the values
         * @return this builder
         */
        public Builder trades(String... trades) {
            return trades(Arrays.asList(trades));
        }

        /**
         * Only licences in good standing: Leave out licences the register shows as suspended, revoked, cancelled or
         * expired. Default: true.
         *
         * @param activeOnly the value
         * @return this builder
         */
        public Builder activeOnly(boolean activeOnly) {
            values.put("active_only", activeOnly);
            return this;
        }

        /**
         * Licences read per search: The register is read up to this many licences for each trade word. From 10 to
         * 200. Default: 200.
         *
         * @param licencesToRead the value
         * @return this builder
         */
        public Builder licencesToRead(int licencesToRead) {
            values.put("licences_to_read", licencesToRead);
            return this;
        }

        /**
         * Show a licence again after (days): For a saved formula, how long before the same licence and expiry date
         * may come back. From 1 to 365. Default: 300.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many licences, soonest expiry first. From 1 to 2000. Default: 500.
         *
         * @param maxResults the value
         * @return this builder
         */
        public Builder maxResults(int maxResults) {
            values.put("max_results", maxResults);
            return this;
        }

        /**
         * The parameters, once every required one is set.
         *
         * @return the parameters
         * @throws IllegalStateException when a required parameter is not set
         */
        public ContractorLicenseWatchParams build() {
            if (!values.containsKey("register")) {
                throw new IllegalStateException("ContractorLicenseWatchParams: register is required");
            }
            return new ContractorLicenseWatchParams(values);
        }
    }
}
