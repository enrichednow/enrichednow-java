// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Used-Car Arbitrage ({@code used-car-arbitrage}): Copart lots selling well below what the same
 * make, model and year fetch at retail near you, ranked by the spread, with bid moves.
 *
 * Only {@code make}, {@code models}, {@code zip} are required: {@link Builder#build()} refuses to build without
 * them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * UsedCarArbitrageParams params = UsedCarArbitrageParams.builder()
 *     .make("Toyota")
 *     .models("Camry", "Corolla")
 *     .zip("77073")
 *     .build();
 * }</pre>
 */
public final class UsedCarArbitrageParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "used-car-arbitrage";

    private final Map<String, Object> values;

    private UsedCarArbitrageParams(Map<String, Object> values) {
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
        return "UsedCarArbitrageParams" + values;
    }

    /** Builds {@link UsedCarArbitrageParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Make: The make, such as Toyota or Ford. Required.
         *
         * @param make the value
         * @return this builder
         */
        public Builder make(String make) {
            values.put("make", make);
            return this;
        }

        /**
         * Models: The models of that make, named as Copart names them, such as Camry or RAV4. 1 to 10 items.
         * Required.
         *
         * @param models the values; not null
         * @return this builder
         */
        public Builder models(List<String> models) {
            values.put("models", List.copyOf(models));
            return this;
        }

        /**
         * Models: The models of that make, named as Copart names them, such as Camry or RAV4. 1 to 10 items.
         * Required.
         *
         * @param models the values
         * @return this builder
         */
        public Builder models(String... models) {
            return models(Arrays.asList(models));
        }

        /**
         * Your ZIP code: Where you would sell the cars. Retail comparables are the listings around it. Required.
         *
         * @param zip the value
         * @return this builder
         */
        public Builder zip(String zip) {
            values.put("zip", zip);
            return this;
        }

        /**
         * Model years: The model years to search. Each year of each model is one search for retail comparables.
         * Default: {"min": 2018, "max": 2022}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder years(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("years", Collections.unmodifiableMap(range));
            return this;
        }

        /**
         * Lots to watch: Copart lot numbers or lot pages you follow, read on every run whether or not a search
         * finds them. At most 50 items. Default: [].
         *
         * @param watchLots the values; not null
         * @return this builder
         */
        public Builder watchLots(List<String> watchLots) {
            values.put("watch_lots", List.copyOf(watchLots));
            return this;
        }

        /**
         * Lots to watch: Copart lot numbers or lot pages you follow, read on every run whether or not a search
         * finds them. At most 50 items. Default: [].
         *
         * @param watchLots the values
         * @return this builder
         */
        public Builder watchLots(String... watchLots) {
            return watchLots(Arrays.asList(watchLots));
        }

        /**
         * Yard states: Two-letter codes of the states whose yards you buy from, such as TX. Leave empty for all. At
         * most 60 items. Default: [].
         *
         * @param states the values; not null
         * @return this builder
         */
        public Builder states(List<String> states) {
            values.put("states", List.copyOf(states));
            return this;
        }

        /**
         * Yard states: Two-letter codes of the states whose yards you buy from, such as TX. Leave empty for all. At
         * most 60 items. Default: [].
         *
         * @param states the values
         * @return this builder
         */
        public Builder states(String... states) {
            return states(Arrays.asList(states));
        }

        /**
         * Most miles: Lots with more miles on the odometer are left out. 0 for any mileage. From 0 to 500000.
         * Default: 0.
         *
         * @param maxOdometer the value
         * @return this builder
         */
        public Builder maxOdometer(int maxOdometer) {
            values.put("max_odometer", maxOdometer);
            return this;
        }

        /**
         * Leave out titles: Words of the titles to leave out. By default the cars that can never be titled for the
         * road again, which no retail price fits. At most 20 items. Default: ["NON-REPAIRABLE", "DESTRUCTION",
         * "SCRAP", "JUNK", "PARTS ONLY"].
         *
         * @param excludeTitles the values; not null
         * @return this builder
         */
        public Builder excludeTitles(List<String> excludeTitles) {
            values.put("exclude_titles", List.copyOf(excludeTitles));
            return this;
        }

        /**
         * Leave out titles: Words of the titles to leave out. By default the cars that can never be titled for the
         * road again, which no retail price fits. At most 20 items. Default: ["NON-REPAIRABLE", "DESTRUCTION",
         * "SCRAP", "JUNK", "PARTS ONLY"].
         *
         * @param excludeTitles the values
         * @return this builder
         */
        public Builder excludeTitles(String... excludeTitles) {
            return excludeTitles(Arrays.asList(excludeTitles));
        }

        /**
         * Clean titles only: Leaves out salvage and other branded titles. Default: false.
         *
         * @param cleanTitleOnly the value
         * @return this builder
         */
        public Builder cleanTitleOnly(boolean cleanTitleOnly) {
            values.put("clean_title_only", cleanTitleOnly);
            return this;
        }

        /**
         * Runs and drives only: Keeps the lots Copart marks as running and driving. Default: false.
         *
         * @param runsAndDrivesOnly the value
         * @return this builder
         */
        public Builder runsAndDrivesOnly(boolean runsAndDrivesOnly) {
            values.put("runs_and_drives_only", runsAndDrivesOnly);
            return this;
        }

        /**
         * Leave out damage: Words of the main damage to leave out, such as FLOOD, BURN or ALL OVER. At most 20
         * items. Default: [].
         *
         * @param excludeDamage the values; not null
         * @return this builder
         */
        public Builder excludeDamage(List<String> excludeDamage) {
            values.put("exclude_damage", List.copyOf(excludeDamage));
            return this;
        }

        /**
         * Leave out damage: Words of the main damage to leave out, such as FLOOD, BURN or ALL OVER. At most 20
         * items. Default: [].
         *
         * @param excludeDamage the values
         * @return this builder
         */
        public Builder excludeDamage(String... excludeDamage) {
            return excludeDamage(Arrays.asList(excludeDamage));
        }

        /**
         * Least current bid ($): Leaves out lots whose bidding has barely begun, whose spread on a first small bid
         * says little. A lot with a buy-it-now price is kept. 0 keeps every lot. From 0 to 100000. Default: 1000.
         *
         * @param minBid the value
         * @return this builder
         */
        public Builder minBid(int minBid) {
            values.put("min_bid", minBid);
            return this;
        }

        /**
         * A bid that counts (% of retail): A lot whose current bid is under this share of its retail median ranks
         * after the others, since bidding usually goes well past it. A buy-it-now price always counts. From 0 to
         * 90. Default: 25.
         *
         * @param seriousBidPct the value
         * @return this builder
         */
        public Builder seriousBidPct(int seriousBidPct) {
            values.put("serious_bid_pct", seriousBidPct);
            return this;
        }

        /**
         * Include lots selling today: Lots whose sale is on the day of the run, which may be over when the rows
         * arrive. Days are whole calendar days (UTC). A lot you watch is kept on its sale day. Default: false.
         *
         * @param includeSaleDay the value
         * @return this builder
         */
        public Builder includeSaleDay(boolean includeSaleDay) {
            values.put("include_sale_day", includeSaleDay);
            return this;
        }

        /**
         * Lots read per model: How many of Copart's lots to read for each model, the soonest sales first. One of
         * 20, 50, 100. Default: 100.
         *
         * @param lotsPerModel the value
         * @return this builder
         */
        public Builder lotsPerModel(int lotsPerModel) {
            values.put("lots_per_model", lotsPerModel);
            return this;
        }

        /**
         * Least spread ($): Lots whose retail median is less than this above their price are left out. From 0 to
         * 100000. Default: 2000.
         *
         * @param minSpread the value
         * @return this builder
         */
        public Builder minSpread(int minSpread) {
            values.put("min_spread", minSpread);
            return this;
        }

        /**
         * Retail comparables from: The sites whose dealer listings price each lot. A car listed on both counts
         * once. Each one of "cargurus", "autotrader". At least 1 item. Default: ["cargurus", "autotrader"].
         *
         * @param compSources the values; not null
         * @return this builder
         */
        public Builder compSources(List<String> compSources) {
            values.put("comp_sources", List.copyOf(compSources));
            return this;
        }

        /**
         * Retail comparables from: The sites whose dealer listings price each lot. A car listed on both counts
         * once. Each one of "cargurus", "autotrader". At least 1 item. Default: ["cargurus", "autotrader"].
         *
         * @param compSources the values
         * @return this builder
         */
        public Builder compSources(String... compSources) {
            return compSources(Arrays.asList(compSources));
        }

        /**
         * Comparables within (miles): How far from your ZIP code a retail listing may be. One of 25, 50, 75, 100,
         * 200, 500. Default: 100.
         *
         * @param radius the value
         * @return this builder
         */
        public Builder radius(int radius) {
            values.put("radius", radius);
            return this;
        }

        /**
         * Similar mileage (miles either way): Comparables within this many miles of the lot's odometer are
         * preferred. 0 compares every mileage. From 0 to 100000. Default: 30000.
         *
         * @param mileageBand the value
         * @return this builder
         */
        public Builder mileageBand(int mileageBand) {
            values.put("mileage_band", mileageBand);
            return this;
        }

        /**
         * Price on any mileage when few are similar: When too few comparables have a similar mileage, price the lot
         * on every mileage instead of leaving it out. A lot with far more miles than most listings then looks
         * better than it is. Default: false.
         *
         * @param anyMileageFallback the value
         * @return this builder
         */
        public Builder anyMileageFallback(boolean anyMileageFallback) {
            values.put("any_mileage_fallback", anyMileageFallback);
            return this;
        }

        /**
         * Least comparables: A lot priced on fewer retail listings than this is left out. From 1 to 20. Default: 3.
         *
         * @param minComps the value
         * @return this builder
         */
        public Builder minComps(int minComps) {
            values.put("min_comps", minComps);
            return this;
        }

        /**
         * Comparables searches: The most retail searches for each site, one for each make, model and year found.
         * From 1 to 100. Default: 40.
         *
         * @param compSearches the value
         * @return this builder
         */
        public Builder compSearches(int compSearches) {
            values.put("comp_searches", compSearches);
            return this;
        }

        /**
         * Most rows: The run stops at this many lots, widest spread first. From 1 to 2000. Default: 200.
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
        public UsedCarArbitrageParams build() {
            if (!values.containsKey("make")) {
                throw new IllegalStateException("UsedCarArbitrageParams: make is required");
            }
            if (!values.containsKey("models")) {
                throw new IllegalStateException("UsedCarArbitrageParams: models is required");
            }
            if (!values.containsKey("zip")) {
                throw new IllegalStateException("UsedCarArbitrageParams: zip is required");
            }
            return new UsedCarArbitrageParams(values);
        }
    }
}
