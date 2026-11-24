// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Broken Link Prospects ({@code broken-link-prospects}): Resource pages on your topic that link
 * to pages or sites that are gone, each with the dead link and the site's email.
 *
 * Only {@code topic} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * BrokenLinkProspectsParams params = BrokenLinkProspectsParams.builder()
 *     .topic("genealogy")
 *     .build();
 * }</pre>
 */
public final class BrokenLinkProspectsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "broken-link-prospects";

    private final Map<String, Object> values;

    private BrokenLinkProspectsParams(Map<String, Object> values) {
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
        return "BrokenLinkProspectsParams" + values;
    }

    /** Builds {@link BrokenLinkProspectsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Topic: The subject of the resource pages, as people search for it, such as genealogy or home schooling.
         * Required.
         *
         * @param topic the value
         * @return this builder
         */
        public Builder topic(String topic) {
            values.put("topic", topic);
            return this;
        }

        /**
         * Footprints: Words searched beside the topic to find resource pages, each its own search. Quote a phrase
         * to find it exactly. 1 to 12 items. Default: ["\"useful links\"", "\"helpful resources\"", "\"recommended
         * resources\"", "\"helpful links\"", "\"resource list\"", "resources"].
         *
         * @param footprints the values; not null
         * @return this builder
         */
        public Builder footprints(List<String> footprints) {
            values.put("footprints", List.copyOf(footprints));
            return this;
        }

        /**
         * Footprints: Words searched beside the topic to find resource pages, each its own search. Quote a phrase
         * to find it exactly. 1 to 12 items. Default: ["\"useful links\"", "\"helpful resources\"", "\"recommended
         * resources\"", "\"helpful links\"", "\"resource list\"", "resources"].
         *
         * @param footprints the values
         * @return this builder
         */
        public Builder footprints(String... footprints) {
            return footprints(Arrays.asList(footprints));
        }

        /**
         * Resource pages to check: How many resource pages are read and have their links checked, one page per
         * site, best placed first. From 1 to 30. Default: 10.
         *
         * @param pagesToRead the value
         * @return this builder
         */
        public Builder pagesToRead(int pagesToRead) {
            values.put("pages_to_read", pagesToRead);
            return this;
        }

        /**
         * Links looked at per page: The first this many links of each page are looked at, and those to other sites
         * are checked, one unit each. From 10 to 100. Default: 80.
         *
         * @param linksPerPage the value
         * @return this builder
         */
        public Builder linksPerPage(int linksPerPage) {
            values.put("links_per_page", linksPerPage);
            return this;
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
         * Sites to leave out: Domains whose pages are not read and whose links are not checked, such as social
         * networks and share buttons. At most 2000 items. Default: a list of 22 values, which {@code get} on the
         * formula shows.
         *
         * @param excludeDomains the values; not null
         * @return this builder
         */
        public Builder excludeDomains(List<String> excludeDomains) {
            values.put("exclude_domains", List.copyOf(excludeDomains));
            return this;
        }

        /**
         * Sites to leave out: Domains whose pages are not read and whose links are not checked, such as social
         * networks and share buttons. At most 2000 items. Default: a list of 22 values, which {@code get} on the
         * formula shows.
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
         * Only sites with an email: Keeps the pages whose site publishes an email address that passes the check.
         * Default: false.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Show a broken link again after (days): For a saved formula, how long before a delivered broken link may
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
         * Most rows: The run stops at this many broken links, gone sites first. From 1 to 2000. Default: 200.
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
        public BrokenLinkProspectsParams build() {
            if (!values.containsKey("topic")) {
                throw new IllegalStateException("BrokenLinkProspectsParams: topic is required");
            }
            return new BrokenLinkProspectsParams(values);
        }
    }
}
