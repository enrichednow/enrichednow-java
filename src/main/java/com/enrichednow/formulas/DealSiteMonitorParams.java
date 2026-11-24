// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Deal-Site Monitor ({@code deal-site-monitor}): New Slickdeals posts for your words, matched to
 * Amazon, with the deal's price against Amazon's and the resale margin.
 *
 * Only {@code queries} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * DealSiteMonitorParams params = DealSiteMonitorParams.builder()
 *     .queries("lego")
 *     .build();
 * }</pre>
 */
public final class DealSiteMonitorParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "deal-site-monitor";

    private final Map<String, Object> values;

    private DealSiteMonitorParams(Map<String, Object> values) {
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
        return "DealSiteMonitorParams" + values;
    }

    /** Builds {@link DealSiteMonitorParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Words to watch: Products, brands or stores, such as lego, airpods or costco. Each is one search, newest
         * deals first. 1 to 10 items. Required.
         *
         * @param queries the values; not null
         * @return this builder
         */
        public Builder queries(List<String> queries) {
            values.put("queries", List.copyOf(queries));
            return this;
        }

        /**
         * Words to watch: Products, brands or stores, such as lego, airpods or costco. Each is one search, newest
         * deals first. 1 to 10 items. Required.
         *
         * @param queries the values
         * @return this builder
         */
        public Builder queries(String... queries) {
            return queries(Arrays.asList(queries));
        }

        /**
         * Only deals at: Store names as Slickdeals shows them, such as Walmart or Target. Empty keeps every store.
         * At most 30 items. Default: [].
         *
         * @param stores the values; not null
         * @return this builder
         */
        public Builder stores(List<String> stores) {
            values.put("stores", List.copyOf(stores));
            return this;
        }

        /**
         * Only deals at: Store names as Slickdeals shows them, such as Walmart or Target. Empty keeps every store.
         * At most 30 items. Default: [].
         *
         * @param stores the values
         * @return this builder
         */
        public Builder stores(String... stores) {
            return stores(Arrays.asList(stores));
        }

        /**
         * Least deal score: The community's votes on the deal. From -50 to 1000. Default: 0.
         *
         * @param minScore the value
         * @return this builder
         */
        public Builder minScore(int minScore) {
            values.put("min_score", minScore);
            return this;
        }

        /**
         * Compare with Amazon: Read each deal's page and find the product on Amazon. Two reads a deal. Default:
         * true.
         *
         * @param compareOnAmazon the value
         * @return this builder
         */
        public Builder compareOnAmazon(boolean compareOnAmazon) {
            values.put("compare_on_amazon", compareOnAmazon);
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
         * Show a deal again after (days). From 1 to 365. Default: 365.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many deals, newest first. Each is two reads when compared with Amazon.
         * From 1 to 500. Default: 40.
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
        public DealSiteMonitorParams build() {
            if (!values.containsKey("queries")) {
                throw new IllegalStateException("DealSiteMonitorParams: queries is required");
            }
            return new DealSiteMonitorParams(values);
        }
    }
}
