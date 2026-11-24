// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Hiring-Signal Outbound ({@code hiring-signal-outbound}): Companies whose job posts reveal a
 * need for what you sell, with the decision-maker and a true line to open with.
 *
 * Only {@code signal_terms}, {@code locations} are required: {@link Builder#build()} refuses to build without them.
 * A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * HiringSignalOutboundParams params = HiringSignalOutboundParams.builder()
 *     .signalTerms("Snowflake", "dbt")
 *     .locations("United States")
 *     .build();
 * }</pre>
 */
public final class HiringSignalOutboundParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "hiring-signal-outbound";

    private final Map<String, Object> values;

    private HiringSignalOutboundParams(Map<String, Object> values) {
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
        return "HiringSignalOutboundParams" + values;
    }

    /** Builds {@link HiringSignalOutboundParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Roles, tools or phrases: What the postings must name. Each is searched on the boards and looked for in
         * the posting's text. 1 to 10 items. Required.
         *
         * @param signalTerms the values; not null
         * @return this builder
         */
        public Builder signalTerms(List<String> signalTerms) {
            values.put("signal_terms", List.copyOf(signalTerms));
            return this;
        }

        /**
         * Roles, tools or phrases: What the postings must name. Each is searched on the boards and looked for in
         * the posting's text. 1 to 10 items. Required.
         *
         * @param signalTerms the values
         * @return this builder
         */
        public Builder signalTerms(String... signalTerms) {
            return signalTerms(Arrays.asList(signalTerms));
        }

        /**
         * Locations: A city, region or country for each place to search. 1 to 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Locations: A city, region or country for each place to search. 1 to 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Signal: role: companies hiring these roles. technology: postings that name these tools. pain: postings
         * that describe these problems. growth: a department hiring several people. One of "role", "technology",
         * "pain", "growth". Default: "technology".
         *
         * @param signalType the value
         * @return this builder
         */
        public Builder signalType(String signalType) {
            values.put("signal_type", signalType);
            return this;
        }

        /**
         * Posted within (days): Only postings from the last this many days. From 1 to 30. Default: 7.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * Job boards: Which boards to search. Reed and Totaljobs cover the UK; Dice is technology roles. Each one
         * of "linkedin", "indeed", "dice", "reed", "totaljobs". At least 1 item. Default: ["linkedin", "indeed"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Job boards: Which boards to search. Reed and Totaljobs cover the UK; Dice is technology roles. Each one
         * of "linkedin", "indeed", "dice", "reed", "totaljobs". At least 1 item. Default: ["linkedin", "indeed"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Postings read per search: How many postings to read for each term, place and board. From 5 to 100.
         * Default: 25.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Postings read in full: How many of the newest postings are opened to read their whole text, where the
         * search gave only a title. From 5 to 300. Default: 60.
         *
         * @param postingsToRead the value
         * @return this builder
         */
        public Builder postingsToRead(int postingsToRead) {
            values.put("postings_to_read", postingsToRead);
            return this;
        }

        /**
         * Postings for growth: For the growth signal, how many postings one department of a company needs in the
         * window. From 2 to 50. Default: 3.
         *
         * @param growthMinPostings the value
         * @return this builder
         */
        public Builder growthMinPostings(int growthMinPostings) {
            values.put("growth_min_postings", growthMinPostings);
            return this;
        }

        /**
         * Company size (employees): Companies whose size is not known are kept. Default: {"min": 11, "max": 5000}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder companySize(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("company_size", Collections.unmodifiableMap(range));
            return this;
        }

        /**
         * Only these industries: Leave empty for any. Default: [].
         *
         * @param industriesInclude the values; not null
         * @return this builder
         */
        public Builder industriesInclude(List<String> industriesInclude) {
            values.put("industries_include", List.copyOf(industriesInclude));
            return this;
        }

        /**
         * Only these industries: Leave empty for any. Default: [].
         *
         * @param industriesInclude the values
         * @return this builder
         */
        public Builder industriesInclude(String... industriesInclude) {
            return industriesInclude(Arrays.asList(industriesInclude));
        }

        /**
         * Not these industries: Companies in these industries are left out. Default: [].
         *
         * @param industriesExclude the values; not null
         * @return this builder
         */
        public Builder industriesExclude(List<String> industriesExclude) {
            values.put("industries_exclude", List.copyOf(industriesExclude));
            return this;
        }

        /**
         * Not these industries: Companies in these industries are left out. Default: [].
         *
         * @param industriesExclude the values
         * @return this builder
         */
        public Builder industriesExclude(String... industriesExclude) {
            return industriesExclude(Arrays.asList(industriesExclude));
        }

        /**
         * Companies to leave out: Names or domains, such as customers and open deals. At most 5000 items. Default:
         * [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Companies to leave out: Names or domains, such as customers and open deals. At most 5000 items. Default:
         * [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Who to contact: The roles of the people to find. Leave empty to look for whoever decides for the role
         * being hired, such as the VP Sales for an SDR. Default: [].
         *
         * @param buyerTitles the values; not null
         * @return this builder
         */
        public Builder buyerTitles(List<String> buyerTitles) {
            values.put("buyer_titles", List.copyOf(buyerTitles));
            return this;
        }

        /**
         * Who to contact: The roles of the people to find. Leave empty to look for whoever decides for the role
         * being hired, such as the VP Sales for an SDR. Default: [].
         *
         * @param buyerTitles the values
         * @return this builder
         */
        public Builder buyerTitles(String... buyerTitles) {
            return buyerTitles(Arrays.asList(buyerTitles));
        }

        /**
         * Contact seniority: How senior the people to find should be. Each one of "manager", "director", "vp",
         * "c_level". Default: ["director", "vp", "c_level"].
         *
         * @param contactSeniority the values; not null
         * @return this builder
         */
        public Builder contactSeniority(List<String> contactSeniority) {
            values.put("contact_seniority", List.copyOf(contactSeniority));
            return this;
        }

        /**
         * Contact seniority: How senior the people to find should be. Each one of "manager", "director", "vp",
         * "c_level". Default: ["director", "vp", "c_level"].
         *
         * @param contactSeniority the values
         * @return this builder
         */
        public Builder contactSeniority(String... contactSeniority) {
            return contactSeniority(Arrays.asList(contactSeniority));
        }

        /**
         * Contacts per company: 0 turns contacts off. From 0 to 5. Default: 1.
         *
         * @param contactsPerCompany the value
         * @return this builder
         */
        public Builder contactsPerCompany(int contactsPerCompany) {
            values.put("contacts_per_company", contactsPerCompany);
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
         * Show a company again after (days): For a saved formula, how long before a company already delivered may
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
         * Most rows: The run stops at this many rows, best first. From 1 to 2000. Default: 100.
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
        public HiringSignalOutboundParams build() {
            if (!values.containsKey("signal_terms")) {
                throw new IllegalStateException("HiringSignalOutboundParams: signal_terms is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("HiringSignalOutboundParams: locations is required");
            }
            return new HiringSignalOutboundParams(values);
        }
    }
}
