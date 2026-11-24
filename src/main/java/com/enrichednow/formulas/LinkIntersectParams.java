// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Link Intersect ({@code link-intersect}): Sites that link to two or more of your competitors but
 * not to you, each with the page and the email the site publishes.
 *
 * Only {@code your_domain}, {@code competitors} are required: {@link Builder#build()} refuses to build without
 * them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * LinkIntersectParams params = LinkIntersectParams.builder()
 *     .yourDomain("pipedrive.com")
 *     .competitors("hubspot.com", "salesforce.com")
 *     .build();
 * }</pre>
 */
public final class LinkIntersectParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "link-intersect";

    private final Map<String, Object> values;

    private LinkIntersectParams(Map<String, Object> values) {
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
        return "LinkIntersectParams" + values;
    }

    /** Builds {@link LinkIntersectParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Your domain: Your site's domain, such as example.com. Pages that already link to it are left out.
         * Required.
         *
         * @param yourDomain the value
         * @return this builder
         */
        public Builder yourDomain(String yourDomain) {
            values.put("your_domain", yourDomain);
            return this;
        }

        /**
         * Competitors' domains: Two to six domains, such as rival.com. Every pair of them is searched for, so each
         * one added costs more. 2 to 6 items. Required.
         *
         * @param competitors the values; not null
         * @return this builder
         */
        public Builder competitors(List<String> competitors) {
            values.put("competitors", List.copyOf(competitors));
            return this;
        }

        /**
         * Competitors' domains: Two to six domains, such as rival.com. Every pair of them is searched for, so each
         * one added costs more. 2 to 6 items. Required.
         *
         * @param competitors the values
         * @return this builder
         */
        public Builder competitors(String... competitors) {
            return competitors(Arrays.asList(competitors));
        }

        /**
         * Topic words: Words added to every search to keep it on your subject, such as crm. Leave empty to search
         * the domains alone. Default: "".
         *
         * @param topic the value
         * @return this builder
         */
        public Builder topic(String topic) {
            values.put("topic", topic);
            return this;
        }

        /**
         * Competitors linked, at least: A page must link to at least this many of your competitors. With 1, each
         * competitor is also searched for alone. From 1 to 6. Default: 2.
         *
         * @param minCompetitors the value
         * @return this builder
         */
        public Builder minCompetitors(int minCompetitors) {
            values.put("min_competitors", minCompetitors);
            return this;
        }

        /**
         * Count a competitor named without a link: A page that writes a competitor's domain in its text without
         * linking to it counts toward the least number. Default: true.
         *
         * @param countNamed the value
         * @return this builder
         */
        public Builder countNamed(boolean countNamed) {
            values.put("count_named", countNamed);
            return this;
        }

        /**
         * Results pages per search: How many pages of results to read for each search, about ten results a page.
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
         * Pages to check: How many of the pages found are read to see whom they link to, those found by the most
         * searches first; never more than the most rows. From 5 to 100. Default: 30.
         *
         * @param pagesToRead the value
         * @return this builder
         */
        public Builder pagesToRead(int pagesToRead) {
            values.put("pages_to_read", pagesToRead);
            return this;
        }

        /**
         * Sites to leave out: Domains never shown, such as social networks and review sites where no editor can add
         * your link. At most 2000 items. Default: a list of 19 values, which {@code get} on the formula shows.
         *
         * @param excludeDomains the values; not null
         * @return this builder
         */
        public Builder excludeDomains(List<String> excludeDomains) {
            values.put("exclude_domains", List.copyOf(excludeDomains));
            return this;
        }

        /**
         * Sites to leave out: Domains never shown, such as social networks and review sites where no editor can add
         * your link. At most 2000 items. Default: a list of 19 values, which {@code get} on the formula shows.
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
         * Most rows: The run stops at this many sites, those linking to the most competitors first. From 1 to 1000.
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
        public LinkIntersectParams build() {
            if (!values.containsKey("your_domain")) {
                throw new IllegalStateException("LinkIntersectParams: your_domain is required");
            }
            if (!values.containsKey("competitors")) {
                throw new IllegalStateException("LinkIntersectParams: competitors is required");
            }
            return new LinkIntersectParams(values);
        }
    }
}
