// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Home-Services Pros ({@code home-services-pros}): The plumbers, electricians or cleaners of a
 * place, with ratings, licences, years in business and an email.
 *
 * Only {@code trade}, {@code city} are required: {@link Builder#build()} refuses to build without them. A value not
 * set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * HomeServicesProsParams params = HomeServicesProsParams.builder()
 *     .trade("plumbers")
 *     .city("Denver")
 *     .build();
 * }</pre>
 */
public final class HomeServicesProsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "home-services-pros";

    private final Map<String, Object> values;

    private HomeServicesProsParams(Map<String, Object> values) {
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
        return "HomeServicesProsParams" + values;
    }

    /** Builds {@link HomeServicesProsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Trade: The trade in the plural, as the sites write it, such as plumbers, electricians or house cleaning.
         * Required.
         *
         * @param trade the value
         * @return this builder
         */
        public Builder trade(String trade) {
            values.put("trade", trade);
            return this;
        }

        /**
         * Town or city: One town or city. Required.
         *
         * @param city the value
         * @return this builder
         */
        public Builder city(String city) {
            values.put("city", city);
            return this;
        }

        /**
         * State or county: For the United States, the two-letter state, which Thumbtack needs. For the United
         * Kingdom, leave empty or give the county. Default: "".
         *
         * @param region the value
         * @return this builder
         */
        public Builder region(String region) {
            values.put("region", region);
            return this;
        }

        /**
         * Country: The United States or the United Kingdom; the marketplaces read depend on it. One of "US", "GB".
         * Default: "US".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Where to look: Each is read only for the country it covers. Each one of "thumbtack", "bbb",
         * "checkatrade", "google_maps". At least 1 item. Default: ["thumbtack", "bbb", "checkatrade",
         * "google_maps"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Where to look: Each is read only for the country it covers. Each one of "thumbtack", "bbb",
         * "checkatrade", "google_maps". At least 1 item. Default: ["thumbtack", "bbb", "checkatrade",
         * "google_maps"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Lowest rating: Out of 5 on most sites and of 10 on Checkatrade; 0 keeps those with no rating. From 0 to
         * 10. Default: 0.
         *
         * @param minRating the value
         * @return this builder
         */
        public Builder minRating(double minRating) {
            values.put("min_rating", minRating);
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
         * Only businesses with an email: Keeps the businesses whose own website publishes an email address that
         * passes the check. Default: false.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Read each marketplace profile: Years in business, licences and background checks on Thumbtack; website
         * and accreditations on Checkatrade. Default: true.
         *
         * @param readProfiles the value
         * @return this builder
         */
        public Builder readProfiles(boolean readProfiles) {
            values.put("read_profiles", readProfiles);
            return this;
        }

        /**
         * Show a business again after (days): For a saved formula, how long before a delivered business may come
         * back. From 1 to 365. Default: 120.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many businesses, most reviewed first. From 1 to 2000. Default: 150.
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
        public HomeServicesProsParams build() {
            if (!values.containsKey("trade")) {
                throw new IllegalStateException("HomeServicesProsParams: trade is required");
            }
            if (!values.containsKey("city")) {
                throw new IllegalStateException("HomeServicesProsParams: city is required");
            }
            return new HomeServicesProsParams(values);
        }
    }
}
