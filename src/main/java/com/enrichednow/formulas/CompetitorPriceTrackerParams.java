// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Competitor Price Tracker ({@code competitor-price-tracker}): Every product and price of
 * competitors' Shopify stores and Amazon listings, read on a schedule, with what changed since last time.
 *
 * Only {@code stores} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * CompetitorPriceTrackerParams params = CompetitorPriceTrackerParams.builder()
 *     .stores("allbirds.com")
 *     .build();
 * }</pre>
 */
public final class CompetitorPriceTrackerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "competitor-price-tracker";

    private final Map<String, Object> values;

    private CompetitorPriceTrackerParams(Map<String, Object> values) {
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
        return "CompetitorPriceTrackerParams" + values;
    }

    /** Builds {@link CompetitorPriceTrackerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Competitor stores: Shopify stores by domain, such as allbirds.com. Each store's whole catalogue is read.
         * At most 20 items. Required.
         *
         * @param stores the values; not null
         * @return this builder
         */
        public Builder stores(List<String> stores) {
            values.put("stores", List.copyOf(stores));
            return this;
        }

        /**
         * Competitor stores: Shopify stores by domain, such as allbirds.com. Each store's whole catalogue is read.
         * At most 20 items. Required.
         *
         * @param stores the values
         * @return this builder
         */
        public Builder stores(String... stores) {
            return stores(Arrays.asList(stores));
        }

        /**
         * Amazon listings: Competitors' Amazon products by ASIN, read one by one. At most 100 items. Default: [].
         *
         * @param asins the values; not null
         * @return this builder
         */
        public Builder asins(List<String> asins) {
            values.put("asins", List.copyOf(asins));
            return this;
        }

        /**
         * Amazon listings: Competitors' Amazon products by ASIN, read one by one. At most 100 items. Default: [].
         *
         * @param asins the values
         * @return this builder
         */
        public Builder asins(String... asins) {
            return asins(Arrays.asList(asins));
        }

        /**
         * Only products about: Keep products whose title or type holds one of these. Empty keeps every product. At
         * most 20 items. Default: [].
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Only products about: Keep products whose title or type holds one of these. Empty keeps every product. At
         * most 20 items. Default: [].
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Store collection: Read only this collection of each store, by its handle (the name in
         * /collections/&lt;handle&gt;). Empty reads the whole catalogue. Default: "".
         *
         * @param collection the value
         * @return this builder
         */
        public Builder collection(String collection) {
            values.put("collection", collection);
            return this;
        }

        /**
         * Products read per store: A store's catalogue is read 250 products at a time; this many are read. From 10
         * to 250. Default: 250.
         *
         * @param productsPerStore the value
         * @return this builder
         */
        public Builder productsPerStore(int productsPerStore) {
            values.put("products_per_store", productsPerStore);
            return this;
        }

        /**
         * Amazon marketplace: Which Amazon the listings are on. One of "us", "uk", "de", "fr", "it", "es", "ca".
         * Default: "us".
         *
         * @param amazonMarketplace the value
         * @return this builder
         */
        public Builder amazonMarketplace(String amazonMarketplace) {
            values.put("amazon_marketplace", amazonMarketplace);
            return this;
        }

        /**
         * Only what changed: Show only products that are new or whose price, compare-at price or stock changed
         * since the last run. Default: false.
         *
         * @param onlyChanges the value
         * @return this builder
         */
        public Builder onlyChanges(boolean onlyChanges) {
            values.put("only_changes", onlyChanges);
            return this;
        }

        /**
         * Most rows: The run stops at this many products, changes first. From 1 to 5000. Default: 1000.
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
        public CompetitorPriceTrackerParams build() {
            if (!values.containsKey("stores")) {
                throw new IllegalStateException("CompetitorPriceTrackerParams: stores is required");
            }
            return new CompetitorPriceTrackerParams(values);
        }
    }
}
