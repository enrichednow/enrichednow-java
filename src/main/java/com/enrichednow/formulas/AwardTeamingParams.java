// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Award Teaming Leads ({@code award-teaming}): Companies that just won government contracts in
 * your field, with what they won, their website and the email they publish.
 *
 * A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * AwardTeamingParams params = AwardTeamingParams.builder()
 *     .build();
 * }</pre>
 */
public final class AwardTeamingParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "award-teaming";

    private final Map<String, Object> values;

    private AwardTeamingParams(Map<String, Object> values) {
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
        return "AwardTeamingParams" + values;
    }

    /** Builds {@link AwardTeamingParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * NAICS codes: The industry codes of the work, 2 to 6 digits, such as 541512 (computer systems design).
         * Each is searched on its own. At most 10 items. Default: [].
         *
         * @param naicsCodes the values; not null
         * @return this builder
         */
        public Builder naicsCodes(List<String> naicsCodes) {
            values.put("naics_codes", List.copyOf(naicsCodes));
            return this;
        }

        /**
         * NAICS codes: The industry codes of the work, 2 to 6 digits, such as 541512 (computer systems design).
         * Each is searched on its own. At most 10 items. Default: [].
         *
         * @param naicsCodes the values
         * @return this builder
         */
        public Builder naicsCodes(String... naicsCodes) {
            return naicsCodes(Arrays.asList(naicsCodes));
        }

        /**
         * Words: Words in the award's description, such as cybersecurity or janitorial. Each is searched on its
         * own. At most 10 items. Default: [].
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Words: Words in the award's description, such as cybersecurity or janitorial. Each is searched on its
         * own. At most 10 items. Default: [].
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Awarding departments: US departments by their full name, such as Department of Defense or Department of
         * Veterans Affairs. At most 5 items. Default: [].
         *
         * @param agencies the values; not null
         * @return this builder
         */
        public Builder agencies(List<String> agencies) {
            values.put("agencies", List.copyOf(agencies));
            return this;
        }

        /**
         * Awarding departments: US departments by their full name, such as Department of Defense or Department of
         * Veterans Affairs. At most 5 items. Default: [].
         *
         * @param agencies the values
         * @return this builder
         */
        public Builder agencies(String... agencies) {
            return agencies(Arrays.asList(agencies));
        }

        /**
         * Awarded within (days): Awards whose work started, or whose notice was published, in the last this many
         * days. Recently signed work that starts later is kept. From 7 to 364. Default: 30.
         *
         * @param awardedWithinDays the value
         * @return this builder
         */
        public Builder awardedWithinDays(int awardedWithinDays) {
            values.put("awarded_within_days", awardedWithinDays);
            return this;
        }

        /**
         * Where awards are read: USAspending.gov (US federal contracts), SAM.gov award notices where offered, and
         * TED (EU contract awards). Each one of "usaspending", "sam_gov", "ted". At least 1 item. Default:
         * ["usaspending", "sam_gov"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Where awards are read: USAspending.gov (US federal contracts), SAM.gov award notices where offered, and
         * TED (EU contract awards). Each one of "usaspending", "sam_gov", "ted". At least 1 item. Default:
         * ["usaspending", "sam_gov"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Places of performance: Only US awards for work done in these states, as two letters, such as VA or TX. At
         * most 10 items. Default: [].
         *
         * @param usStates the values; not null
         * @return this builder
         */
        public Builder usStates(List<String> usStates) {
            values.put("us_states", List.copyOf(usStates));
            return this;
        }

        /**
         * Places of performance: Only US awards for work done in these states, as two letters, such as VA or TX. At
         * most 10 items. Default: [].
         *
         * @param usStates the values
         * @return this builder
         */
        public Builder usStates(String... usStates) {
            return usStates(Arrays.asList(usStates));
        }

        /**
         * Buyers' countries (TED): Only EU awards made by buyers in these countries, as three letters, such as DEU
         * or FRA. At most 10 items. Default: [].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Buyers' countries (TED): Only EU awards made by buyers in these countries, as three letters, such as DEU
         * or FRA. At most 10 items. Default: [].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Smallest award: Awards below this amount, in the award's currency, are left out. An award that states no
         * amount is kept. At least 0. Default: 100000.
         *
         * @param minAwardAmount the value
         * @return this builder
         */
        public Builder minAwardAmount(double minAwardAmount) {
            values.put("min_award_amount", minAwardAmount);
            return this;
        }

        /**
         * Winner's size: Small businesses or large primes, as USAspending records them. A winner it has no record
         * of is kept only for any. One of "any", "small_business", "other_than_small". Default: "any".
         *
         * @param winnerSize the value
         * @return this builder
         */
        public Builder winnerSize(String winnerSize) {
            values.put("winner_size", winnerSize);
            return this;
        }

        /**
         * Companies to leave out: Names, such as your competitors or the partners you already have. At most 1000
         * items. Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Companies to leave out: Names, such as your competitors or the partners you already have. At most 1000
         * items. Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Only companies with an email: Keeps the companies whose own website publishes an email address that
         * passes the check. Default: true.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Pages read per search: Each page is the next 100 awards, newest first. Raise it for a busy code or a long
         * window. From 1 to 5. Default: 2.
         *
         * @param pagesPerSearch the value
         * @return this builder
         */
        public Builder pagesPerSearch(int pagesPerSearch) {
            values.put("pages_per_search", pagesPerSearch);
            return this;
        }

        /**
         * Most rows: The run stops at this many companies, the biggest winners first. From 1 to 1000. Default: 100.
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
        public AwardTeamingParams build() {
            return new AwardTeamingParams(values);
        }
    }
}
