// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Salary Benchmark ({@code salary-benchmark}): The pay employers advertise for a job in each
 * place, as yearly ranges per place, with every posting behind them.
 *
 * Only {@code job_titles}, {@code locations} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * SalaryBenchmarkParams params = SalaryBenchmarkParams.builder()
 *     .jobTitles("Java Developer")
 *     .locations("London", "Manchester")
 *     .build();
 * }</pre>
 */
public final class SalaryBenchmarkParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "salary-benchmark";

    private final Map<String, Object> values;

    private SalaryBenchmarkParams(Map<String, Object> values) {
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
        return "SalaryBenchmarkParams" + values;
    }

    /** Builds {@link SalaryBenchmarkParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Job: The job to benchmark, and up to two other names it is advertised under. 1 to 3 items. Required.
         *
         * @param jobTitles the values; not null
         * @return this builder
         */
        public Builder jobTitles(List<String> jobTitles) {
            values.put("job_titles", List.copyOf(jobTitles));
            return this;
        }

        /**
         * Job: The job to benchmark, and up to two other names it is advertised under. 1 to 3 items. Required.
         *
         * @param jobTitles the values
         * @return this builder
         */
        public Builder jobTitles(String... jobTitles) {
            return jobTitles(Arrays.asList(jobTitles));
        }

        /**
         * Places: Each city, region or country to compare. 1 to 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Places: Each city, region or country to compare. 1 to 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Posted within (days): Only postings from the last this many days, so the figures are today's. From 1 to
         * 60. Default: 30.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * Job boards: Which boards to read. Reed and Totaljobs cover the UK (Reed states pay on most postings),
         * Dice technology roles in the US, Seek Australia, StepStone Germany. Each one of "indeed", "linkedin",
         * "reed", "totaljobs", "dice", "seek", "stepstone". At least 1 item. Default: ["indeed", "linkedin"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Job boards: Which boards to read. Reed and Totaljobs cover the UK (Reed states pay on most postings),
         * Dice technology roles in the US, Seek Australia, StepStone Germany. Each one of "indeed", "linkedin",
         * "reed", "totaljobs", "dice", "seek", "stepstone". At least 1 item. Default: ["indeed", "linkedin"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Jobs read per search: How many postings to read for each job name, place and board; many do not state
         * pay. From 5 to 200. Default: 50.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Only titles that name the job: Keeps postings whose title names the job as whole words; boards also
         * return near matches, which would blur the figures. Default: true.
         *
         * @param titleMustMatch the value
         * @return this builder
         */
        public Builder titleMustMatch(boolean titleMustMatch) {
            values.put("title_must_match", titleMustMatch);
            return this;
        }

        /**
         * Leave out agencies' postings: Agencies often advertise a wide range; leave them out to see only what
         * employers state. Default: false.
         *
         * @param excludeAgencies the value
         * @return this builder
         */
        public Builder excludeAgencies(boolean excludeAgencies) {
            values.put("exclude_agencies", excludeAgencies);
            return this;
        }

        /**
         * Companies to add from Levels.fyi: Company names as Levels.fyi writes them; their pay for the job is added
         * by level, from what people reported. At most 20 items. Default: [].
         *
         * @param levelsCompanies the values; not null
         * @return this builder
         */
        public Builder levelsCompanies(List<String> levelsCompanies) {
            values.put("levels_companies", List.copyOf(levelsCompanies));
            return this;
        }

        /**
         * Companies to add from Levels.fyi: Company names as Levels.fyi writes them; their pay for the job is added
         * by level, from what people reported. At most 20 items. Default: [].
         *
         * @param levelsCompanies the values
         * @return this builder
         */
        public Builder levelsCompanies(String... levelsCompanies) {
            return levelsCompanies(Arrays.asList(levelsCompanies));
        }

        /**
         * The job as Levels.fyi names it: Such as Software Engineer or Data Scientist. Empty uses the first job
         * name. Default: "".
         *
         * @param levelsRole the value
         * @return this builder
         */
        public Builder levelsRole(String levelsRole) {
            values.put("levels_role", levelsRole);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, best paid first. From 1 to 2000. Default: 200.
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
        public SalaryBenchmarkParams build() {
            if (!values.containsKey("job_titles")) {
                throw new IllegalStateException("SalaryBenchmarkParams: job_titles is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("SalaryBenchmarkParams: locations is required");
            }
            return new SalaryBenchmarkParams(values);
        }
    }
}
