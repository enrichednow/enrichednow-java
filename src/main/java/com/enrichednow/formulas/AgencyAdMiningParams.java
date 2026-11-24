// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Agency Ad Mining ({@code agency-ad-mining}): Jobs competing agencies advertise, what they say
 * about the client, and the employers posting the same job directly.
 *
 * Only {@code job_titles}, {@code locations} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * AgencyAdMiningParams params = AgencyAdMiningParams.builder()
 *     .jobTitles("Java Developer")
 *     .locations("London")
 *     .build();
 * }</pre>
 */
public final class AgencyAdMiningParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "agency-ad-mining";

    private final Map<String, Object> values;

    private AgencyAdMiningParams(Map<String, Object> values) {
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
        return "AgencyAdMiningParams" + values;
    }

    /** Builds {@link AgencyAdMiningParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Job titles: The roles you work, each searched as you type it. 1 to 10 items. Required.
         *
         * @param jobTitles the values; not null
         * @return this builder
         */
        public Builder jobTitles(List<String> jobTitles) {
            values.put("job_titles", List.copyOf(jobTitles));
            return this;
        }

        /**
         * Job titles: The roles you work, each searched as you type it. 1 to 10 items. Required.
         *
         * @param jobTitles the values
         * @return this builder
         */
        public Builder jobTitles(String... jobTitles) {
            return jobTitles(Arrays.asList(jobTitles));
        }

        /**
         * Locations: A city, region or country for each market you watch. 1 to 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Locations: A city, region or country for each market you watch. 1 to 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
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
         * Only these agencies: Names of the competing agencies to watch. Leave empty for every agency. At most 50
         * items. Default: [].
         *
         * @param agencies the values; not null
         * @return this builder
         */
        public Builder agencies(List<String> agencies) {
            values.put("agencies", List.copyOf(agencies));
            return this;
        }

        /**
         * Only these agencies: Names of the competing agencies to watch. Leave empty for every agency. At most 50
         * items. Default: [].
         *
         * @param agencies the values
         * @return this builder
         */
        public Builder agencies(String... agencies) {
            return agencies(Arrays.asList(agencies));
        }

        /**
         * Job boards: Which boards to search. Reed and Dice say who placed a posting; Reed and Totaljobs cover the
         * UK, Dice technology roles in the US. Each one of "linkedin", "indeed", "reed", "totaljobs", "dice". At
         * least 1 item. Default: ["linkedin", "reed", "dice"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Job boards: Which boards to search. Reed and Dice say who placed a posting; Reed and Totaljobs cover the
         * UK, Dice technology roles in the US. Each one of "linkedin", "indeed", "reed", "totaljobs", "dice". At
         * least 1 item. Default: ["linkedin", "reed", "dice"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Jobs read per search: How many postings to read for each title, place and board, agencies' and employers'
         * alike. From 5 to 200. Default: 50.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Agency postings read in full: The newest agency postings whose full text is read for what they say about
         * the client. From 5 to 300. Default: 60.
         *
         * @param postingsRead the value
         * @return this builder
         */
        public Builder postingsRead(int postingsRead) {
            values.put("postings_read", postingsRead);
            return this;
        }

        /**
         * Words that describe the client: The first of these a posting uses is quoted with the sentence around it.
         * Default: ["our client", "my client", "on behalf of", "client is", "client based in", "working with a", "a
         * leading", "well-established"].
         *
         * @param clientPhrases the values; not null
         * @return this builder
         */
        public Builder clientPhrases(List<String> clientPhrases) {
            values.put("client_phrases", List.copyOf(clientPhrases));
            return this;
        }

        /**
         * Words that describe the client: The first of these a posting uses is quoted with the sentence around it.
         * Default: ["our client", "my client", "on behalf of", "client is", "client based in", "working with a", "a
         * leading", "well-established"].
         *
         * @param clientPhrases the values
         * @return this builder
         */
        public Builder clientPhrases(String... clientPhrases) {
            return clientPhrases(Arrays.asList(clientPhrases));
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
        public AgencyAdMiningParams build() {
            if (!values.containsKey("job_titles")) {
                throw new IllegalStateException("AgencyAdMiningParams: job_titles is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("AgencyAdMiningParams: locations is required");
            }
            return new AgencyAdMiningParams(values);
        }
    }
}
