// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Job Order Hunter ({@code job-order-hunter}): Companies newly hiring for your roles and places,
 * qualified by size and industry, with the person to contact.
 *
 * Only {@code job_titles}, {@code locations} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * JobOrderHunterParams params = JobOrderHunterParams.builder()
 *     .jobTitles("Java Developer", "DevOps Engineer")
 *     .locations("London")
 *     .build();
 * }</pre>
 */
public final class JobOrderHunterParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "job-order-hunter";

    private final Map<String, Object> values;

    private JobOrderHunterParams(Map<String, Object> values) {
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
        return "JobOrderHunterParams" + values;
    }

    /** Builds {@link JobOrderHunterParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Job titles: The roles you recruit for, each searched as you type it. 1 to 10 items. Required.
         *
         * @param jobTitles the values; not null
         * @return this builder
         */
        public Builder jobTitles(List<String> jobTitles) {
            values.put("job_titles", List.copyOf(jobTitles));
            return this;
        }

        /**
         * Job titles: The roles you recruit for, each searched as you type it. 1 to 10 items. Required.
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
         * Posted within (days): Only jobs posted in the last this many days. From 1 to 30. Default: 1.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * Job boards: Which boards to search. The careers pages of target companies are read when you choose them
         * and name the companies. Each one of "linkedin", "indeed", "google_jobs", "dice", "reed", "totaljobs",
         * "stepstone", "seek", "simplyhired", "wellfound", "xing", "irishjobs", "ycombinator", "ats_boards". At
         * least 1 item. Default: ["linkedin", "indeed", "google_jobs"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Job boards: Which boards to search. The careers pages of target companies are read when you choose them
         * and name the companies. Each one of "linkedin", "indeed", "google_jobs", "dice", "reed", "totaljobs",
         * "stepstone", "seek", "simplyhired", "wellfound", "xing", "irishjobs", "ycombinator", "ats_boards". At
         * least 1 item. Default: ["linkedin", "indeed", "google_jobs"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Search other names of the same job: Also searches the names the same job is often advertised under, such
         * as RN for Registered Nurse. Default: false.
         *
         * @param titleSynonyms the value
         * @return this builder
         */
        public Builder titleSynonyms(boolean titleSynonyms) {
            values.put("title_synonyms", titleSynonyms);
            return this;
        }

        /**
         * Target companies: Domains or careers pages whose own openings are read when Careers pages is chosen as a
         * board. At most 100 items. Default: [].
         *
         * @param targetCompanies the values; not null
         * @return this builder
         */
        public Builder targetCompanies(List<String> targetCompanies) {
            values.put("target_companies", List.copyOf(targetCompanies));
            return this;
        }

        /**
         * Target companies: Domains or careers pages whose own openings are read when Careers pages is chosen as a
         * board. At most 100 items. Default: [].
         *
         * @param targetCompanies the values
         * @return this builder
         */
        public Builder targetCompanies(String... targetCompanies) {
            return targetCompanies(Arrays.asList(targetCompanies));
        }

        /**
         * Jobs read per search: How many jobs to read for each title, place and board. From 5 to 200. Default: 50.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Employment types: Leave empty for any. Each one of "full_time", "part_time", "contract", "temporary".
         * Default: [].
         *
         * @param employmentTypes the values; not null
         * @return this builder
         */
        public Builder employmentTypes(List<String> employmentTypes) {
            values.put("employment_types", List.copyOf(employmentTypes));
            return this;
        }

        /**
         * Employment types: Leave empty for any. Each one of "full_time", "part_time", "contract", "temporary".
         * Default: [].
         *
         * @param employmentTypes the values
         * @return this builder
         */
        public Builder employmentTypes(String... employmentTypes) {
            return employmentTypes(Arrays.asList(employmentTypes));
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
         * Leave out staffing agencies: Recruiters' own postings are rarely a job order. Default: true.
         *
         * @param excludeStaffingAgencies the value
         * @return this builder
         */
        public Builder excludeStaffingAgencies(boolean excludeStaffingAgencies) {
            values.put("exclude_staffing_agencies", excludeStaffingAgencies);
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
         * Least open roles: Matching roles a company needs to have open. At least 1. Default: 1.
         *
         * @param minOpenRoles the value
         * @return this builder
         */
        public Builder minOpenRoles(int minOpenRoles) {
            values.put("min_open_roles", minOpenRoles);
            return this;
        }

        /**
         * Words to leave out of titles: For example intern or unpaid. Default: [].
         *
         * @param titleExcludeKeywords the values; not null
         * @return this builder
         */
        public Builder titleExcludeKeywords(List<String> titleExcludeKeywords) {
            values.put("title_exclude_keywords", List.copyOf(titleExcludeKeywords));
            return this;
        }

        /**
         * Words to leave out of titles: For example intern or unpaid. Default: [].
         *
         * @param titleExcludeKeywords the values
         * @return this builder
         */
        public Builder titleExcludeKeywords(String... titleExcludeKeywords) {
            return titleExcludeKeywords(Arrays.asList(titleExcludeKeywords));
        }

        /**
         * Check their careers pages: Reads the careers page of the companies with the most matching roles; a role
         * listed there ranks higher. Default: true.
         *
         * @param checkCareersPages the value
         * @return this builder
         */
        public Builder checkCareersPages(boolean checkCareersPages) {
            values.put("check_careers_pages", checkCareersPages);
            return this;
        }

        /**
         * Careers pages to read: How many companies' careers pages to read, those with the most matching roles
         * first. From 1 to 100. Default: 25.
         *
         * @param careersPagesMax the value
         * @return this builder
         */
        public Builder careersPagesMax(int careersPagesMax) {
            values.put("careers_pages_max", careersPagesMax);
            return this;
        }

        /**
         * Who to contact: The roles of the people to find at each company. Default: ["Talent Acquisition", "HR
         * Manager", "Hiring Manager"].
         *
         * @param contactTitles the values; not null
         * @return this builder
         */
        public Builder contactTitles(List<String> contactTitles) {
            values.put("contact_titles", List.copyOf(contactTitles));
            return this;
        }

        /**
         * Who to contact: The roles of the people to find at each company. Default: ["Talent Acquisition", "HR
         * Manager", "Hiring Manager"].
         *
         * @param contactTitles the values
         * @return this builder
         */
        public Builder contactTitles(String... contactTitles) {
            return contactTitles(Arrays.asList(contactTitles));
        }

        /**
         * Contact seniority: How senior the people to find should be. Each one of "manager", "director", "vp",
         * "c_level". Default: ["manager", "director", "vp", "c_level"].
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
         * "c_level". Default: ["manager", "director", "vp", "c_level"].
         *
         * @param contactSeniority the values
         * @return this builder
         */
        public Builder contactSeniority(String... contactSeniority) {
            return contactSeniority(Arrays.asList(contactSeniority));
        }

        /**
         * Contacts per company: 0 turns contacts off. From 0 to 5. Default: 2.
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
         * Show a company again after (days): For a saved formula, how long before a delivered row may come back.
         * From 1 to 365. Default: 45.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, best first. From 1 to 5000. Default: 200.
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
        public JobOrderHunterParams build() {
            if (!values.containsKey("job_titles")) {
                throw new IllegalStateException("JobOrderHunterParams: job_titles is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("JobOrderHunterParams: locations is required");
            }
            return new JobOrderHunterParams(values);
        }
    }
}
