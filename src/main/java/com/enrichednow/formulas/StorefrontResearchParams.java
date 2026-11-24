// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Storefront Research ({@code storefront-research}): What a competing Amazon seller sells in your
 * niche, its prices and Buy Box, the sellers it competes with, and where to source it.
 *
 * Only {@code seller_ids}, {@code queries} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * StorefrontResearchParams params = StorefrontResearchParams.builder()
 *     .sellerIds("AAK72K4OIWDXL")
 *     .queries("jbl speaker")
 *     .build();
 * }</pre>
 */
public final class StorefrontResearchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "storefront-research";

    private final Map<String, Object> values;

    private StorefrontResearchParams(Map<String, Object> values) {
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
        return "StorefrontResearchParams" + values;
    }

    /** Builds {@link StorefrontResearchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Competitors' seller ids: The id in a storefront's address (seller=...). 1 to 10 items. Required.
         *
         * @param sellerIds the values; not null
         * @return this builder
         */
        public Builder sellerIds(List<String> sellerIds) {
            values.put("seller_ids", List.copyOf(sellerIds));
            return this;
        }

        /**
         * Competitors' seller ids: The id in a storefront's address (seller=...). 1 to 10 items. Required.
         *
         * @param sellerIds the values
         * @return this builder
         */
        public Builder sellerIds(String... sellerIds) {
            return sellerIds(Arrays.asList(sellerIds));
        }

        /**
         * Niche search words: Searched on Amazon; the listings found are checked for the competitor's offers. At
         * most 10 items. Required.
         *
         * @param queries the values; not null
         * @return this builder
         */
        public Builder queries(List<String> queries) {
            values.put("queries", List.copyOf(queries));
            return this;
        }

        /**
         * Niche search words: Searched on Amazon; the listings found are checked for the competitor's offers. At
         * most 10 items. Required.
         *
         * @param queries the values
         * @return this builder
         */
        public Builder queries(String... queries) {
            return queries(Arrays.asList(queries));
        }

        /**
         * Best-seller lists: Also check the products of these Best Sellers lists, such as toys-and-games. At most 5
         * items. Default: [].
         *
         * @param categories the values; not null
         * @return this builder
         */
        public Builder categories(List<String> categories) {
            values.put("categories", List.copyOf(categories));
            return this;
        }

        /**
         * Best-seller lists: Also check the products of these Best Sellers lists, such as toys-and-games. At most 5
         * items. Default: [].
         *
         * @param categories the values
         * @return this builder
         */
        public Builder categories(String... categories) {
            return categories(Arrays.asList(categories));
        }

        /**
         * Listings per search: Each listing is one read of its offers. From 1 to 48. Default: 20.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Look for each product at Target and Best Buy. Default: true.
         *
         * @param sourceAtRetailers the value
         * @return this builder
         */
        public Builder sourceAtRetailers(boolean sourceAtRetailers) {
            values.put("source_at_retailers", sourceAtRetailers);
            return this;
        }

        /**
         * Amazon referral fee (%). From 0 to 50. Default: 15.
         *
         * @param referralFeePct the value
         * @return this builder
         */
        public Builder referralFeePct(double referralFeePct) {
            values.put("referral_fee_pct", referralFeePct);
            return this;
        }

        /**
         * Fulfilment and shipping a unit (USD). From 0 to 100. Default: 5.
         *
         * @param fulfilmentCost the value
         * @return this builder
         */
        public Builder fulfilmentCost(double fulfilmentCost) {
            values.put("fulfilment_cost", fulfilmentCost);
            return this;
        }

        /**
         * Most rows: The run stops at this many products, Buy Box first. From 1 to 500. Default: 100.
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
        public StorefrontResearchParams build() {
            if (!values.containsKey("seller_ids")) {
                throw new IllegalStateException("StorefrontResearchParams: seller_ids is required");
            }
            if (!values.containsKey("queries")) {
                throw new IllegalStateException("StorefrontResearchParams: queries is required");
            }
            return new StorefrontResearchParams(values);
        }
    }
}
