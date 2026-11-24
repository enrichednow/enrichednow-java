// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Job Seeker Targets ({@code job-seeker-targets}): Employers hiring now for the job you want,
 * with the person who decides, their address and a first line to send.
 *
 * Only {@code target_titles}, {@code locations} are required: {@link Builder#build()} refuses to build without
 * them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * JobSeekerTargetsParams params = JobSeekerTargetsParams.builder()
 *     .targetTitles("Product Manager")
 *     .locations("London")
 *     .build();
 * }</pre>
 */
public final class JobSeekerTargetsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "job-seeker-targets";

    private final Map<String, Object> values;

    private JobSeekerTargetsParams(Map<String, Object> values) {
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
        return "JobSeekerTargetsParams" + values;
    }

    /** Builds {@link JobSeekerTargetsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Jobs you want: The job titles you are applying for, each searched as you type it. 1 to 5 items. Required.
         *
         * @param targetTitles the values; not null
         * @return this builder
         */
        public Builder targetTitles(List<String> targetTitles) {
            values.put("target_titles", List.copyOf(targetTitles));
            return this;
        }

        /**
         * Jobs you want: The job titles you are applying for, each searched as you type it. 1 to 5 items. Required.
         *
         * @param targetTitles the values
         * @return this builder
         */
        public Builder targetTitles(String... targetTitles) {
            return targetTitles(Arrays.asList(targetTitles));
        }

        /**
         * Where: A city, region or country for each place you would work, or Remote. 1 to 5 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Where: A city, region or country for each place you would work, or Remote. 1 to 5 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Posted within (days): Only postings from the last this many days, while the role is still open. From 1 to
         * 30. Default: 3.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * Job boards: Which boards to search. Reed and Totaljobs cover the UK; Dice is technology roles in the US.
         * Each one of "linkedin", "indeed", "reed", "totaljobs", "dice". At least 1 item. Default: ["linkedin",
         * "indeed"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Job boards: Which boards to search. Reed and Totaljobs cover the UK; Dice is technology roles in the US.
         * Each one of "linkedin", "indeed", "reed", "totaljobs", "dice". At least 1 item. Default: ["linkedin",
         * "indeed"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Jobs read per search: How many postings to read for each job, place and board. From 5 to 100. Default:
         * 25.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Company size (employees): Companies whose size is not known are kept. Default: {"min": 1, "max": 100000}.
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
         * Employers to leave out: Names or domains, such as your current employer or places you already applied to.
         * At most 2000 items. Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Employers to leave out: Names or domains, such as your current employer or places you already applied to.
         * At most 2000 items. Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Only titles that name the job: Keeps postings whose title names one of your jobs as whole words; boards
         * also return near matches. Default: true.
         *
         * @param titleMustMatch the value
         * @return this builder
         */
        public Builder titleMustMatch(boolean titleMustMatch) {
            values.put("title_must_match", titleMustMatch);
            return this;
        }

        /**
         * Words to leave out of titles: For example intern, senior or contract. Default: [].
         *
         * @param titleExcludeKeywords the values; not null
         * @return this builder
         */
        public Builder titleExcludeKeywords(List<String> titleExcludeKeywords) {
            values.put("title_exclude_keywords", List.copyOf(titleExcludeKeywords));
            return this;
        }

        /**
         * Words to leave out of titles: For example intern, senior or contract. Default: [].
         *
         * @param titleExcludeKeywords the values
         * @return this builder
         */
        public Builder titleExcludeKeywords(String... titleExcludeKeywords) {
            return titleExcludeKeywords(Arrays.asList(titleExcludeKeywords));
        }

        /**
         * Who to write to: Leave empty to look for whoever decides on that kind of hire, such as the CTO for a
         * developer. Default: [].
         *
         * @param contactTitles the values; not null
         * @return this builder
         */
        public Builder contactTitles(List<String> contactTitles) {
            values.put("contact_titles", List.copyOf(contactTitles));
            return this;
        }

        /**
         * Who to write to: Leave empty to look for whoever decides on that kind of hire, such as the CTO for a
         * developer. Default: [].
         *
         * @param contactTitles the values
         * @return this builder
         */
        public Builder contactTitles(String... contactTitles) {
            return contactTitles(Arrays.asList(contactTitles));
        }

        /**
         * People per employer: 0 turns contacts off. From 0 to 3. Default: 1.
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
         * Most rows: The run stops at this many rows, newest postings first. From 1 to 500. Default: 50.
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
        public JobSeekerTargetsParams build() {
            if (!values.containsKey("target_titles")) {
                throw new IllegalStateException("JobSeekerTargetsParams: target_titles is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("JobSeekerTargetsParams: locations is required");
            }
            return new JobSeekerTargetsParams(values);
        }
    }
}
