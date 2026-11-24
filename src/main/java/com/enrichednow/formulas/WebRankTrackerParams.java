// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Web Rank Tracker ({@code web-rank-tracker}): Where the websites you follow rank in Similarweb's
 * most-visited rankings, by country and category, and how they moved since last run.
 *
 * Only {@code domains} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * WebRankTrackerParams params = WebRankTrackerParams.builder()
 *     .domains("temu.com", "shein.com")
 *     .build();
 * }</pre>
 */
public final class WebRankTrackerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "web-rank-tracker";

    private final Map<String, Object> values;

    private WebRankTrackerParams(Map<String, Object> values) {
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
        return "WebRankTrackerParams" + values;
    }

    /** Builds {@link WebRankTrackerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Websites: The domains to follow, such as etsy.com. 1 to 30 items. Required.
         *
         * @param domains the values; not null
         * @return this builder
         */
        public Builder domains(List<String> domains) {
            values.put("domains", List.copyOf(domains));
            return this;
        }

        /**
         * Websites: The domains to follow, such as etsy.com. 1 to 30 items. Required.
         *
         * @param domains the values
         * @return this builder
         */
        public Builder domains(String... domains) {
            return domains(Arrays.asList(domains));
        }

        /**
         * Countries: The countries whose rankings to read, or worldwide. Each one of "worldwide", "US", "GB", "DE",
         * "FR", "CA", "AU", "JP", "IN", "BR", "MX", "IT", "ES", "NL", "SE", "CH", "KR", "SG", "AE", "SA", "ZA",
         * "TR", "PL", "ID", "PH", "TH", "VN", "IL", "IE", "NZ". 1 to 4 items. Default: ["US"].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Countries: The countries whose rankings to read, or worldwide. Each one of "worldwide", "US", "GB", "DE",
         * "FR", "CA", "AU", "JP", "IN", "BR", "MX", "IT", "ES", "NL", "SE", "CH", "KR", "SG", "AE", "SA", "ZA",
         * "TR", "PL", "ID", "PH", "TH", "VN", "IL", "IE", "NZ". 1 to 4 items. Default: ["US"].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Categories: The rankings to read in each country. All is the ranking of every category together, 50
         * places deep; a category's ranking shows 50 places worldwide and 5 in one country. Each one of "all",
         * "ai-chatbots-and-tools", "arts-and-entertainment", "business-and-consumer-services",
         * "community-and-society", "computers-electronics-and-technology", "crypto-and-blockchain",
         * "e-commerce-and-shopping", "finance", "food-and-drink", "gambling", "games", "health",
         * "heavy-industry-and-engineering", "hobbies-and-leisure", "home-and-garden", "jobs-and-career",
         * "law-and-government", "lifestyle", "news-and-media", "pets-and-animals", "reference-materials",
         * "science-and-education", "sports", "travel-and-tourism", "vehicles". 1 to 4 items. Default: ["all"].
         *
         * @param categories the values; not null
         * @return this builder
         */
        public Builder categories(List<String> categories) {
            values.put("categories", List.copyOf(categories));
            return this;
        }

        /**
         * Categories: The rankings to read in each country. All is the ranking of every category together, 50
         * places deep; a category's ranking shows 50 places worldwide and 5 in one country. Each one of "all",
         * "ai-chatbots-and-tools", "arts-and-entertainment", "business-and-consumer-services",
         * "community-and-society", "computers-electronics-and-technology", "crypto-and-blockchain",
         * "e-commerce-and-shopping", "finance", "food-and-drink", "gambling", "games", "health",
         * "heavy-industry-and-engineering", "hobbies-and-leisure", "home-and-garden", "jobs-and-career",
         * "law-and-government", "lifestyle", "news-and-media", "pets-and-animals", "reference-materials",
         * "science-and-education", "sports", "travel-and-tourism", "vehicles". 1 to 4 items. Default: ["all"].
         *
         * @param categories the values
         * @return this builder
         */
        public Builder categories(String... categories) {
            return categories(Arrays.asList(categories));
        }

        /**
         * Most rows: The run stops at this many rows, the biggest moves first. From 1 to 500. Default: 500.
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
        public WebRankTrackerParams build() {
            if (!values.containsKey("domains")) {
                throw new IllegalStateException("WebRankTrackerParams: domains is required");
            }
            return new WebRankTrackerParams(values);
        }
    }
}
