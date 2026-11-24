// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Business-for-Sale Alerts ({@code business-for-sale-alerts}): Businesses newly listed for sale
 * in your states and sectors, with asking price, revenue, cash flow, sector and broker.
 *
 * Only {@code states} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * BusinessForSaleAlertsParams params = BusinessForSaleAlertsParams.builder()
 *     .states("Texas")
 *     .build();
 * }</pre>
 */
public final class BusinessForSaleAlertsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "business-for-sale-alerts";

    private final Map<String, Object> values;

    private BusinessForSaleAlertsParams(Map<String, Object> values) {
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
        return "BusinessForSaleAlertsParams" + values;
    }

    /** Builds {@link BusinessForSaleAlertsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * States: US states, by name, such as Texas or North Carolina. 1 to 10 items. Required.
         *
         * @param states the values; not null
         * @return this builder
         */
        public Builder states(List<String> states) {
            values.put("states", List.copyOf(states));
            return this;
        }

        /**
         * States: US states, by name, such as Texas or North Carolina. 1 to 10 items. Required.
         *
         * @param states the values
         * @return this builder
         */
        public Builder states(String... states) {
            return states(Arrays.asList(states));
        }

        /**
         * Kinds of business: Words a listing's title or summary must hold as whole words, such as laundromat or
         * HVAC. Empty keeps every kind. At most 10 items. Default: [].
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Kinds of business: Words a listing's title or summary must hold as whole words, such as laundromat or
         * HVAC. Empty keeps every kind. At most 10 items. Default: [].
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Least cash flow ($): The yearly cash flow or net profit a listing must state. 0 keeps every listing. At
         * least 0. Default: 0.
         *
         * @param minCashFlow the value
         * @return this builder
         */
        public Builder minCashFlow(int minCashFlow) {
            values.put("min_cash_flow", minCashFlow);
            return this;
        }

        /**
         * Highest asking price ($): Listings asking more are left out. 0 sets no limit. At least 0. Default: 0.
         *
         * @param maxAskingPrice the value
         * @return this builder
         */
        public Builder maxAskingPrice(int maxAskingPrice) {
            values.put("max_asking_price", maxAskingPrice);
            return this;
        }

        /**
         * Lowest asking price ($): Listings asking less are left out. At least 0. Default: 0.
         *
         * @param minAskingPrice the value
         * @return this builder
         */
        public Builder minAskingPrice(int minAskingPrice) {
            values.put("min_asking_price", minAskingPrice);
            return this;
        }

        /**
         * Least revenue ($): The yearly revenue a listing must state. 0 keeps every listing. At least 0. Default:
         * 0.
         *
         * @param minRevenue the value
         * @return this builder
         */
        public Builder minRevenue(int minRevenue) {
            values.put("min_revenue", minRevenue);
            return this;
        }

        /**
         * Keep listings that withhold a figure: A listing whose cash flow, revenue or price is on request is kept
         * rather than judged by a number it does not give. Default: true.
         *
         * @param keepUndisclosed the value
         * @return this builder
         */
        public Builder keepUndisclosed(boolean keepUndisclosed) {
            values.put("keep_undisclosed", keepUndisclosed);
            return this;
        }

        /**
         * Sold by: Every listing, only those a broker sells, or only those the owner sells. One of "any", "broker",
         * "owner". Default: "any".
         *
         * @param seller the value
         * @return this builder
         */
        public Builder seller(String seller) {
            values.put("seller", seller);
            return this;
        }

        /**
         * Marketplaces: BusinessesForSale.com and BusinessBroker.net. Each one of "businessesforsale",
         * "businessbroker". At least 1 item. Default: ["businessesforsale", "businessbroker"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Marketplaces: BusinessesForSale.com and BusinessBroker.net. Each one of "businessesforsale",
         * "businessbroker". At least 1 item. Default: ["businessesforsale", "businessbroker"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Pages read a state: BusinessesForSale.com's pages of 25, newest first; BusinessBroker.net's first page of
         * about 30 is read either way. From 1 to 4. Default: 2.
         *
         * @param pages the value
         * @return this builder
         */
        public Builder pages(int pages) {
            values.put("pages", pages);
            return this;
        }

        /**
         * Larger companies without their name: BusinessesForSale.com's M&amp;A Vault describes larger companies
         * without naming them, from any country, among a state's listings. Default: false.
         *
         * @param includeVault the value
         * @return this builder
         */
        public Builder includeVault(boolean includeVault) {
            values.put("include_vault", includeVault);
            return this;
        }

        /**
         * Read each listing's page: For the sector, employees, year founded, premises, reason for selling and the
         * broker. Off saves one read a listing. Default: true.
         *
         * @param readListings the value
         * @return this builder
         */
        public Builder readListings(boolean readListings) {
            values.put("read_listings", readListings);
            return this;
        }

        /**
         * Show a listing again after (days): For a saved formula, how long before a delivered listing may come
         * back. From 1 to 365. Default: 365.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many listings, highest cash flow first. From 1 to 1000. Default: 100.
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
        public BusinessForSaleAlertsParams build() {
            if (!values.containsKey("states")) {
                throw new IllegalStateException("BusinessForSaleAlertsParams: states is required");
            }
            return new BusinessForSaleAlertsParams(values);
        }
    }
}
