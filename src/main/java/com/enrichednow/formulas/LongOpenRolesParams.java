// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Long-Open Roles ({@code long-open-roles}): Jobs open for a month or more at companies that are
 * struggling to fill them, with who decides on the hire.
 *
 * Only {@code job_titles}, {@code locations} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * LongOpenRolesParams params = LongOpenRolesParams.builder()
 *     .jobTitles("Registered Nurse")
 *     .locations("Houston", "TX")
 *     .build();
 * }</pre>
 */
public final class LongOpenRolesParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "long-open-roles";

    private final Map<String, Object> values;

    private LongOpenRolesParams(Map<String, Object> values) {
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
        return "LongOpenRolesParams" + values;
    }

    /** Builds {@link LongOpenRolesParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Job titles: The roles you place, each searched as you type it. 1 to 10 items. Required.
         *
         * @param jobTitles the values; not null
         * @return this builder
         */
        public Builder jobTitles(List<String> jobTitles) {
            values.put("job_titles", List.copyOf(jobTitles));
            return this;
        }

        /**
         * Job titles: The roles you place, each searched as you type it. 1 to 10 items. Required.
         *
         * @param jobTitles the values
         * @return this builder
         */
        public Builder jobTitles(String... jobTitles) {
            return jobTitles(Arrays.asList(jobTitles));
        }

        /**
         * Locations: A city, region or country for each place you cover. 1 to 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Locations: A city, region or country for each place you cover. 1 to 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Open at least (days): Only jobs that have been posted at least this many days. From 7 to 180. Default:
         * 30.
         *
         * @param minDaysOpen the value
         * @return this builder
         */
        public Builder minDaysOpen(int minDaysOpen) {
            values.put("min_days_open", minDaysOpen);
            return this;
        }

        /**
         * Open at most (days): Older postings are often forgotten rather than open; they are left out. From 14 to
         * 365. Default: 120.
         *
         * @param maxDaysOpen the value
         * @return this builder
         */
        public Builder maxDaysOpen(int maxDaysOpen) {
            values.put("max_days_open", maxDaysOpen);
            return this;
        }

        /**
         * Job boards: Which boards to search. Indeed lists jobs of the last 30 days only. Reed and Totaljobs cover
         * the UK; Dice is technology roles in the US. Each one of "linkedin", "indeed", "reed", "totaljobs",
         * "dice". At least 1 item. Default: ["linkedin", "indeed"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Job boards: Which boards to search. Indeed lists jobs of the last 30 days only. Reed and Totaljobs cover
         * the UK; Dice is technology roles in the US. Each one of "linkedin", "indeed", "reed", "totaljobs",
         * "dice". At least 1 item. Default: ["linkedin", "indeed"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Jobs read per search: How many postings to read for each title, place and board; most are newer than you
         * want, so read more. From 5 to 200. Default: 50.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Company size (employees): Companies whose size is not known are kept. Default: {"min": 11, "max": 10000}.
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
         * Companies to leave out: Names or domains, such as your existing clients. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Companies to leave out: Names or domains, such as your existing clients. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Words to leave out of titles: For example intern or volunteer. Default: [].
         *
         * @param titleExcludeKeywords the values; not null
         * @return this builder
         */
        public Builder titleExcludeKeywords(List<String> titleExcludeKeywords) {
            values.put("title_exclude_keywords", List.copyOf(titleExcludeKeywords));
            return this;
        }

        /**
         * Words to leave out of titles: For example intern or volunteer. Default: [].
         *
         * @param titleExcludeKeywords the values
         * @return this builder
         */
        public Builder titleExcludeKeywords(String... titleExcludeKeywords) {
            return titleExcludeKeywords(Arrays.asList(titleExcludeKeywords));
        }

        /**
         * Who to contact: Leave empty to look for whoever decides on that kind of hire, such as the Director of
         * Nursing for a nurse. Default: [].
         *
         * @param contactTitles the values; not null
         * @return this builder
         */
        public Builder contactTitles(List<String> contactTitles) {
            values.put("contact_titles", List.copyOf(contactTitles));
            return this;
        }

        /**
         * Who to contact: Leave empty to look for whoever decides on that kind of hire, such as the Director of
         * Nursing for a nurse. Default: [].
         *
         * @param contactTitles the values
         * @return this builder
         */
        public Builder contactTitles(String... contactTitles) {
            return contactTitles(Arrays.asList(contactTitles));
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
         * Most rows: The run stops at this many rows, the longest-open first. From 1 to 2000. Default: 100.
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
        public LongOpenRolesParams build() {
            if (!values.containsKey("job_titles")) {
                throw new IllegalStateException("LongOpenRolesParams: job_titles is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("LongOpenRolesParams: locations is required");
            }
            return new LongOpenRolesParams(values);
        }
    }
}
