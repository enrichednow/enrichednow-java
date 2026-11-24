// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Price and Seller Watch ({@code price-seller-watch}): Who sells each of your products on Amazon,
 * Best Buy, Target and Newegg, at what price, what changed, and who is under your minimum.
 *
 * Only {@code products} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * PriceSellerWatchParams params = PriceSellerWatchParams.builder()
 *     .products("{'name': 'JBL Flip 6 Portable Waterproof Speaker', 'code': '050036384407', 'brand': 'JBL', 'model': 'JBLFLIP6BLKAM', 'min_price': 109.99}")
 *     .build();
 * }</pre>
 */
public final class PriceSellerWatchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "price-seller-watch";

    private final Map<String, Object> values;

    private PriceSellerWatchParams(Map<String, Object> values) {
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
        return "PriceSellerWatchParams" + values;
    }

    /** Builds {@link PriceSellerWatchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Products: One product an entry: its name as shoppers search it, and where you have them its UPC or EAN
         * (code), Amazon ASIN, brand, model and minimum advertised price (min_price). 1 to 50 items. Required.
         *
         * @param products the values; not null
         * @return this builder
         */
        public Builder products(List<Object> products) {
            values.put("products", List.copyOf(products));
            return this;
        }

        /**
         * Products: One product an entry: its name as shoppers search it, and where you have them its UPC or EAN
         * (code), Amazon ASIN, brand, model and minimum advertised price (min_price). 1 to 50 items. Required.
         *
         * @param products the values
         * @return this builder
         */
        public Builder products(Object... products) {
            return products(Arrays.asList(products));
        }

        /**
         * Marketplaces: Where to look. Walmart often answers automated visits with a check, so its listings may be
         * missing from a run. Each one of "amazon", "bestbuy", "target", "newegg", "walmart". At least 1 item.
         * Default: ["amazon", "bestbuy", "target"].
         *
         * @param marketplaces the values; not null
         * @return this builder
         */
        public Builder marketplaces(List<String> marketplaces) {
            values.put("marketplaces", List.copyOf(marketplaces));
            return this;
        }

        /**
         * Marketplaces: Where to look. Walmart often answers automated visits with a check, so its listings may be
         * missing from a run. Each one of "amazon", "bestbuy", "target", "newegg", "walmart". At least 1 item.
         * Default: ["amazon", "bestbuy", "target"].
         *
         * @param marketplaces the values
         * @return this builder
         */
        public Builder marketplaces(String... marketplaces) {
            return marketplaces(Arrays.asList(marketplaces));
        }

        /**
         * Authorised sellers: Seller names as the marketplaces show them, such as Amazon.com or Best Buy. Empty
         * treats every seller as authorised. At most 500 items. Default: [].
         *
         * @param authorisedSellers the values; not null
         * @return this builder
         */
        public Builder authorisedSellers(List<String> authorisedSellers) {
            values.put("authorised_sellers", List.copyOf(authorisedSellers));
            return this;
        }

        /**
         * Authorised sellers: Seller names as the marketplaces show them, such as Amazon.com or Best Buy. Empty
         * treats every seller as authorised. At most 500 items. Default: [].
         *
         * @param authorisedSellers the values
         * @return this builder
         */
        public Builder authorisedSellers(String... authorisedSellers) {
            return authorisedSellers(Arrays.asList(authorisedSellers));
        }

        /**
         * Price change worth an alert (%): A price that moved by at least this much since the last run raises an
         * alert. From 0 to 100. Default: 5.
         *
         * @param priceChangePct the value
         * @return this builder
         */
        public Builder priceChangePct(double priceChangePct) {
            values.put("price_change_pct", priceChangePct);
            return this;
        }

        /**
         * Amazon marketplace: Which Amazon to read. Best Buy, Target, Newegg and Walmart are read in the United
         * States. One of "us", "uk", "de", "fr", "it", "es", "ca". Default: "us".
         *
         * @param amazonMarketplace the value
         * @return this builder
         */
        public Builder amazonMarketplace(String amazonMarketplace) {
            values.put("amazon_marketplace", amazonMarketplace);
            return this;
        }

        /**
         * Listings compared per marketplace: How many search results of each marketplace are checked against each
         * product. From 1 to 5. Default: 2.
         *
         * @param candidatesPerMarketplace the value
         * @return this builder
         */
        public Builder candidatesPerMarketplace(int candidatesPerMarketplace) {
            values.put("candidates_per_marketplace", candidatesPerMarketplace);
            return this;
        }

        /**
         * Only rows with an alert: Leave out the offers that raise no alert. Default: false.
         *
         * @param alertsOnly the value
         * @return this builder
         */
        public Builder alertsOnly(boolean alertsOnly) {
            values.put("alerts_only", alertsOnly);
            return this;
        }

        /**
         * Most rows: The run stops at this many offers, alerts first. From 1 to 2000. Default: 500.
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
        public PriceSellerWatchParams build() {
            if (!values.containsKey("products")) {
                throw new IllegalStateException("PriceSellerWatchParams: products is required");
            }
            return new PriceSellerWatchParams(values);
        }
    }
}
