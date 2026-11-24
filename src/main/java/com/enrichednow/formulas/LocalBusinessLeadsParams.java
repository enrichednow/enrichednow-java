// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Local Business Leads ({@code local-business-leads}): Businesses of a kind in your places, with
 * the email their own website publishes and facts to open with.
 *
 * Only {@code business_types}, {@code locations} are required: {@link Builder#build()} refuses to build without
 * them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * LocalBusinessLeadsParams params = LocalBusinessLeadsParams.builder()
 *     .businessTypes("dentist")
 *     .locations("Austin, TX")
 *     .build();
 * }</pre>
 */
public final class LocalBusinessLeadsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "local-business-leads";

    private final Map<String, Object> values;

    private LocalBusinessLeadsParams(Map<String, Object> values) {
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
        return "LocalBusinessLeadsParams" + values;
    }

    /** Builds {@link LocalBusinessLeadsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Kinds of business: What to search for, as you would type it into Google Maps, such as roofer or vegan
         * restaurant. 1 to 10 items. Required.
         *
         * @param businessTypes the values; not null
         * @return this builder
         */
        public Builder businessTypes(List<String> businessTypes) {
            values.put("business_types", List.copyOf(businessTypes));
            return this;
        }

        /**
         * Kinds of business: What to search for, as you would type it into Google Maps, such as roofer or vegan
         * restaurant. 1 to 10 items. Required.
         *
         * @param businessTypes the values
         * @return this builder
         */
        public Builder businessTypes(String... businessTypes) {
            return businessTypes(Arrays.asList(businessTypes));
        }

        /**
         * Places: A city, district or region for each place, with its state or country when the name is common. 1
         * to 10 items. Required.
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Places: A city, district or region for each place, with its state or country when the name is common. 1
         * to 10 items. Required.
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Search each neighbourhood too: Searches again in the neighbourhoods and postcodes the first search found,
         * to reach past its few hundred results. Default: true.
         *
         * @param expandLocations the value
         * @return this builder
         */
        public Builder expandLocations(boolean expandLocations) {
            values.put("expand_locations", expandLocations);
            return this;
        }

        /**
         * Neighbourhoods per search: How many neighbourhoods or postcodes to search again, those with the most
         * places first. From 1 to 40. Default: 8.
         *
         * @param maxSubAreas the value
         * @return this builder
         */
        public Builder maxSubAreas(int maxSubAreas) {
            values.put("max_sub_areas", maxSubAreas);
            return this;
        }

        /**
         * Places read per search: How many places to read from each search. From 10 to 300. Default: 60.
         *
         * @param placesPerSearch the value
         * @return this builder
         */
        public Builder placesPerSearch(int placesPerSearch) {
            values.put("places_per_search", placesPerSearch);
            return this;
        }

        /**
         * Website: Keep every business, only those with a website, or only those without one. One of "any", "has",
         * "missing". Default: "any".
         *
         * @param website the value
         * @return this builder
         */
        public Builder website(String website) {
            values.put("website", website);
            return this;
        }

        /**
         * Lowest rating: Businesses rated below this are left out. 0 keeps those with no rating. From 0 to 5.
         * Default: 0.
         *
         * @param minRating the value
         * @return this builder
         */
        public Builder minRating(double minRating) {
            values.put("min_rating", minRating);
            return this;
        }

        /**
         * Highest rating: Businesses rated above this are left out. From 0 to 5. Default: 5.
         *
         * @param maxRating the value
         * @return this builder
         */
        public Builder maxRating(double maxRating) {
            values.put("max_rating", maxRating);
            return this;
        }

        /**
         * Fewest reviews: Businesses with fewer reviews are left out. At least 0. Default: 0.
         *
         * @param minReviews the value
         * @return this builder
         */
        public Builder minReviews(int minReviews) {
            values.put("min_reviews", minReviews);
            return this;
        }

        /**
         * Most reviews: Businesses with more reviews are left out. At least 0. Default: 1000000.
         *
         * @param maxReviews the value
         * @return this builder
         */
        public Builder maxReviews(int maxReviews) {
            values.put("max_reviews", maxReviews);
            return this;
        }

        /**
         * Leave out chains: A name found five or more times in the results is taken for a chain and left out.
         * Default: true.
         *
         * @param excludeChains the value
         * @return this builder
         */
        public Builder excludeChains(boolean excludeChains) {
            values.put("exclude_chains", excludeChains);
            return this;
        }

        /**
         * Only businesses with an email: Keeps the businesses whose own website publishes an email address that
         * passes the check. Default: true.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Only named addresses: Leaves out shared inboxes such as info{@literal @} and keeps addresses that passed
         * every check. Default: false.
         *
         * @param excludeRoleAddresses the value
         * @return this builder
         */
        public Builder excludeRoleAddresses(boolean excludeRoleAddresses) {
            values.put("exclude_role_addresses", excludeRoleAddresses);
            return this;
        }

        /**
         * Only businesses with social profiles: Keeps the businesses whose website links to at least one social
         * profile. Default: false.
         *
         * @param requireSocials the value
         * @return this builder
         */
        public Builder requireSocials(boolean requireSocials) {
            values.put("require_socials", requireSocials);
            return this;
        }

        /**
         * Site facts: What each website is built on, and its speed score on a phone. Default: false.
         *
         * @param includeSiteFacts the value
         * @return this builder
         */
        public Builder includeSiteFacts(boolean includeSiteFacts) {
            values.put("include_site_facts", includeSiteFacts);
            return this;
        }

        /**
         * Slowest site score kept: With site facts, keeps the sites scoring this or less; 50 keeps the slow ones.
         * From 0 to 100. Default: 100.
         *
         * @param maxSpeedScore the value
         * @return this builder
         */
        public Builder maxSpeedScore(int maxSpeedScore) {
            values.put("max_speed_score", maxSpeedScore);
            return this;
        }

        /**
         * Company facts: Industry and size from the company's LinkedIn page, when its website links to one.
         * Default: false.
         *
         * @param includeFirmographics the value
         * @return this builder
         */
        public Builder includeFirmographics(boolean includeFirmographics) {
            values.put("include_firmographics", includeFirmographics);
            return this;
        }

        /**
         * Show a business again after (days): For a saved formula, how long before a delivered business may come
         * back. From 1 to 365. Default: 180.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many businesses, most reviewed first. From 1 to 5000. Default: 500.
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
        public LocalBusinessLeadsParams build() {
            if (!values.containsKey("business_types")) {
                throw new IllegalStateException("LocalBusinessLeadsParams: business_types is required");
            }
            if (!values.containsKey("locations")) {
                throw new IllegalStateException("LocalBusinessLeadsParams: locations is required");
            }
            return new LocalBusinessLeadsParams(values);
        }
    }
}
