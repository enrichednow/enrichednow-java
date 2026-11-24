// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Property Comps ({@code property-comps}): Recently sold and listed homes like yours in a US
 * area, with sale price, price per square foot and the ZIP code average.
 *
 * Only {@code locations} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * PropertyCompsParams params = PropertyCompsParams.builder()
 *     .locations("85032")
 *     .build();
 * }</pre>
 */
public final class PropertyCompsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "property-comps";

    private final Map<String, Object> values;

    private PropertyCompsParams(Map<String, Object> values) {
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
        return "PropertyCompsParams" + values;
    }

    /** Builds {@link PropertyCompsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Places: ZIP codes, cities with their state ("Austin, TX") or neighbourhoods, as Zillow names them. 1 to
         * 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Places: ZIP codes, cities with their state ("Austin, TX") or neighbourhoods, as Zillow names them. 1 to
         * 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Kinds of home: The kinds of home that count as comparable. Each one of "house", "condo", "townhouse",
         * "multi_family", "manufactured". Default: ["house"].
         *
         * @param homeTypes the values; not null
         * @return this builder
         */
        public Builder homeTypes(List<String> homeTypes) {
            values.put("home_types", List.copyOf(homeTypes));
            return this;
        }

        /**
         * Kinds of home: The kinds of home that count as comparable. Each one of "house", "condo", "townhouse",
         * "multi_family", "manufactured". Default: ["house"].
         *
         * @param homeTypes the values
         * @return this builder
         */
        public Builder homeTypes(String... homeTypes) {
            return homeTypes(Arrays.asList(homeTypes));
        }

        /**
         * Least bedrooms: 0 for any. From 0 to 5. Default: 0.
         *
         * @param minBeds the value
         * @return this builder
         */
        public Builder minBeds(int minBeds) {
            values.put("min_beds", minBeds);
            return this;
        }

        /**
         * Living area (sq ft): The size range of a comparable home, such as 1200 to 1800 for a 1500 sq ft home.
         * Default: {"min": 0, "max": 20000}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder livingArea(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("living_area", Collections.unmodifiableMap(range));
            return this;
        }

        /**
         * Show homes for sale too: The matching homes on the market now, as the competition. Default: true.
         *
         * @param includeForSale the value
         * @return this builder
         */
        public Builder includeForSale(boolean includeForSale) {
            values.put("include_for_sale", includeForSale);
            return this;
        }

        /**
         * Sold within (days): Only homes sold in the last this many days. From 7 to 730. Default: 180.
         *
         * @param soldWithinDays the value
         * @return this builder
         */
        public Builder soldWithinDays(int soldWithinDays) {
            values.put("sold_within_days", soldWithinDays);
            return this;
        }

        /**
         * Homes read per place and kind: Zillow's first page of each place is read, up to this many homes, for sold
         * and for sale each. From 5 to 41. Default: 41.
         *
         * @param homesPerPlace the value
         * @return this builder
         */
        public Builder homesPerPlace(int homesPerPlace) {
            values.put("homes_per_place", homesPerPlace);
            return this;
        }

        /**
         * Most rows: The run stops at this many homes, newest sales first. Each sold home's page is read for its
         * price. From 1 to 500. Default: 60.
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
        public PropertyCompsParams build() {
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("PropertyCompsParams: locations is required");
            }
            return new PropertyCompsParams(values);
        }
    }
}
