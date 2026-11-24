// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Unlinked Mentions ({@code unlinked-mentions}): Pages that name your brand but do not link to
 * your site, each read to be sure, with the email the site publishes.
 *
 * Only {@code brand}, {@code brand_domain} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * UnlinkedMentionsParams params = UnlinkedMentionsParams.builder()
 *     .brand("Pipedrive")
 *     .brandDomain("pipedrive.com")
 *     .build();
 * }</pre>
 */
public final class UnlinkedMentionsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "unlinked-mentions";

    private final Map<String, Object> values;

    private UnlinkedMentionsParams(Map<String, Object> values) {
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
        return "UnlinkedMentionsParams" + values;
    }

    /** Builds {@link UnlinkedMentionsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Brand name: The name as people write it. A name that is also a common word finds pages using the word as
         * well. Required.
         *
         * @param brand the value
         * @return this builder
         */
        public Builder brand(String brand) {
            values.put("brand", brand);
            return this;
        }

        /**
         * Your domain: The domain a link should lead to, such as example.com. Pages on it are left out. Required.
         *
         * @param brandDomain the value
         * @return this builder
         */
        public Builder brandDomain(String brandDomain) {
            values.put("brand_domain", brandDomain);
            return this;
        }

        /**
         * Searches: Words searched with the name, each its own search; an empty one searches the name alone. 1 to
         * 10 items. Default: ["", "review", "alternatives", "pricing", "integration"].
         *
         * @param searchTerms the values; not null
         * @return this builder
         */
        public Builder searchTerms(List<String> searchTerms) {
            values.put("search_terms", List.copyOf(searchTerms));
            return this;
        }

        /**
         * Searches: Words searched with the name, each its own search; an empty one searches the name alone. 1 to
         * 10 items. Default: ["", "review", "alternatives", "pricing", "integration"].
         *
         * @param searchTerms the values
         * @return this builder
         */
        public Builder searchTerms(String... searchTerms) {
            return searchTerms(Arrays.asList(searchTerms));
        }

        /**
         * Include news stories: Also reads the stories the news search lists for the name. Default: true.
         *
         * @param includeNews the value
         * @return this builder
         */
        public Builder includeNews(boolean includeNews) {
            values.put("include_news", includeNews);
            return this;
        }

        /**
         * Your other domains: Other domains of yours; a link to any of them counts as a link, and pages on them are
         * left out. At most 20 items. Default: [].
         *
         * @param otherDomains the values; not null
         * @return this builder
         */
        public Builder otherDomains(List<String> otherDomains) {
            values.put("other_domains", List.copyOf(otherDomains));
            return this;
        }

        /**
         * Your other domains: Other domains of yours; a link to any of them counts as a link, and pages on them are
         * left out. At most 20 items. Default: [].
         *
         * @param otherDomains the values
         * @return this builder
         */
        public Builder otherDomains(String... otherDomains) {
            return otherDomains(Arrays.asList(otherDomains));
        }

        /**
         * Pages to read: How many of the pages found are read, news first, then the best placed in search. From 5
         * to 150. Default: 40.
         *
         * @param pagesToRead the value
         * @return this builder
         */
        public Builder pagesToRead(int pagesToRead) {
            values.put("pages_to_read", pagesToRead);
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
         * Sites to leave out: Domains never shown, such as social networks and review sites where no editor adds
         * links. At most 2000 items. Default: a list of 22 values, which {@code get} on the formula shows.
         *
         * @param excludeDomains the values; not null
         * @return this builder
         */
        public Builder excludeDomains(List<String> excludeDomains) {
            values.put("exclude_domains", List.copyOf(excludeDomains));
            return this;
        }

        /**
         * Sites to leave out: Domains never shown, such as social networks and review sites where no editor adds
         * links. At most 2000 items. Default: a list of 22 values, which {@code get} on the formula shows.
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
         * Show a page again after (days): For a saved formula, how long before a delivered page may come back. From
         * 1 to 365. Default: 180.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many pages, news and pages naming the brand in their title first. From 1
         * to 1000. Default: 100.
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
        public UnlinkedMentionsParams build() {
            if (!values.containsKey("brand")) {
                throw new IllegalStateException("UnlinkedMentionsParams: brand is required");
            }
            if (!values.containsKey("brand_domain")) {
                throw new IllegalStateException("UnlinkedMentionsParams: brand_domain is required");
            }
            return new UnlinkedMentionsParams(values);
        }
    }
}
