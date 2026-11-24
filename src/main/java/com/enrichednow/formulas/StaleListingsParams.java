// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Stale Listings ({@code stale-listings}): Homes for sale that have sat on Zillow for months or
 * been cut in price, with list price, Zestimate and the listing agent.
 *
 * Only {@code locations} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * StaleListingsParams params = StaleListingsParams.builder()
 *     .locations("Austin, TX")
 *     .build();
 * }</pre>
 */
public final class StaleListingsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "stale-listings";

    private final Map<String, Object> values;

    private StaleListingsParams(Map<String, Object> values) {
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
        return "StaleListingsParams" + values;
    }

    /** Builds {@link StaleListingsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Places: Cities with their state ("Austin, TX"), ZIP codes or counties, as Zillow names them. 1 to 20
         * items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Places: Cities with their state ("Austin, TX"), ZIP codes or counties, as Zillow names them. 1 to 20
         * items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Least days on Zillow: Homes listed at least this long. A home with a price cut is kept sooner when the
         * next option is on. From 1 to 730. Default: 90.
         *
         * @param minDaysOnMarket the value
         * @return this builder
         */
        public Builder minDaysOnMarket(int minDaysOnMarket) {
            values.put("min_days_on_market", minDaysOnMarket);
            return this;
        }

        /**
         * A price cut makes a listing stale too: Keep a home whose price was cut, however long it has been listed.
         * Default: true.
         *
         * @param priceCutCounts the value
         * @return this builder
         */
        public Builder priceCutCounts(boolean priceCutCounts) {
            values.put("price_cut_counts", priceCutCounts);
            return this;
        }

        /**
         * Kinds of home: Which kinds of home to search. Each one of "house", "condo", "townhouse", "multi_family",
         * "manufactured", "land". Default: ["house", "townhouse", "multi_family"].
         *
         * @param homeTypes the values; not null
         * @return this builder
         */
        public Builder homeTypes(List<String> homeTypes) {
            values.put("home_types", List.copyOf(homeTypes));
            return this;
        }

        /**
         * Kinds of home: Which kinds of home to search. Each one of "house", "condo", "townhouse", "multi_family",
         * "manufactured", "land". Default: ["house", "townhouse", "multi_family"].
         *
         * @param homeTypes the values
         * @return this builder
         */
        public Builder homeTypes(String... homeTypes) {
            return homeTypes(Arrays.asList(homeTypes));
        }

        /**
         * Least price (USD): 0 for no floor. At least 0. Default: 0.
         *
         * @param minPrice the value
         * @return this builder
         */
        public Builder minPrice(int minPrice) {
            values.put("min_price", minPrice);
            return this;
        }

        /**
         * Most price (USD): 0 for no ceiling. At least 0. Default: 0.
         *
         * @param maxPrice the value
         * @return this builder
         */
        public Builder maxPrice(int maxPrice) {
            values.put("max_price", maxPrice);
            return this;
        }

        /**
         * Listings read per place: Zillow's first page of each place is read, up to this many homes. From 5 to 41.
         * Default: 41.
         *
         * @param listingsPerPlace the value
         * @return this builder
         */
        public Builder listingsPerPlace(int listingsPerPlace) {
            values.put("listings_per_place", listingsPerPlace);
            return this;
        }

        /**
         * Read each listing's agent and history: The listing agent and brokerage, the Zestimate and the price
         * history. One page a listing. Default: true.
         *
         * @param withAgent the value
         * @return this builder
         */
        public Builder withAgent(boolean withAgent) {
            values.put("with_agent", withAgent);
            return this;
        }

        /**
         * Show a listing again after (days): For a saved formula, how long before a delivered listing may come
         * back. From 1 to 365. Default: 30.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many listings, longest listed first. From 1 to 1000. Default: 100.
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
        public StaleListingsParams build() {
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("StaleListingsParams: locations is required");
            }
            return new StaleListingsParams(values);
        }
    }
}
