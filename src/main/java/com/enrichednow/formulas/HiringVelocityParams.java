// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Hiring Velocity ({@code hiring-velocity}): Open roles at the companies you watch, counted by
 * department from their own careers sites, with the change since the last run.
 *
 * Only {@code companies} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * HiringVelocityParams params = HiringVelocityParams.builder()
 *     .companies("DUOL", "janestreet.com")
 *     .build();
 * }</pre>
 */
public final class HiringVelocityParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "hiring-velocity";

    private final Map<String, Object> values;

    private HiringVelocityParams(Map<String, Object> values) {
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
        return "HiringVelocityParams" + values;
    }

    /** Builds {@link HiringVelocityParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Companies: Each company as a stock ticker in capitals (DUOL), a domain (janestreet.com), or the address
         * of its careers page or job board (https://boards.greenhouse.io/wehrtyou). A job board address is the most
         * exact. 1 to 100 items. Required.
         *
         * @param companies the values; not null
         * @return this builder
         */
        public Builder companies(List<String> companies) {
            values.put("companies", List.copyOf(companies));
            return this;
        }

        /**
         * Companies: Each company as a stock ticker in capitals (DUOL), a domain (janestreet.com), or the address
         * of its careers page or job board (https://boards.greenhouse.io/wehrtyou). A job board address is the most
         * exact. 1 to 100 items. Required.
         *
         * @param companies the values
         * @return this builder
         */
        public Builder companies(String... companies) {
            return companies(Arrays.asList(companies));
        }

        /**
         * Recent means posted within (days): A role posted in the last this many days counts as a recent role. From
         * 1 to 90. Default: 30.
         *
         * @param recentDays the value
         * @return this builder
         */
        public Builder recentDays(int recentDays) {
            values.put("recent_days", recentDays);
            return this;
        }

        /**
         * Only these departments: Leave empty for every department. Each one of "engineering", "data", "research",
         * "trading", "investments", "risk", "product", "design", "sales", "marketing", "customer_support",
         * "finance", "legal", "people", "it", "operations", "healthcare", "education", "skilled_trades",
         * "retail_hospitality", "other". Default: [].
         *
         * @param departments the values; not null
         * @return this builder
         */
        public Builder departments(List<String> departments) {
            values.put("departments", List.copyOf(departments));
            return this;
        }

        /**
         * Only these departments: Leave empty for every department. Each one of "engineering", "data", "research",
         * "trading", "investments", "risk", "product", "design", "sales", "marketing", "customer_support",
         * "finance", "legal", "people", "it", "operations", "healthcare", "education", "skilled_trades",
         * "retail_hospitality", "other". Default: [].
         *
         * @param departments the values
         * @return this builder
         */
        public Builder departments(String... departments) {
            return departments(Arrays.asList(departments));
        }

        /**
         * Fewest open roles: A department with fewer open roles than this is left out. From 1 to 1000. Default: 1.
         *
         * @param minOpenRoles the value
         * @return this builder
         */
        public Builder minOpenRoles(int minOpenRoles) {
            values.put("min_open_roles", minOpenRoles);
            return this;
        }

        /**
         * Postings to leave out: Postings whose title holds one of these words are not counted; by default,
         * evergreen talent lists and recruiting events, which are not roles. At most 50 items. Default: a list of
         * 12 values, which {@code get} on the formula shows.
         *
         * @param excludeTitleWords the values; not null
         * @return this builder
         */
        public Builder excludeTitleWords(List<String> excludeTitleWords) {
            values.put("exclude_title_words", List.copyOf(excludeTitleWords));
            return this;
        }

        /**
         * Postings to leave out: Postings whose title holds one of these words are not counted; by default,
         * evergreen talent lists and recruiting events, which are not roles. At most 50 items. Default: a list of
         * 12 values, which {@code get} on the formula shows.
         *
         * @param excludeTitleWords the values
         * @return this builder
         */
        public Builder excludeTitleWords(String... excludeTitleWords) {
            return excludeTitleWords(Arrays.asList(excludeTitleWords));
        }

        /**
         * Count jobs read from the careers page itself: Off: only jobs from the company's applicant tracking system
         * are counted, which is exact. On: jobs read from the text of a careers page that has none are counted too;
         * such a page can list things that are not jobs, or show only some. Default: false.
         *
         * @param includePageReads the value
         * @return this builder
         */
        public Builder includePageReads(boolean includePageReads) {
            values.put("include_page_reads", includePageReads);
            return this;
        }

        /**
         * Most rows: The run stops at this many departments, biggest movers first. From 1 to 5000. Default: 500.
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
        public HiringVelocityParams build() {
            if (!values.containsKey("companies")) {
                throw new IllegalStateException("HiringVelocityParams: companies is required");
            }
            return new HiringVelocityParams(values);
        }
    }
}
