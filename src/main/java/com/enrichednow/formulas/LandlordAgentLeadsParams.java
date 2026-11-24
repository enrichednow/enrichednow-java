// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Letting Agent Leads (UK) ({@code landlord-agent-leads}): The letting agents and letting
 * businesses with the most rentals on Rightmove and OnTheMarket in a UK area, with their branch phone.
 *
 * Only {@code locations} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * LandlordAgentLeadsParams params = LandlordAgentLeadsParams.builder()
 *     .locations("Manchester")
 *     .build();
 * }</pre>
 */
public final class LandlordAgentLeadsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "landlord-agent-leads";

    private final Map<String, Object> values;

    private LandlordAgentLeadsParams(Map<String, Object> values) {
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
        return "LandlordAgentLeadsParams" + values;
    }

    /** Builds {@link LandlordAgentLeadsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Places: UK towns, areas or postcodes, such as Manchester, Leeds or SW11. 1 to 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Places: UK towns, areas or postcodes, such as Manchester, Leeds or SW11. 1 to 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Least rentals on the market: Only agents advertising at least this many of the rentals read. From 1 to
         * 100. Default: 1.
         *
         * @param minListings the value
         * @return this builder
         */
        public Builder minListings(int minListings) {
            values.put("min_listings", minListings);
            return this;
        }

        /**
         * Portals: Where to read the rentals. Each one of "rightmove", "onthemarket". At least 1 item. Default:
         * ["rightmove", "onthemarket"].
         *
         * @param portals the values; not null
         * @return this builder
         */
        public Builder portals(List<String> portals) {
            values.put("portals", List.copyOf(portals));
            return this;
        }

        /**
         * Portals: Where to read the rentals. Each one of "rightmove", "onthemarket". At least 1 item. Default:
         * ["rightmove", "onthemarket"].
         *
         * @param portals the values
         * @return this builder
         */
        public Builder portals(String... portals) {
            return portals(Arrays.asList(portals));
        }

        /**
         * Result pages read: Which pages of each portal's results to read for each place, about 25 rentals a page.
         * Each one of 1, 2, 3, 4, 5. At least 1 item. Default: [1, 2, 3].
         *
         * @param pages the values; not null
         * @return this builder
         */
        public Builder pages(List<Integer> pages) {
            values.put("pages", List.copyOf(pages));
            return this;
        }

        /**
         * Result pages read: Which pages of each portal's results to read for each place, about 25 rentals a page.
         * Each one of 1, 2, 3, 4, 5. At least 1 item. Default: [1, 2, 3].
         *
         * @param pages the values
         * @return this builder
         */
        public Builder pages(Integer... pages) {
            return pages(Arrays.asList(pages));
        }

        /**
         * Radius (miles): Also read rentals this far around each place. One of 0, 0.25, 0.5, 1, 3, 5, 10. Default:
         * 0.
         *
         * @param radiusMiles the value
         * @return this builder
         */
        public Builder radiusMiles(double radiusMiles) {
            values.put("radius_miles", radiusMiles);
            return this;
        }

        /**
         * Most rent a month (GBP): 0 for no ceiling. At least 0. Default: 0.
         *
         * @param maxRent the value
         * @return this builder
         */
        public Builder maxRent(int maxRent) {
            values.put("max_rent", maxRent);
            return this;
        }

        /**
         * Agents to leave out: Agents you already work with, by the name the portal shows. At most 2000 items.
         * Default: [].
         *
         * @param excludeAgents the values; not null
         * @return this builder
         */
        public Builder excludeAgents(List<String> excludeAgents) {
            values.put("exclude_agents", List.copyOf(excludeAgents));
            return this;
        }

        /**
         * Agents to leave out: Agents you already work with, by the name the portal shows. At most 2000 items.
         * Default: [].
         *
         * @param excludeAgents the values
         * @return this builder
         */
        public Builder excludeAgents(String... excludeAgents) {
            return excludeAgents(Arrays.asList(excludeAgents));
        }

        /**
         * Show an agent again after (days): For a saved formula, how long before a delivered agent may come back.
         * From 1 to 365. Default: 60.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many agents, most rentals first. From 1 to 1000. Default: 100.
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
        public LandlordAgentLeadsParams build() {
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("LandlordAgentLeadsParams: locations is required");
            }
            return new LandlordAgentLeadsParams(values);
        }
    }
}
