// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Wholesale Catalogue Check ({@code wholesale-catalog-check}): Your supplier's price list checked
 * against Amazon line by line, with Amazon's price, reviews and your margin after fees.
 *
 * Only {@code catalog} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * WholesaleCatalogCheckParams params = WholesaleCatalogCheckParams.builder()
 *     .catalog("{'code': '630509870233', 'cost': 5.0, 'name': 'Trouble Board Game'}")
 *     .build();
 * }</pre>
 */
public final class WholesaleCatalogCheckParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "wholesale-catalog-check";

    private final Map<String, Object> values;

    private WholesaleCatalogCheckParams(Map<String, Object> values) {
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
        return "WholesaleCatalogCheckParams" + values;
    }

    /** Builds {@link WholesaleCatalogCheckParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Supplier catalogue: One line a product, from the supplier's sheet - its UPC or EAN (code), your cost, the
         * name, and the case pack when the cost is for a case. 1 to 500 items. Required.
         *
         * @param catalog the values; not null
         * @return this builder
         */
        public Builder catalog(List<Object> catalog) {
            values.put("catalog", List.copyOf(catalog));
            return this;
        }

        /**
         * Supplier catalogue: One line a product, from the supplier's sheet - its UPC or EAN (code), your cost, the
         * name, and the case pack when the cost is for a case. 1 to 500 items. Required.
         *
         * @param catalog the values
         * @return this builder
         */
        public Builder catalog(Object... catalog) {
            return catalog(Arrays.asList(catalog));
        }

        /**
         * Amazon marketplace: Where you sell. One of "us", "uk", "de", "it", "es", "ca". Default: "us".
         *
         * @param marketplace the value
         * @return this builder
         */
        public Builder marketplace(String marketplace) {
            values.put("marketplace", marketplace);
            return this;
        }

        /**
         * Currency of your costs: Costs are compared only with Amazon prices in this currency. One of "USD", "GBP",
         * "EUR", "CAD". Default: "USD".
         *
         * @param costCurrency the value
         * @return this builder
         */
        public Builder costCurrency(String costCurrency) {
            values.put("cost_currency", costCurrency);
            return this;
        }

        /**
         * Least return on cost (%): Lines below this are left out. 0 keeps every line that makes money; -100 keeps
         * every line found. From -100 to 1000. Default: 0.
         *
         * @param minRoiPct the value
         * @return this builder
         */
        public Builder minRoiPct(double minRoiPct) {
            values.put("min_roi_pct", minRoiPct);
            return this;
        }

        /**
         * Amazon referral fee (%): The share of the sale price Amazon keeps. From 0 to 50. Default: 15.
         *
         * @param referralFeePct the value
         * @return this builder
         */
        public Builder referralFeePct(double referralFeePct) {
            values.put("referral_fee_pct", referralFeePct);
            return this;
        }

        /**
         * Fulfilment and shipping a unit: Your FBA fee, prep and inbound shipping for one unit, in the currency of
         * your costs. From 0 to 100. Default: 4.
         *
         * @param fulfilmentCost the value
         * @return this builder
         */
        public Builder fulfilmentCost(double fulfilmentCost) {
            values.put("fulfilment_cost", fulfilmentCost);
            return this;
        }

        /**
         * Read each listing's page: Its Buy Box price, whether Amazon sells it, and stock. One page a line found.
         * Default: true.
         *
         * @param checkOffer the value
         * @return this builder
         */
        public Builder checkOffer(boolean checkOffer) {
            values.put("check_offer", checkOffer);
            return this;
        }

        /**
         * Most rows: The run stops at this many lines, best return first. From 1 to 500. Default: 500.
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
        public WholesaleCatalogCheckParams build() {
            if (!values.containsKey("catalog")) {
                throw new IllegalStateException("WholesaleCatalogCheckParams: catalog is required");
            }
            return new WholesaleCatalogCheckParams(values);
        }
    }
}
