// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Review Velocity ({@code review-velocity}): Ratings and reviews of the apps, companies and games
 * you follow, with how many were added since the last run and per day.
 *
 * Only {@code apps} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * ReviewVelocityParams params = ReviewVelocityParams.builder()
 *     .apps("368677368", "com.ubercab")
 *     .build();
 * }</pre>
 */
public final class ReviewVelocityParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "review-velocity";

    private final Map<String, Object> values;

    private ReviewVelocityParams(Map<String, Object> values) {
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
        return "ReviewVelocityParams" + values;
    }

    /** Builds {@link ReviewVelocityParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Apps: An App Store app's address or its number (368677368 for Uber), or a Google Play app's package name,
         * as its address shows it after id= (com.ubercab). An app on both stores is given twice, once for each. At
         * most 50 items. Required.
         *
         * @param apps the values; not null
         * @return this builder
         */
        public Builder apps(List<String> apps) {
            values.put("apps", List.copyOf(apps));
            return this;
        }

        /**
         * Apps: An App Store app's address or its number (368677368 for Uber), or a Google Play app's package name,
         * as its address shows it after id= (com.ubercab). An app on both stores is given twice, once for each. At
         * most 50 items. Required.
         *
         * @param apps the values
         * @return this builder
         */
        public Builder apps(String... apps) {
            return apps(Arrays.asList(apps));
        }

        /**
         * Companies on Trustpilot: The company's website domain, such as chewy.com, for its Trustpilot reviews. At
         * most 50 items. Default: [].
         *
         * @param companies the values; not null
         * @return this builder
         */
        public Builder companies(List<String> companies) {
            values.put("companies", List.copyOf(companies));
            return this;
        }

        /**
         * Companies on Trustpilot: The company's website domain, such as chewy.com, for its Trustpilot reviews. At
         * most 50 items. Default: [].
         *
         * @param companies the values
         * @return this builder
         */
        public Builder companies(String... companies) {
            return companies(Arrays.asList(companies));
        }

        /**
         * Games on Steam: A Steam game's number or its store address, such as 1091500 for Cyberpunk 2077. At most
         * 50 items. Default: [].
         *
         * @param games the values; not null
         * @return this builder
         */
        public Builder games(List<String> games) {
            values.put("games", List.copyOf(games));
            return this;
        }

        /**
         * Games on Steam: A Steam game's number or its store address, such as 1091500 for Cyberpunk 2077. At most
         * 50 items. Default: [].
         *
         * @param games the values
         * @return this builder
         */
        public Builder games(String... games) {
            return games(Arrays.asList(games));
        }

        /**
         * Store country: The country whose App Store, Google Play and Steam store are read, as a two-letter code.
         * Their counts and ratings are per country. Default: "us".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, the biggest moves first. From 1 to 200. Default: 200.
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
        public ReviewVelocityParams build() {
            if (!values.containsKey("apps")) {
                throw new IllegalStateException("ReviewVelocityParams: apps is required");
            }
            return new ReviewVelocityParams(values);
        }
    }
}
