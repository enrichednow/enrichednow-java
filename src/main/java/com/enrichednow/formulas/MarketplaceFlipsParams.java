// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Amazon Marketplace Flips (Europe) ({@code marketplace-flips}): Products cheaper on one European
 * Amazon than another, by the same ASIN, with the price gap and margin in euros.
 *
 * Only {@code queries} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * MarketplaceFlipsParams params = MarketplaceFlipsParams.builder()
 *     .queries("lego technic")
 *     .build();
 * }</pre>
 */
public final class MarketplaceFlipsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "marketplace-flips";

    private final Map<String, Object> values;

    private MarketplaceFlipsParams(Map<String, Object> values) {
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
        return "MarketplaceFlipsParams" + values;
    }

    /** Builds {@link MarketplaceFlipsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Search words: Searched on the Amazon you buy from, such as lego technic. Not needed when ASINs are given.
         * At most 10 items. Required.
         *
         * @param queries the values; not null
         * @return this builder
         */
        public Builder queries(List<String> queries) {
            values.put("queries", List.copyOf(queries));
            return this;
        }

        /**
         * Search words: Searched on the Amazon you buy from, such as lego technic. Not needed when ASINs are given.
         * At most 10 items. Required.
         *
         * @param queries the values
         * @return this builder
         */
        public Builder queries(String... queries) {
            return queries(Arrays.asList(queries));
        }

        /**
         * Buy on: The Amazon you buy from (amazon.de, .it, .es or .nl). One of "de", "it", "es", "nl". Default:
         * "de".
         *
         * @param buyMarketplace the value
         * @return this builder
         */
        public Builder buyMarketplace(String buyMarketplace) {
            values.put("buy_marketplace", buyMarketplace);
            return this;
        }

        /**
         * Sell on: The Amazons whose price is compared. The one you buy on is skipped. Each one of "de", "fr",
         * "it", "es", "nl". At least 1 item. Default: ["fr", "it", "es"].
         *
         * @param sellMarketplaces the values; not null
         * @return this builder
         */
        public Builder sellMarketplaces(List<String> sellMarketplaces) {
            values.put("sell_marketplaces", List.copyOf(sellMarketplaces));
            return this;
        }

        /**
         * Sell on: The Amazons whose price is compared. The one you buy on is skipped. Each one of "de", "fr",
         * "it", "es", "nl". At least 1 item. Default: ["fr", "it", "es"].
         *
         * @param sellMarketplaces the values
         * @return this builder
         */
        public Builder sellMarketplaces(String... sellMarketplaces) {
            return sellMarketplaces(Arrays.asList(sellMarketplaces));
        }

        /**
         * ASINs: Products to compare, instead of a search. At most 50 items. Default: [].
         *
         * @param asins the values; not null
         * @return this builder
         */
        public Builder asins(List<String> asins) {
            values.put("asins", List.copyOf(asins));
            return this;
        }

        /**
         * ASINs: Products to compare, instead of a search. At most 50 items. Default: [].
         *
         * @param asins the values
         * @return this builder
         */
        public Builder asins(String... asins) {
            return asins(Arrays.asList(asins));
        }

        /**
         * Least price gap (%): The selling price above the buying price, as a share of the buying price. From 0 to
         * 500. Default: 15.
         *
         * @param minGapPct the value
         * @return this builder
         */
        public Builder minGapPct(double minGapPct) {
            values.put("min_gap_pct", minGapPct);
            return this;
        }

        /**
         * Referral fee (%): The share of the sale price Amazon keeps. From 0 to 50. Default: 15.
         *
         * @param referralFeePct the value
         * @return this builder
         */
        public Builder referralFeePct(double referralFeePct) {
            values.put("referral_fee_pct", referralFeePct);
            return this;
        }

        /**
         * Fulfilment and shipping a unit (EUR): Your fulfilment fee and the cost of moving a unit between
         * countries. From 0 to 100. Default: 4.
         *
         * @param fulfilmentCost the value
         * @return this builder
         */
        public Builder fulfilmentCost(double fulfilmentCost) {
            values.put("fulfilment_cost", fulfilmentCost);
            return this;
        }

        /**
         * Products per search: How many products of each search are compared. From 1 to 48. Default: 15.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Most rows: The run stops at this many pairs, widest gap first. From 1 to 1000. Default: 100.
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
        public MarketplaceFlipsParams build() {
            if (!values.containsKey("queries")) {
                throw new IllegalStateException("MarketplaceFlipsParams: queries is required");
            }
            return new MarketplaceFlipsParams(values);
        }
    }
}
