// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Champion Tracker ({@code champion-tracker}): People who held your champions' roles at your
 * accounts and moved on, found at their new company, with contact details.
 *
 * Only {@code accounts}, {@code champion_titles} are required: {@link Builder#build()} refuses to build without
 * them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * ChampionTrackerParams params = ChampionTrackerParams.builder()
 *     .accounts("gong.io", "hubspot.com")
 *     .championTitles("VP Sales", "Head of Revenue Operations")
 *     .build();
 * }</pre>
 */
public final class ChampionTrackerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "champion-tracker";

    private final Map<String, Object> values;

    private ChampionTrackerParams(Map<String, Object> values) {
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
        return "ChampionTrackerParams" + values;
    }

    /** Builds {@link ChampionTrackerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Accounts: The website domains of the accounts where your champions work or worked, such as your
         * customers. 1 to 100 items. Required.
         *
         * @param accounts the values; not null
         * @return this builder
         */
        public Builder accounts(List<String> accounts) {
            values.put("accounts", List.copyOf(accounts));
            return this;
        }

        /**
         * Accounts: The website domains of the accounts where your champions work or worked, such as your
         * customers. 1 to 100 items. Required.
         *
         * @param accounts the values
         * @return this builder
         */
        public Builder accounts(String... accounts) {
            return accounts(Arrays.asList(accounts));
        }

        /**
         * Champion roles: The roles your champions held, such as VP Sales. 1 to 10 items. Required.
         *
         * @param championTitles the values; not null
         * @return this builder
         */
        public Builder championTitles(List<String> championTitles) {
            values.put("champion_titles", List.copyOf(championTitles));
            return this;
        }

        /**
         * Champion roles: The roles your champions held, such as VP Sales. 1 to 10 items. Required.
         *
         * @param championTitles the values
         * @return this builder
         */
        public Builder championTitles(String... championTitles) {
            return championTitles(Arrays.asList(championTitles));
        }

        /**
         * Who to find: moved: people who held these roles at the account and left, where they are now. successor:
         * who holds them at the account today. One of "moved", "successor". Default: "moved".
         *
         * @param track the value
         * @return this builder
         */
        public Builder track(String track) {
            values.put("track", track);
            return this;
        }

        /**
         * Least employees at the new company: Only follow people to companies at least this big. 1 follows
         * everyone. At least 1. Default: 1.
         *
         * @param minEmployees the value
         * @return this builder
         */
        public Builder minEmployees(int minEmployees) {
            values.put("min_employees", minEmployees);
            return this;
        }

        /**
         * New companies to leave out: Domains of companies not to follow people to, such as competitors or
         * customers already won. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * New companies to leave out: Domains of companies not to follow people to, such as competitors or
         * customers already won. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * People per account: The most people to find for each account. From 1 to 10. Default: 3.
         *
         * @param peoplePerAccount the value
         * @return this builder
         */
        public Builder peoplePerAccount(int peoplePerAccount) {
            values.put("people_per_account", peoplePerAccount);
            return this;
        }

        /**
         * Only addresses that pass the check: Keeps addresses whose domain takes mail, and those a verification
         * provider confirmed. Default: true.
         *
         * @param requireVerifiedEmail the value
         * @return this builder
         */
        public Builder requireVerifiedEmail(boolean requireVerifiedEmail) {
            values.put("require_verified_email", requireVerifiedEmail);
            return this;
        }

        /**
         * Show a person again after (days): For a saved formula, how long before a person already delivered may
         * come back. From 1 to 365. Default: 180.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many people, best first. From 1 to 2000. Default: 200.
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
        public ChampionTrackerParams build() {
            if (!values.containsKey("accounts")) {
                throw new IllegalStateException("ChampionTrackerParams: accounts is required");
            }
            if (!values.containsKey("champion_titles")) {
                throw new IllegalStateException("ChampionTrackerParams: champion_titles is required");
            }
            return new ChampionTrackerParams(values);
        }
    }
}
