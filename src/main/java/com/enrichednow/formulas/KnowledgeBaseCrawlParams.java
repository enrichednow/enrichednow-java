// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Knowledge Base Crawl ({@code knowledge-base-crawl}): A site's or docs' pages as clean Markdown,
 * one row a page; saved, it returns only the pages that are new, changed or gone.
 *
 * Only {@code start_url} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * KnowledgeBaseCrawlParams params = KnowledgeBaseCrawlParams.builder()
 *     .startUrl("https://docs.astral.sh/uv/")
 *     .build();
 * }</pre>
 */
public final class KnowledgeBaseCrawlParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "knowledge-base-crawl";

    private final Map<String, Object> values;

    private KnowledgeBaseCrawlParams(Map<String, Object> values) {
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
        return "KnowledgeBaseCrawlParams" + values;
    }

    /** Builds {@link KnowledgeBaseCrawlParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Start address: The address of the site or the documentation, such as https://docs.example.com/guide/.
         * Only pages of the same site are read. Required.
         *
         * @param startUrl the value
         * @return this builder
         */
        public Builder startUrl(String startUrl) {
            values.put("start_url", startUrl);
            return this;
        }

        /**
         * Most pages read: Each page read is one unit. Following links reads at most 100 pages a run; with a
         * sitemap, up to 2000. From 1 to 2000. Default: 50.
         *
         * @param maxPages the value
         * @return this builder
         */
        public Builder maxPages(int maxPages) {
            values.put("max_pages", maxPages);
            return this;
        }

        /**
         * Only these sections: Read only pages whose path starts with one of these, such as /docs/. Leave empty for
         * the whole site below the start address. At most 20 items. Default: [].
         *
         * @param includePaths the values; not null
         * @return this builder
         */
        public Builder includePaths(List<String> includePaths) {
            values.put("include_paths", List.copyOf(includePaths));
            return this;
        }

        /**
         * Only these sections: Read only pages whose path starts with one of these, such as /docs/. Leave empty for
         * the whole site below the start address. At most 20 items. Default: [].
         *
         * @param includePaths the values
         * @return this builder
         */
        public Builder includePaths(String... includePaths) {
            return includePaths(Arrays.asList(includePaths));
        }

        /**
         * Sitemap: To read the pages a sitemap lists instead of following links, its address
         * (https://example.com/sitemap.xml) or the site's bare address. Only listed pages under the start address
         * are read. Default: "".
         *
         * @param sitemapUrl the value
         * @return this builder
         */
        public Builder sitemapUrl(String sitemapUrl) {
            values.put("sitemap_url", sitemapUrl);
            return this;
        }

        /**
         * Leave out these sections: Pages whose path starts with one of these are not read, such as /blog/ or
         * /changelog/. At most 20 items. Default: [].
         *
         * @param excludePaths the values; not null
         * @return this builder
         */
        public Builder excludePaths(List<String> excludePaths) {
            values.put("exclude_paths", List.copyOf(excludePaths));
            return this;
        }

        /**
         * Leave out these sections: Pages whose path starts with one of these are not read, such as /blog/ or
         * /changelog/. At most 20 items. Default: [].
         *
         * @param excludePaths the values
         * @return this builder
         */
        public Builder excludePaths(String... excludePaths) {
            return excludePaths(Arrays.asList(excludePaths));
        }

        /**
         * Link depth: How many links away from the start address a page may be when links are followed. From 0 to
         * 10. Default: 3.
         *
         * @param maxDepth the value
         * @return this builder
         */
        public Builder maxDepth(int maxDepth) {
            values.put("max_depth", maxDepth);
            return this;
        }

        /**
         * Keep menus, headers and footers: Off, each page's Markdown is its main content only, which is what an
         * index wants. Default: false.
         *
         * @param wholePage the value
         * @return this builder
         */
        public Builder wholePage(boolean wholePage) {
            values.put("whole_page", wholePage);
            return this;
        }

        /**
         * Least words on a page: Leaves out near-empty pages, such as redirects and section stubs. From 0 to 5000.
         * Default: 20.
         *
         * @param minWords the value
         * @return this builder
         */
        public Builder minWords(int minWords) {
            values.put("min_words", minWords);
            return this;
        }

        /**
         * Report pages that are gone: A saved formula also returns the pages the last run read that this one did
         * not find. Turn it off when the site has more pages than a run reads. Default: true.
         *
         * @param reportRemoved the value
         * @return this builder
         */
        public Builder reportRemoved(boolean reportRemoved) {
            values.put("report_removed", reportRemoved);
            return this;
        }

        /**
         * Most rows: The run stops at this many pages, changed ones first. From 1 to 2000. Default: 500.
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
        public KnowledgeBaseCrawlParams build() {
            if (!values.containsKey("start_url")) {
                throw new IllegalStateException("KnowledgeBaseCrawlParams: start_url is required");
            }
            return new KnowledgeBaseCrawlParams(values);
        }
    }
}
