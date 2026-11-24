// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Compound Signal Score ({@code compound-signal-score}): Your target accounts scored by what they
 * are doing now: hiring, raising, new leaders, expanding and advertising.
 *
 * Only {@code accounts} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * CompoundSignalScoreParams params = CompoundSignalScoreParams.builder()
 *     .accounts("Ramp (ramp.com)", "Rippling")
 *     .build();
 * }</pre>
 */
public final class CompoundSignalScoreParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "compound-signal-score";

    private final Map<String, Object> values;

    private CompoundSignalScoreParams(Map<String, Object> values) {
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
        return "CompoundSignalScoreParams" + values;
    }

    /** Builds {@link CompoundSignalScoreParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Accounts: The companies to score, by name, by domain, or both as Rippling (rippling.com); a domain given
         * needs no search to match. 1 to 50 items. Required.
         *
         * @param accounts the values; not null
         * @return this builder
         */
        public Builder accounts(List<String> accounts) {
            values.put("accounts", List.copyOf(accounts));
            return this;
        }

        /**
         * Accounts: The companies to score, by name, by domain, or both as Rippling (rippling.com); a domain given
         * needs no search to match. 1 to 50 items. Required.
         *
         * @param accounts the values
         * @return this builder
         */
        public Builder accounts(String... accounts) {
            return accounts(Arrays.asList(accounts));
        }

        /**
         * Look back (days): How recent news, postings and ads must be to count. From 7 to 180. Default: 30.
         *
         * @param windowDays the value
         * @return this builder
         */
        public Builder windowDays(int windowDays) {
            values.put("window_days", windowDays);
            return this;
        }

        /**
         * Signals to read: What to read for each account. Each costs units for every account. Each one of "hiring",
         * "announcements", "ads". At least 1 item. Default: ["hiring", "announcements", "ads"].
         *
         * @param signals the values; not null
         * @return this builder
         */
        public Builder signals(List<String> signals) {
            values.put("signals", List.copyOf(signals));
            return this;
        }

        /**
         * Signals to read: What to read for each account. Each costs units for every account. Each one of "hiring",
         * "announcements", "ads". At least 1 item. Default: ["hiring", "announcements", "ads"].
         *
         * @param signals the values
         * @return this builder
         */
        public Builder signals(String... signals) {
            return signals(Arrays.asList(signals));
        }

        /**
         * Weight of hiring: Points for open roles on its careers page, in full at ten roles or more. From 0 to 100.
         * Default: 30.
         *
         * @param weightHiring the value
         * @return this builder
         */
        public Builder weightHiring(int weightHiring) {
            values.put("weight_hiring", weightHiring);
            return this;
        }

        /**
         * Weight of funding: Points for a funding announcement in the window. From 0 to 100. Default: 25.
         *
         * @param weightFunding the value
         * @return this builder
         */
        public Builder weightFunding(int weightFunding) {
            values.put("weight_funding", weightFunding);
            return this;
        }

        /**
         * Weight of a senior hire: Points for an announced executive hire in the window. From 0 to 100. Default:
         * 15.
         *
         * @param weightExecutive the value
         * @return this builder
         */
        public Builder weightExecutive(int weightExecutive) {
            values.put("weight_executive", weightExecutive);
            return this;
        }

        /**
         * Weight of an expansion: Points for an announced expansion or new office in the window. From 0 to 100.
         * Default: 10.
         *
         * @param weightExpansion the value
         * @return this builder
         */
        public Builder weightExpansion(int weightExpansion) {
            values.put("weight_expansion", weightExpansion);
            return this;
        }

        /**
         * Weight of a launch: Points for an announced product launch in the window. From 0 to 100. Default: 5.
         *
         * @param weightLaunch the value
         * @return this builder
         */
        public Builder weightLaunch(int weightLaunch) {
            values.put("weight_launch", weightLaunch);
            return this;
        }

        /**
         * Weight of advertising: Points for ads shown in the window. From 0 to 100. Default: 15.
         *
         * @param weightAds the value
         * @return this builder
         */
        public Builder weightAds(int weightAds) {
            values.put("weight_ads", weightAds);
            return this;
        }

        /**
         * Most open roles: Leave out accounts with more open roles than this, to find those that will hire before
         * they post. At least 0. Default: 100000.
         *
         * @param maxOpenRoles the value
         * @return this builder
         */
        public Builder maxOpenRoles(int maxOpenRoles) {
            values.put("max_open_roles", maxOpenRoles);
            return this;
        }

        /**
         * Ads read per account: How many of an account's newest ads are read; each costs a unit. From 1 to 40.
         * Default: 10.
         *
         * @param adsRead the value
         * @return this builder
         */
        public Builder adsRead(int adsRead) {
            values.put("ads_read", adsRead);
            return this;
        }

        /**
         * Least score: Accounts scoring below this are left out. At least 0. Default: 0.
         *
         * @param minScore the value
         * @return this builder
         */
        public Builder minScore(double minScore) {
            values.put("min_score", minScore);
            return this;
        }

        /**
         * Most rows: The run stops at this many accounts, best first. From 1 to 50. Default: 50.
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
        public CompoundSignalScoreParams build() {
            if (!values.containsKey("accounts")) {
                throw new IllegalStateException("CompoundSignalScoreParams: accounts is required");
            }
            return new CompoundSignalScoreParams(values);
        }
    }
}
