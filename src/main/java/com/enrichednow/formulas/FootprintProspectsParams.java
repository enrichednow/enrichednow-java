// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Footprint Prospects ({@code footprint-prospects}): Sites on your topic that take guest posts or
 * keep resource pages, found by search footprints, each with its email.
 *
 * Only {@code topic} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * FootprintProspectsParams params = FootprintProspectsParams.builder()
 *     .topic("gardening")
 *     .build();
 * }</pre>
 */
public final class FootprintProspectsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "footprint-prospects";

    private final Map<String, Object> values;

    private FootprintProspectsParams(Map<String, Object> values) {
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
        return "FootprintProspectsParams" + values;
    }

    /** Builds {@link FootprintProspectsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Topic: The subject, as people search for it, such as gardening or personal finance. Required.
         *
         * @param topic the value
         * @return this builder
         */
        public Builder topic(String topic) {
            values.put("topic", topic);
            return this;
        }

        /**
         * Footprints: Words searched beside the topic, each its own search. Quote a phrase to find it exactly.
         * Search operators such as intitle: work too. 1 to 20 items. Default: ["\"write for us\"", "\"guest post
         * guidelines\"", "\"submit a guest post\"", "\"become a contributor\"", "\"accepting guest posts\"",
         * "\"contribute an article\""].
         *
         * @param footprints the values; not null
         * @return this builder
         */
        public Builder footprints(List<String> footprints) {
            values.put("footprints", List.copyOf(footprints));
            return this;
        }

        /**
         * Footprints: Words searched beside the topic, each its own search. Quote a phrase to find it exactly.
         * Search operators such as intitle: work too. 1 to 20 items. Default: ["\"write for us\"", "\"guest post
         * guidelines\"", "\"submit a guest post\"", "\"become a contributor\"", "\"accepting guest posts\"",
         * "\"contribute an article\""].
         *
         * @param footprints the values
         * @return this builder
         */
        public Builder footprints(String... footprints) {
            return footprints(Arrays.asList(footprints));
        }

        /**
         * Results pages per search: How many pages of results to read for each footprint, about ten results a page.
         * From 1 to 3. Default: 1.
         *
         * @param searchPages the value
         * @return this builder
         */
        public Builder searchPages(int searchPages) {
            values.put("search_pages", searchPages);
            return this;
        }

        /**
         * Sites to leave out: Domains never shown, such as social networks, marketplaces and your own sites. At
         * most 2000 items. Default: a list of 21 values, which {@code get} on the formula shows.
         *
         * @param excludeDomains the values; not null
         * @return this builder
         */
        public Builder excludeDomains(List<String> excludeDomains) {
            values.put("exclude_domains", List.copyOf(excludeDomains));
            return this;
        }

        /**
         * Sites to leave out: Domains never shown, such as social networks, marketplaces and your own sites. At
         * most 2000 items. Default: a list of 21 values, which {@code get} on the formula shows.
         *
         * @param excludeDomains the values
         * @return this builder
         */
        public Builder excludeDomains(String... excludeDomains) {
            return excludeDomains(Arrays.asList(excludeDomains));
        }

        /**
         * Search country: The two-letter country whose search results are read, such as us, gb or de. Default:
         * "us".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Search language: The language of the results, such as en or de. Default: "en".
         *
         * @param language the value
         * @return this builder
         */
        public Builder language(String language) {
            values.put("language", language);
            return this;
        }

        /**
         * Read each page for its author: Reads each page found as an article, for its author and the day it was
         * published. Default: false.
         *
         * @param readArticles the value
         * @return this builder
         */
        public Builder readArticles(boolean readArticles) {
            values.put("read_articles", readArticles);
            return this;
        }

        /**
         * Only pages with a named author: When pages are read, keeps the sites whose page names who wrote it.
         * Default: false.
         *
         * @param requireAuthor the value
         * @return this builder
         */
        public Builder requireAuthor(boolean requireAuthor) {
            values.put("require_author", requireAuthor);
            return this;
        }

        /**
         * Only sites with an email: Keeps the sites whose own website publishes an email address that passes the
         * check. Default: false.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Show a site again after (days): For a saved formula, how long before a delivered site may come back. From
         * 1 to 365. Default: 90.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many sites, those matching the most footprints first. From 1 to 1000.
         * Default: 100.
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
        public FootprintProspectsParams build() {
            if (!values.containsKey("topic")) {
                throw new IllegalStateException("FootprintProspectsParams: topic is required");
            }
            return new FootprintProspectsParams(values);
        }
    }
}
