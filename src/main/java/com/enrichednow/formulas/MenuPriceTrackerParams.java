// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Menu Price Tracker ({@code menu-price-tracker}): Every dish and price on the menus of the
 * restaurants you watch, on Uber Eats, TheFork and Zomato, with each price change.
 *
 * Only {@code restaurants} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * MenuPriceTrackerParams params = MenuPriceTrackerParams.builder()
 *     .restaurants("https://www.ubereats.com/store/pho-viet-mai/Xe58iEUhQbqwy-819Y9AhA", "https://www.thefork.com/restaurant/la-grande-cascade-r122")
 *     .build();
 * }</pre>
 */
public final class MenuPriceTrackerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "menu-price-tracker";

    private final Map<String, Object> values;

    private MenuPriceTrackerParams(Map<String, Object> values) {
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
        return "MenuPriceTrackerParams" + values;
    }

    /** Builds {@link MenuPriceTrackerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Restaurants: The restaurants' pages on Uber Eats, TheFork or Zomato, one address each. 1 to 50 items.
         * Required.
         *
         * @param restaurants the values; not null
         * @return this builder
         */
        public Builder restaurants(List<String> restaurants) {
            values.put("restaurants", List.copyOf(restaurants));
            return this;
        }

        /**
         * Restaurants: The restaurants' pages on Uber Eats, TheFork or Zomato, one address each. 1 to 50 items.
         * Required.
         *
         * @param restaurants the values
         * @return this builder
         */
        public Builder restaurants(String... restaurants) {
            return restaurants(Arrays.asList(restaurants));
        }

        /**
         * Only these menu sections: Words of the sections to keep, such as Mains or Pizza. Leave empty for the
         * whole menu. At most 30 items. Default: [].
         *
         * @param sections the values; not null
         * @return this builder
         */
        public Builder sections(List<String> sections) {
            values.put("sections", List.copyOf(sections));
            return this;
        }

        /**
         * Only these menu sections: Words of the sections to keep, such as Mains or Pizza. Leave empty for the
         * whole menu. At most 30 items. Default: [].
         *
         * @param sections the values
         * @return this builder
         */
        public Builder sections(String... sections) {
            return sections(Arrays.asList(sections));
        }

        /**
         * Only dishes named with: Keeps the dishes whose name holds one of these words, such as burger or
         * margherita. Leave empty for every dish. At most 30 items. Default: [].
         *
         * @param dishWords the values; not null
         * @return this builder
         */
        public Builder dishWords(List<String> dishWords) {
            values.put("dish_words", List.copyOf(dishWords));
            return this;
        }

        /**
         * Only dishes named with: Keeps the dishes whose name holds one of these words, such as burger or
         * margherita. Leave empty for every dish. At most 30 items. Default: [].
         *
         * @param dishWords the values
         * @return this builder
         */
        public Builder dishWords(String... dishWords) {
            return dishWords(Arrays.asList(dishWords));
        }

        /**
         * Include set menus: TheFork's set menus and their prices, beside the à la carte dishes. Default: true.
         *
         * @param includeSetMenus the value
         * @return this builder
         */
        public Builder includeSetMenus(boolean includeSetMenus) {
            values.put("include_set_menus", includeSetMenus);
            return this;
        }

        /**
         * Most dishes per restaurant: The first this many dishes of each menu, in the menu's own order. From 1 to
         * 500. Default: 300.
         *
         * @param maxItemsPerRestaurant the value
         * @return this builder
         */
        public Builder maxItemsPerRestaurant(int maxItemsPerRestaurant) {
            values.put("max_items_per_restaurant", maxItemsPerRestaurant);
            return this;
        }

        /**
         * Most rows: The run stops at this many dishes, price changes first. From 1 to 10000. Default: 2000.
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
        public MenuPriceTrackerParams build() {
            if (!values.containsKey("restaurants")) {
                throw new IllegalStateException("MenuPriceTrackerParams: restaurants is required");
            }
            return new MenuPriceTrackerParams(values);
        }
    }
}
