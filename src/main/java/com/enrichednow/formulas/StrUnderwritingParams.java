// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Short-Term Rental Comps ({@code str-underwriting}): Airbnb and Vrbo rentals in an area for your
 * dates, with nightly rate, rating, reviews and the area's average rate.
 *
 * Only {@code location}, {@code checkin}, {@code checkout} are required: {@link Builder#build()} refuses to build
 * without them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * StrUnderwritingParams params = StrUnderwritingParams.builder()
 *     .location("Nashville, Tennessee, United States")
 *     .checkin("2027-01-15")
 *     .checkout("2027-01-18")
 *     .build();
 * }</pre>
 */
public final class StrUnderwritingParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "str-underwriting";

    private final Map<String, Object> values;

    private StrUnderwritingParams(Map<String, Object> values) {
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
        return "StrUnderwritingParams" + values;
    }

    /** Builds {@link StrUnderwritingParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Place: Where, as you would type it into Airbnb, with the country, such as "Nashville, Tennessee, United
         * States". Required.
         *
         * @param location the value
         * @return this builder
         */
        public Builder location(String location) {
            values.put("location", location);
            return this;
        }

        /**
         * Check-in: The first night, YYYY-MM-DD. Rates are for this stay. Required.
         *
         * @param checkin the value
         * @return this builder
         */
        public Builder checkin(String checkin) {
            values.put("checkin", checkin);
            return this;
        }

        /**
         * Check-out: The day of departure, YYYY-MM-DD. Required.
         *
         * @param checkout the value
         * @return this builder
         */
        public Builder checkout(String checkout) {
            values.put("checkout", checkout);
            return this;
        }

        /**
         * Guests: Adults staying; rentals too small for them are not offered. From 1 to 16. Default: 2.
         *
         * @param guests the value
         * @return this builder
         */
        public Builder guests(int guests) {
            values.put("guests", guests);
            return this;
        }

        /**
         * Platforms: Where to look. Each one of "airbnb", "vrbo". At least 1 item. Default: ["airbnb", "vrbo"].
         *
         * @param platforms the values; not null
         * @return this builder
         */
        public Builder platforms(List<String> platforms) {
            values.put("platforms", List.copyOf(platforms));
            return this;
        }

        /**
         * Platforms: Where to look. Each one of "airbnb", "vrbo". At least 1 item. Default: ["airbnb", "vrbo"].
         *
         * @param platforms the values
         * @return this builder
         */
        public Builder platforms(String... platforms) {
            return platforms(Arrays.asList(platforms));
        }

        /**
         * Airbnb currency: The currency Airbnb shows prices in. Vrbo shows the destination's own. One of "USD",
         * "EUR", "GBP", "CAD", "AUD". Default: "USD".
         *
         * @param currency the value
         * @return this builder
         */
        public Builder currency(String currency) {
            values.put("currency", currency);
            return this;
        }

        /**
         * Least reviews: Leave out rentals with fewer reviews, such as new listings. At least 0. Default: 0.
         *
         * @param minReviews the value
         * @return this builder
         */
        public Builder minReviews(int minReviews) {
            values.put("min_reviews", minReviews);
            return this;
        }

        /**
         * Most rows: The run stops at this many rentals, most reviewed first. From 1 to 500. Default: 150.
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
        public StrUnderwritingParams build() {
            if (!values.containsKey("location")) {
                throw new IllegalStateException("StrUnderwritingParams: location is required");
            }
            if (!values.containsKey("checkin")) {
                throw new IllegalStateException("StrUnderwritingParams: checkin is required");
            }
            if (!values.containsKey("checkout")) {
                throw new IllegalStateException("StrUnderwritingParams: checkout is required");
            }
            return new StrUnderwritingParams(values);
        }
    }
}
