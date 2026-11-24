// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of E-commerce Panel ({@code ecommerce-panel}): A brand's listings on Amazon, Walmart and Target,
 * with price, stock, rating and ratings count, and what changed since the last run.
 *
 * Only {@code queries} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * EcommercePanelParams params = EcommercePanelParams.builder()
 *     .queries("YETI tumbler", "Crocs clogs")
 *     .build();
 * }</pre>
 */
public final class EcommercePanelParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "ecommerce-panel";

    private final Map<String, Object> values;

    private EcommercePanelParams(Map<String, Object> values) {
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
        return "EcommercePanelParams" + values;
    }

    /** Builds {@link EcommercePanelParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Brands or products: Each is searched on every retailer you choose, as you type it, such as a brand or a
         * brand and a product line. 1 to 20 items. Required.
         *
         * @param queries the values; not null
         * @return this builder
         */
        public Builder queries(List<String> queries) {
            values.put("queries", List.copyOf(queries));
            return this;
        }

        /**
         * Brands or products: Each is searched on every retailer you choose, as you type it, such as a brand or a
         * brand and a product line. 1 to 20 items. Required.
         *
         * @param queries the values
         * @return this builder
         */
        public Builder queries(String... queries) {
            return queries(Arrays.asList(queries));
        }

        /**
         * Only these brands: Keeps the listings whose brand or title names one of these, so that other brands a
         * search shows are left out. Leave empty to keep every listing. At most 20 items. Default: [].
         *
         * @param brands the values; not null
         * @return this builder
         */
        public Builder brands(List<String> brands) {
            values.put("brands", List.copyOf(brands));
            return this;
        }

        /**
         * Only these brands: Keeps the listings whose brand or title names one of these, so that other brands a
         * search shows are left out. Leave empty to keep every listing. At most 20 items. Default: [].
         *
         * @param brands the values
         * @return this builder
         */
        public Builder brands(String... brands) {
            return brands(Arrays.asList(brands));
        }

        /**
         * Retailers: Where to search. Walmart and Target are their US stores. Each one of "amazon", "walmart",
         * "target". At least 1 item. Default: ["amazon", "walmart", "target"].
         *
         * @param retailers the values; not null
         * @return this builder
         */
        public Builder retailers(List<String> retailers) {
            values.put("retailers", List.copyOf(retailers));
            return this;
        }

        /**
         * Retailers: Where to search. Walmart and Target are their US stores. Each one of "amazon", "walmart",
         * "target". At least 1 item. Default: ["amazon", "walmart", "target"].
         *
         * @param retailers the values
         * @return this builder
         */
        public Builder retailers(String... retailers) {
            return retailers(Arrays.asList(retailers));
        }

        /**
         * Listings per search: How many listings to read from the top of each retailer's results for each brand or
         * product. From 1 to 40. Default: 20.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Include sponsored listings: Paid placements are left out by default, so that a position is the retailer's
         * own ranking. Default: false.
         *
         * @param includeSponsored the value
         * @return this builder
         */
        public Builder includeSponsored(boolean includeSponsored) {
            values.put("include_sponsored", includeSponsored);
            return this;
        }

        /**
         * Amazon marketplace: Which Amazon store to read. One of "us", "uk", "de", "ca", "it", "es", "nl", "jp",
         * "au", "mx", "br", "in", "ae". Default: "us".
         *
         * @param amazonMarketplace the value
         * @return this builder
         */
        public Builder amazonMarketplace(String amazonMarketplace) {
            values.put("amazon_marketplace", amazonMarketplace);
            return this;
        }

        /**
         * Amazon Best Sellers lists: Categories as their Best Sellers address names them, such as kitchen, beauty
         * or electronics/172456. A listing in their top 100 gets its rank, and a best seller of your brands is
         * added. At most 10 items. Default: [].
         *
         * @param amazonBestsellerCategories the values; not null
         * @return this builder
         */
        public Builder amazonBestsellerCategories(List<String> amazonBestsellerCategories) {
            values.put("amazon_bestseller_categories", List.copyOf(amazonBestsellerCategories));
            return this;
        }

        /**
         * Amazon Best Sellers lists: Categories as their Best Sellers address names them, such as kitchen, beauty
         * or electronics/172456. A listing in their top 100 gets its rank, and a best seller of your brands is
         * added. At most 10 items. Default: [].
         *
         * @param amazonBestsellerCategories the values
         * @return this builder
         */
        public Builder amazonBestsellerCategories(String... amazonBestsellerCategories) {
            return amazonBestsellerCategories(Arrays.asList(amazonBestsellerCategories));
        }

        /**
         * Check stock on Amazon and Target: Their search results do not say whether a product is in stock, so each
         * Amazon and Target listing's page is read, one unit a listing. Default: true.
         *
         * @param checkStock the value
         * @return this builder
         */
        public Builder checkStock(boolean checkStock) {
            values.put("check_stock", checkStock);
            return this;
        }

        /**
         * Most rows: The run stops at this many listings, the biggest changes first. From 1 to 2000. Default: 200.
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
        public EcommercePanelParams build() {
            if (!values.containsKey("queries")) {
                throw new IllegalStateException("EcommercePanelParams: queries is required");
            }
            return new EcommercePanelParams(values);
        }
    }
}
