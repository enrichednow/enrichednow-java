// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Rank Tracker ({@code rank-tracker}): Your domain's and your competitors' Google Search
 * positions for each keyword, with the top results and what moved since last run.
 *
 * Only {@code keywords}, {@code your_domain} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * RankTrackerParams params = RankTrackerParams.builder()
 *     .keywords("crm software", "best crm for small business")
 *     .yourDomain("pipedrive.com")
 *     .build();
 * }</pre>
 */
public final class RankTrackerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "rank-tracker";

    private final Map<String, Object> values;

    private RankTrackerParams(Map<String, Object> values) {
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
        return "RankTrackerParams" + values;
    }

    /** Builds {@link RankTrackerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Keywords: The searches to track, each as people type it. 1 to 100 items. Required.
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Keywords: The searches to track, each as people type it. 1 to 100 items. Required.
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Your domain: Your site's domain, such as example.com; its subdomains count as it. Required.
         *
         * @param yourDomain the value
         * @return this builder
         */
        public Builder yourDomain(String yourDomain) {
            values.put("your_domain", yourDomain);
            return this;
        }

        /**
         * Competitors' domains: Up to ten domains, such as rival.com, each tracked for every keyword. At most 10
         * items. Default: [].
         *
         * @param competitors the values; not null
         * @return this builder
         */
        public Builder competitors(List<String> competitors) {
            values.put("competitors", List.copyOf(competitors));
            return this;
        }

        /**
         * Competitors' domains: Up to ten domains, such as rival.com, each tracked for every keyword. At most 10
         * items. Default: [].
         *
         * @param competitors the values
         * @return this builder
         */
        public Builder competitors(String... competitors) {
            return competitors(Arrays.asList(competitors));
        }

        /**
         * Country: The two-letter country whose results are read, such as us, gb or de. Default: "us".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Results pages read: 1 reads the top 10 results of each keyword, 2 the top 20, 3 the top 30; each page is
         * one search. From 1 to 3. Default: 1.
         *
         * @param pages the value
         * @return this builder
         */
        public Builder pages(int pages) {
            values.put("pages", pages);
            return this;
        }

        /**
         * Language: The language of the results, such as en or de. Default: "en".
         *
         * @param language the value
         * @return this builder
         */
        public Builder language(String language) {
            values.put("language", language);
            return this;
        }

        /**
         * Top results shown: How many of the domains at the top of each keyword's results are listed on its rows.
         * From 0 to 10. Default: 3.
         *
         * @param topResults the value
         * @return this builder
         */
        public Builder topResults(int topResults) {
            values.put("top_results", topResults);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, one for each keyword and domain. From 1 to 1100. Default:
         * 500.
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
        public RankTrackerParams build() {
            if (!values.containsKey("keywords")) {
                throw new IllegalStateException("RankTrackerParams: keywords is required");
            }
            if (!values.containsKey("your_domain")) {
                throw new IllegalStateException("RankTrackerParams: your_domain is required");
            }
            return new RankTrackerParams(values);
        }
    }
}
