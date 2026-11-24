// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Permit Trade Leads ({@code permit-trade-leads}): Building permits just issued for your trades
 * in a US city, with each job's contractor, or the busiest contractors ranked.
 *
 * Only {@code city}, {@code trades} are required: {@link Builder#build()} refuses to build without them. A value
 * not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * PermitTradeLeadsParams params = PermitTradeLeadsParams.builder()
 *     .city("austin")
 *     .trades("roof", "solar")
 *     .build();
 * }</pre>
 */
public final class PermitTradeLeadsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "permit-trade-leads";

    private final Map<String, Object> values;

    private PermitTradeLeadsParams(Map<String, Object> values) {
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
        return "PermitTradeLeadsParams" + values;
    }

    /** Builds {@link PermitTradeLeadsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * City: The US city or county whose permits are read. Contractor names are published by Austin, Seattle,
         * New York, Dallas, Cincinnati, Baton Rouge, Orlando, Mesa, Raleigh and Chicago. One of "austin",
         * "chicago", "new_york", "los_angeles", "san_francisco", "seattle", "dallas", "new_orleans", "cincinnati",
         * "baton_rouge", "montgomery_county_md", "prince_georges_county_md", "norfolk", "buffalo", "orlando",
         * "mesa", "raleigh", "washington_dc", "minneapolis", "detroit". Required.
         *
         * @param city the value
         * @return this builder
         */
        public Builder city(String city) {
            values.put("city", city);
            return this;
        }

        /**
         * Trades: Words in a permit's type, work or description, such as roof, solar, hvac, deck or addition. Each
         * is one search. 1 to 8 items. Required.
         *
         * @param trades the values; not null
         * @return this builder
         */
        public Builder trades(List<String> trades) {
            values.put("trades", List.copyOf(trades));
            return this;
        }

        /**
         * Trades: Words in a permit's type, work or description, such as roof, solar, hvac, deck or addition. Each
         * is one search. 1 to 8 items. Required.
         *
         * @param trades the values
         * @return this builder
         */
        public Builder trades(String... trades) {
            return trades(Arrays.asList(trades));
        }

        /**
         * Issued within (days): Only permits issued in the last this many days. From 1 to 365. Default: 30.
         *
         * @param issuedWithinDays the value
         * @return this builder
         */
        public Builder issuedWithinDays(int issuedWithinDays) {
            values.put("issued_within_days", issuedWithinDays);
            return this;
        }

        /**
         * Rows are: One row a permit, or one row a contractor with how many permits it pulled. One of "permits",
         * "contractors". Default: "permits".
         *
         * @param view the value
         * @return this builder
         */
        public Builder view(String view) {
            values.put("view", view);
            return this;
        }

        /**
         * Least job value (USD): Only permits whose declared value is at least this. Permits with no value are kept
         * when this is 0. At least 0. Default: 0.
         *
         * @param minValue the value
         * @return this builder
         */
        public Builder minValue(double minValue) {
            values.put("min_value", minValue);
            return this;
        }

        /**
         * Only permits that name a contractor: Leave out permits the city published without a contractor. Default:
         * false.
         *
         * @param requireContractor the value
         * @return this builder
         */
        public Builder requireContractor(boolean requireContractor) {
            values.put("require_contractor", requireContractor);
            return this;
        }

        /**
         * Contractors to leave out: Names of contractors, such as your own business or current customers. At most
         * 2000 items. Default: [].
         *
         * @param excludeContractors the values; not null
         * @return this builder
         */
        public Builder excludeContractors(List<String> excludeContractors) {
            values.put("exclude_contractors", List.copyOf(excludeContractors));
            return this;
        }

        /**
         * Contractors to leave out: Names of contractors, such as your own business or current customers. At most
         * 2000 items. Default: [].
         *
         * @param excludeContractors the values
         * @return this builder
         */
        public Builder excludeContractors(String... excludeContractors) {
            return excludeContractors(Arrays.asList(excludeContractors));
        }

        /**
         * Permits read per trade: The newest this many permits are read for each trade word. From 10 to 200.
         * Default: 200.
         *
         * @param permitsPerTrade the value
         * @return this builder
         */
        public Builder permitsPerTrade(int permitsPerTrade) {
            values.put("permits_per_trade", permitsPerTrade);
            return this;
        }

        /**
         * Show a row again after (days): For a saved formula, how long before a delivered permit or contractor may
         * come back. From 1 to 365. Default: 90.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, newest permits or busiest contractors first. From 1 to 2000.
         * Default: 300.
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
        public PermitTradeLeadsParams build() {
            if (!values.containsKey("city")) {
                throw new IllegalStateException("PermitTradeLeadsParams: city is required");
            }
            if (!values.containsKey("trades")) {
                throw new IllegalStateException("PermitTradeLeadsParams: trades is required");
            }
            return new PermitTradeLeadsParams(values);
        }
    }
}
