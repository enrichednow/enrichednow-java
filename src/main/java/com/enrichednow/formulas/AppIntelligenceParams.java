// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of App Intelligence ({@code app-intelligence}): Ratings, versions, release notes and chart ranks
 * of apps on the App Store and Google Play, with what changed since last run.
 *
 * Only {@code apps} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * AppIntelligenceParams params = AppIntelligenceParams.builder()
 *     .apps("324684580", "com.spotify.music")
 *     .build();
 * }</pre>
 */
public final class AppIntelligenceParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "app-intelligence";

    private final Map<String, Object> values;

    private AppIntelligenceParams(Map<String, Object> values) {
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
        return "AppIntelligenceParams" + values;
    }

    /** Builds {@link AppIntelligenceParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Apps: App Store apps by their number (the digits after id in the app's address) or their App Store link;
         * Google Play apps by their package name, such as com.spotify.music. 1 to 100 items. Required.
         *
         * @param apps the values; not null
         * @return this builder
         */
        public Builder apps(List<String> apps) {
            values.put("apps", List.copyOf(apps));
            return this;
        }

        /**
         * Apps: App Store apps by their number (the digits after id in the app's address) or their App Store link;
         * Google Play apps by their package name, such as com.spotify.music. 1 to 100 items. Required.
         *
         * @param apps the values
         * @return this builder
         */
        public Builder apps(String... apps) {
            return apps(Arrays.asList(apps));
        }

        /**
         * Search words: Also the apps that rank in the stores' search for these words, such as habit tracker. At
         * most 10 items. Default: [].
         *
         * @param searchTerms the values; not null
         * @return this builder
         */
        public Builder searchTerms(List<String> searchTerms) {
            values.put("search_terms", List.copyOf(searchTerms));
            return this;
        }

        /**
         * Search words: Also the apps that rank in the stores' search for these words, such as habit tracker. At
         * most 10 items. Default: [].
         *
         * @param searchTerms the values
         * @return this builder
         */
        public Builder searchTerms(String... searchTerms) {
            return searchTerms(Arrays.asList(searchTerms));
        }

        /**
         * Top chart: Also the top apps of this category's chart, with their rank. An app you named gets its rank
         * when it is on the chart. One of "none", "all", "books", "business", "education", "entertainment",
         * "finance", "food_and_drink", "games", "health_and_fitness", "lifestyle", "medical", "music",
         * "navigation", "news", "photo_and_video", "productivity", "shopping", "social_networking", "sports",
         * "travel", "utilities", "weather". Default: "none".
         *
         * @param chartCategory the value
         * @return this builder
         */
        public Builder chartCategory(String chartCategory) {
            values.put("chart_category", chartCategory);
            return this;
        }

        /**
         * Stores: The stores searched and charted. Apps you name are read in their own store. Each one of
         * "app_store", "google_play". At least 1 item. Default: ["app_store", "google_play"].
         *
         * @param stores the values; not null
         * @return this builder
         */
        public Builder stores(List<String> stores) {
            values.put("stores", List.copyOf(stores));
            return this;
        }

        /**
         * Stores: The stores searched and charted. Apps you name are read in their own store. Each one of
         * "app_store", "google_play". At least 1 item. Default: ["app_store", "google_play"].
         *
         * @param stores the values
         * @return this builder
         */
        public Builder stores(String... stores) {
            return stores(Arrays.asList(stores));
        }

        /**
         * Chart: Top free, top paid or top grossing. One of "free", "paid", "grossing". Default: "free".
         *
         * @param chart the value
         * @return this builder
         */
        public Builder chart(String chart) {
            values.put("chart", chart);
            return this;
        }

        /**
         * Chart places read: How many of the chart's top apps are read in each store. From 5 to 100. Default: 25.
         *
         * @param chartSize the value
         * @return this builder
         */
        public Builder chartSize(int chartSize) {
            values.put("chart_size", chartSize);
            return this;
        }

        /**
         * Apps read per search: How many of the top results of each search word are read in each store. From 1 to
         * 50. Default: 10.
         *
         * @param appsPerSearch the value
         * @return this builder
         */
        public Builder appsPerSearch(int appsPerSearch) {
            values.put("apps_per_search", appsPerSearch);
            return this;
        }

        /**
         * Store country: The country's store, as a two-letter code such as us, gb or de. Ratings and charts differ
         * by country. Default: "us".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Lowest rating: Leaves out apps rated below this. Apps you name are always kept. From 0 to 5. Default: 0.
         *
         * @param minRating the value
         * @return this builder
         */
        public Builder minRating(double minRating) {
            values.put("min_rating", minRating);
            return this;
        }

        /**
         * Recent reviews read per app: Reads this many of each app's newest reviews for its recent rating and the
         * share of one- and two-star reviews. Each review read is one unit; 0 turns it off. From 0 to 100. Default:
         * 0.
         *
         * @param recentReviews the value
         * @return this builder
         */
        public Builder recentReviews(int recentReviews) {
            values.put("recent_reviews", recentReviews);
            return this;
        }

        /**
         * Most rows: The run stops at this many apps, those with the biggest changes first. From 1 to 1000.
         * Default: 200.
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
        public AppIntelligenceParams build() {
            if (!values.containsKey("apps")) {
                throw new IllegalStateException("AppIntelligenceParams: apps is required");
            }
            return new AppIntelligenceParams(values);
        }
    }
}
