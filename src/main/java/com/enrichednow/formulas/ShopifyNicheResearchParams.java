// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Shopify Niche Research ({@code shopify-niche-research}): The products of a niche's Shopify
 * stores, with price, newness and reductions, and similar products on AliExpress with the margin.
 *
 * Only {@code stores} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * ShopifyNicheResearchParams params = ShopifyNicheResearchParams.builder()
 *     .stores("puravidabracelets.com")
 *     .build();
 * }</pre>
 */
public final class ShopifyNicheResearchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "shopify-niche-research";

    private final Map<String, Object> values;

    private ShopifyNicheResearchParams(Map<String, Object> values) {
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
        return "ShopifyNicheResearchParams" + values;
    }

    /** Builds {@link ShopifyNicheResearchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Shopify stores: The stores of the niche, by domain. 1 to 20 items. Required.
         *
         * @param stores the values; not null
         * @return this builder
         */
        public Builder stores(List<String> stores) {
            values.put("stores", List.copyOf(stores));
            return this;
        }

        /**
         * Shopify stores: The stores of the niche, by domain. 1 to 20 items. Required.
         *
         * @param stores the values
         * @return this builder
         */
        public Builder stores(String... stores) {
            return stores(Arrays.asList(stores));
        }

        /**
         * Niche words: Keep products whose title, type or tags hold one of these. Empty keeps every product. At
         * most 20 items. Default: [].
         *
         * @param nicheWords the values; not null
         * @return this builder
         */
        public Builder nicheWords(List<String> nicheWords) {
            values.put("niche_words", List.copyOf(nicheWords));
            return this;
        }

        /**
         * Niche words: Keep products whose title, type or tags hold one of these. Empty keeps every product. At
         * most 20 items. Default: [].
         *
         * @param nicheWords the values
         * @return this builder
         */
        public Builder nicheWords(String... nicheWords) {
            return nicheWords(Arrays.asList(nicheWords));
        }

        /**
         * Collection: A collection to read in each store, by its handle, such as best-sellers. Empty reads the
         * catalogue. Default: "".
         *
         * @param collection the value
         * @return this builder
         */
        public Builder collection(String collection) {
            values.put("collection", collection);
            return this;
        }

        /**
         * Look for similar products on AliExpress: One search a product. Default: true.
         *
         * @param findSuppliers the value
         * @return this builder
         */
        public Builder findSuppliers(boolean findSuppliers) {
            values.put("find_suppliers", findSuppliers);
            return this;
        }

        /**
         * Products read per store. From 10 to 250. Default: 250.
         *
         * @param productsPerStore the value
         * @return this builder
         */
        public Builder productsPerStore(int productsPerStore) {
            values.put("products_per_store", productsPerStore);
            return this;
        }

        /**
         * Most rows: The run stops at this many products, newest and reduced first. Each is one AliExpress search.
         * From 1 to 500. Default: 30.
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
        public ShopifyNicheResearchParams build() {
            if (!values.containsKey("stores")) {
                throw new IllegalStateException("ShopifyNicheResearchParams: stores is required");
            }
            return new ShopifyNicheResearchParams(values);
        }
    }
}
