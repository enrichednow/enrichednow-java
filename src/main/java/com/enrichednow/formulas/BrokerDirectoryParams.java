// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Broker Directory ({@code broker-directory}): The business brokers of your states, one row for
 * each firm, with specialties, designations and their current listings.
 *
 * Only {@code states} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * BrokerDirectoryParams params = BrokerDirectoryParams.builder()
 *     .states("Texas")
 *     .build();
 * }</pre>
 */
public final class BrokerDirectoryParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "broker-directory";

    private final Map<String, Object> values;

    private BrokerDirectoryParams(Map<String, Object> values) {
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
        return "BrokerDirectoryParams" + values;
    }

    /** Builds {@link BrokerDirectoryParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * States: US states or Canadian provinces, by name or two-letter code. Listings are read for US states
         * only. 1 to 10 items. Required.
         *
         * @param states the values; not null
         * @return this builder
         */
        public Builder states(List<String> states) {
            values.put("states", List.copyOf(states));
            return this;
        }

        /**
         * States: US states or Canadian provinces, by name or two-letter code. Listings are read for US states
         * only. 1 to 10 items. Required.
         *
         * @param states the values
         * @return this builder
         */
        public Builder states(String... states) {
            return states(Arrays.asList(states));
        }

        /**
         * Specialties: IBBA brokers who list one of these industries. Empty keeps every broker. Each one of
         * "accommodations", "agriculture", "arts", "auto-related-business", "business-services", "construction",
         * "ecommerce", "education-services", "entertainment-recreation", "finance-insurance", "food-beverage",
         * "health-care-social-assistance", "industrial-services", "information", "logistics", "manufacturing",
         * "print-and-media", "public-administration", "real-estate-rental-leasing", "retail-trade",
         * "technical-services", "transportation", "waste-management", "wholesale-trade". Default: [].
         *
         * @param specialties the values; not null
         * @return this builder
         */
        public Builder specialties(List<String> specialties) {
            values.put("specialties", List.copyOf(specialties));
            return this;
        }

        /**
         * Specialties: IBBA brokers who list one of these industries. Empty keeps every broker. Each one of
         * "accommodations", "agriculture", "arts", "auto-related-business", "business-services", "construction",
         * "ecommerce", "education-services", "entertainment-recreation", "finance-insurance", "food-beverage",
         * "health-care-social-assistance", "industrial-services", "information", "logistics", "manufacturing",
         * "print-and-media", "public-administration", "real-estate-rental-leasing", "retail-trade",
         * "technical-services", "transportation", "waste-management", "wholesale-trade". Default: [].
         *
         * @param specialties the values
         * @return this builder
         */
        public Builder specialties(String... specialties) {
            return specialties(Arrays.asList(specialties));
        }

        /**
         * Designation: IBBA brokers holding this designation, such as CBI (Certified Business Intermediary) or
         * M&amp;AMI. One of "any", "cbi", "mami", "master_cbi", "cbi_or_mami". Default: "any".
         *
         * @param certification the value
         * @return this builder
         */
        public Builder certification(String certification) {
            values.put("certification", certification);
            return this;
        }

        /**
         * Add the firms selling businesses now: Reads the state's businesses for sale on BusinessesForSale.com, and
         * each brokered one's page for its firm. Default: true.
         *
         * @param includeListings the value
         * @return this builder
         */
        public Builder includeListings(boolean includeListings) {
            values.put("include_listings", includeListings);
            return this;
        }

        /**
         * Listing pages read a state: BusinessesForSale.com's pages of 25, newest first. From 1 to 4. Default: 2.
         *
         * @param listingPages the value
         * @return this builder
         */
        public Builder listingPages(int listingPages) {
            values.put("listing_pages", listingPages);
            return this;
        }

        /**
         * Fewest listings now: Keep firms with at least this many of the listings read. 0 keeps every broker. At
         * least 0. Default: 0.
         *
         * @param minListings the value
         * @return this builder
         */
        public Builder minListings(int minListings) {
            values.put("min_listings", minListings);
            return this;
        }

        /**
         * Only brokers who name a firm: Leaves out IBBA members whose profile names no firm. Default: false.
         *
         * @param firmsOnly the value
         * @return this builder
         */
        public Builder firmsOnly(boolean firmsOnly) {
            values.put("firms_only", firmsOnly);
            return this;
        }

        /**
         * Read each website for an address: The address each firm's own website publishes, checked. Off saves one
         * read a firm. Default: true.
         *
         * @param findEmail the value
         * @return this builder
         */
        public Builder findEmail(boolean findEmail) {
            values.put("find_email", findEmail);
            return this;
        }

        /**
         * Show a firm again after (days): For a saved formula, how long before a delivered firm may come back. From
         * 1 to 365. Default: 180.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many firms, those with the most listings and brokers first. From 1 to
         * 2000. Default: 200.
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
        public BrokerDirectoryParams build() {
            if (!values.containsKey("states")) {
                throw new IllegalStateException("BrokerDirectoryParams: states is required");
            }
            return new BrokerDirectoryParams(values);
        }
    }
}
