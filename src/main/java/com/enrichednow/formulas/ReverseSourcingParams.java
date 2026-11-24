// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Reverse Sourcing ({@code reverse-sourcing}): Products that sell on Amazon for more than Target,
 * Best Buy, Newegg or Walmart charge, with profit and ROI after your fees.
 *
 * Only {@code queries} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * ReverseSourcingParams params = ReverseSourcingParams.builder()
 *     .queries("lego")
 *     .build();
 * }</pre>
 */
public final class ReverseSourcingParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "reverse-sourcing";

    private final Map<String, Object> values;

    private ReverseSourcingParams(Map<String, Object> values) {
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
        return "ReverseSourcingParams" + values;
    }

    /** Builds {@link ReverseSourcingParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Search words: Searched on Amazon (reverse, without ASINs) or on each retailer (forward), such as lego
         * duplo or air fryer. At most 10 items. Required.
         *
         * @param queries the values; not null
         * @return this builder
         */
        public Builder queries(List<String> queries) {
            values.put("queries", List.copyOf(queries));
            return this;
        }

        /**
         * Search words: Searched on Amazon (reverse, without ASINs) or on each retailer (forward), such as lego
         * duplo or air fryer. At most 10 items. Required.
         *
         * @param queries the values
         * @return this builder
         */
        public Builder queries(String... queries) {
            return queries(Arrays.asList(queries));
        }

        /**
         * Start from: reverse starts from Amazon products and looks for them at the retailers; forward starts from
         * the retailers' search and looks for each product on Amazon. One of "reverse", "forward". Default:
         * "reverse".
         *
         * @param mode the value
         * @return this builder
         */
        public Builder mode(String mode) {
            values.put("mode", mode);
            return this;
        }

        /**
         * Amazon products: For reverse, the ASINs to source. Leave empty to search Amazon for the words below
         * instead. At most 50 items. Default: [].
         *
         * @param asins the values; not null
         * @return this builder
         */
        public Builder asins(List<String> asins) {
            values.put("asins", List.copyOf(asins));
            return this;
        }

        /**
         * Amazon products: For reverse, the ASINs to source. Leave empty to search Amazon for the words below
         * instead. At most 50 items. Default: [].
         *
         * @param asins the values
         * @return this builder
         */
        public Builder asins(String... asins) {
            return asins(Arrays.asList(asins));
        }

        /**
         * Retailers: Where to buy. Walmart often answers automated visits with a check, so its products may be
         * missing from a run. Each one of "target", "bestbuy", "newegg", "walmart". At least 1 item. Default:
         * ["target", "bestbuy"].
         *
         * @param retailers the values; not null
         * @return this builder
         */
        public Builder retailers(List<String> retailers) {
            values.put("retailers", List.copyOf(retailers));
            return this;
        }

        /**
         * Retailers: Where to buy. Walmart often answers automated visits with a check, so its products may be
         * missing from a run. Each one of "target", "bestbuy", "newegg", "walmart". At least 1 item. Default:
         * ["target", "bestbuy"].
         *
         * @param retailers the values
         * @return this builder
         */
        public Builder retailers(String... retailers) {
            return retailers(Arrays.asList(retailers));
        }

        /**
         * Amazon referral fee (%): The share of the sale price Amazon keeps; 15% in most categories, 8% in some.
         * From 0 to 50. Default: 15.
         *
         * @param referralFeePct the value
         * @return this builder
         */
        public Builder referralFeePct(double referralFeePct) {
            values.put("referral_fee_pct", referralFeePct);
            return this;
        }

        /**
         * Fulfilment and shipping a unit (USD): Your FBA fee, prep and inbound shipping for one unit. From 0 to
         * 100. Default: 5.
         *
         * @param fulfilmentCost the value
         * @return this builder
         */
        public Builder fulfilmentCost(double fulfilmentCost) {
            values.put("fulfilment_cost", fulfilmentCost);
            return this;
        }

        /**
         * Least return on cost (%): Profit as a share of the retailer's price. From -100 to 1000. Default: 15.
         *
         * @param minRoiPct the value
         * @return this builder
         */
        public Builder minRoiPct(double minRoiPct) {
            values.put("min_roi_pct", minRoiPct);
            return this;
        }

        /**
         * Least profit a unit (USD): Profit after the referral fee and fulfilment cost. At least -1000. Default: 2.
         *
         * @param minProfit the value
         * @return this builder
         */
        public Builder minProfit(double minProfit) {
            values.put("min_profit", minProfit);
            return this;
        }

        /**
         * Best Buy order: For forward, the order of Best Buy's results; best_discount reads its deepest reductions
         * first. One of "best_match", "best_discount", "best_selling". Default: "best_match".
         *
         * @param bestbuySort the value
         * @return this builder
         */
        public Builder bestbuySort(String bestbuySort) {
            values.put("bestbuy_sort", bestbuySort);
            return this;
        }

        /**
         * Results compared per search: How many search results are checked against each product. From 1 to 5.
         * Default: 2.
         *
         * @param candidatesPerRetailer the value
         * @return this builder
         */
        public Builder candidatesPerRetailer(int candidatesPerRetailer) {
            values.put("candidates_per_retailer", candidatesPerRetailer);
            return this;
        }

        /**
         * Amazon products per search: For reverse without ASINs, how many products of each Amazon search are
         * sourced. From 1 to 48. Default: 10.
         *
         * @param amazonResults the value
         * @return this builder
         */
        public Builder amazonResults(int amazonResults) {
            values.put("amazon_results", amazonResults);
            return this;
        }

        /**
         * Retailer products per search: For forward, how many products of each retailer search are looked up on
         * Amazon. From 1 to 48. Default: 12.
         *
         * @param retailerResults the value
         * @return this builder
         */
        public Builder retailerResults(int retailerResults) {
            values.put("retailer_results", retailerResults);
            return this;
        }

        /**
         * Most rows: The run stops at this many pairs, best return first. From 1 to 1000. Default: 100.
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
        public ReverseSourcingParams build() {
            if (!values.containsKey("queries")) {
                throw new IllegalStateException("ReverseSourcingParams: queries is required");
            }
            return new ReverseSourcingParams(values);
        }
    }
}
