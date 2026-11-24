// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Candidate Reverse Marketing ({@code candidate-reverse-marketing}): Companies hiring for what
 * your best candidate does, with the skills they ask for, who decides, and an opening line.
 *
 * Only {@code candidate_titles}, {@code skills}, {@code locations} are required: {@link Builder#build()} refuses to
 * build without them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * CandidateReverseMarketingParams params = CandidateReverseMarketingParams.builder()
 *     .candidateTitles("Java Developer")
 *     .skills("Java", "Spring Boot")
 *     .locations("London")
 *     .build();
 * }</pre>
 */
public final class CandidateReverseMarketingParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "candidate-reverse-marketing";

    private final Map<String, Object> values;

    private CandidateReverseMarketingParams(Map<String, Object> values) {
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
        return "CandidateReverseMarketingParams" + values;
    }

    /** Builds {@link CandidateReverseMarketingParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Roles the candidate fits: The job titles your candidate would be hired as, each searched as you type it.
         * 1 to 5 items. Required.
         *
         * @param candidateTitles the values; not null
         * @return this builder
         */
        public Builder candidateTitles(List<String> candidateTitles) {
            values.put("candidate_titles", List.copyOf(candidateTitles));
            return this;
        }

        /**
         * Roles the candidate fits: The job titles your candidate would be hired as, each searched as you type it.
         * 1 to 5 items. Required.
         *
         * @param candidateTitles the values
         * @return this builder
         */
        public Builder candidateTitles(String... candidateTitles) {
            return candidateTitles(Arrays.asList(candidateTitles));
        }

        /**
         * The candidate's skills: Tools, methods or certificates the candidate has. A posting is kept when it asks
         * for enough of them. 1 to 15 items. Required.
         *
         * @param skills the values; not null
         * @return this builder
         */
        public Builder skills(List<String> skills) {
            values.put("skills", List.copyOf(skills));
            return this;
        }

        /**
         * The candidate's skills: Tools, methods or certificates the candidate has. A posting is kept when it asks
         * for enough of them. 1 to 15 items. Required.
         *
         * @param skills the values
         * @return this builder
         */
        public Builder skills(String... skills) {
            return skills(Arrays.asList(skills));
        }

        /**
         * Where they will work: A city, region or country for each place the candidate would work. 1 to 5 items.
         * Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Where they will work: A city, region or country for each place the candidate would work. 1 to 5 items.
         * Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Posted within (days): Only postings from the last this many days; a fresh posting is still unfilled. From
         * 1 to 30. Default: 7.
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
         * Jobs read per search: How many postings to read for each role, place and board. From 5 to 100. Default:
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
         * Least skills a posting must ask for: Of the candidate's skills, how many a posting has to name to be
         * kept. From 1 to 10. Default: 2.
         *
         * @param minSkillsMatched the value
         * @return this builder
         */
        public Builder minSkillsMatched(int minSkillsMatched) {
            values.put("min_skills_matched", minSkillsMatched);
            return this;
        }

        /**
         * Postings read in full: The newest postings whose full text is read to match skills; each costs a unit
         * when the board gave only a summary. From 10 to 300. Default: 80.
         *
         * @param postingsRead the value
         * @return this builder
         */
        public Builder postingsRead(int postingsRead) {
            values.put("postings_read", postingsRead);
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
         * Companies to leave out: Names or domains, such as the candidate's current employer or companies they
         * already applied to. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Companies to leave out: Names or domains, such as the candidate's current employer or companies they
         * already applied to. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Words to leave out of titles: For example junior or intern. Default: [].
         *
         * @param titleExcludeKeywords the values; not null
         * @return this builder
         */
        public Builder titleExcludeKeywords(List<String> titleExcludeKeywords) {
            values.put("title_exclude_keywords", List.copyOf(titleExcludeKeywords));
            return this;
        }

        /**
         * Words to leave out of titles: For example junior or intern. Default: [].
         *
         * @param titleExcludeKeywords the values
         * @return this builder
         */
        public Builder titleExcludeKeywords(String... titleExcludeKeywords) {
            return titleExcludeKeywords(Arrays.asList(titleExcludeKeywords));
        }

        /**
         * Who to contact: Leave empty to look for whoever decides on that kind of hire, such as the VP Engineering
         * for a developer. Default: [].
         *
         * @param contactTitles the values; not null
         * @return this builder
         */
        public Builder contactTitles(List<String> contactTitles) {
            values.put("contact_titles", List.copyOf(contactTitles));
            return this;
        }

        /**
         * Who to contact: Leave empty to look for whoever decides on that kind of hire, such as the VP Engineering
         * for a developer. Default: [].
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
         * Most rows: The run stops at this many rows, best first. From 1 to 1000. Default: 50.
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
        public CandidateReverseMarketingParams build() {
            if (!values.containsKey("candidate_titles")) {
                throw new IllegalStateException("CandidateReverseMarketingParams: candidate_titles is required");
            }
            if (!values.containsKey("skills")) {
                throw new IllegalStateException("CandidateReverseMarketingParams: skills is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("CandidateReverseMarketingParams: locations is required");
            }
            return new CandidateReverseMarketingParams(values);
        }
    }
}
