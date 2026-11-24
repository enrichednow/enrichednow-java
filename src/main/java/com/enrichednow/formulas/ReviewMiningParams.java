// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Review Mining ({@code review-mining}): The complaints in your products' or competitors' reviews
 * on Target, Walmart and Amazon, by topic, with the buyer's own words.
 *
 * Only {@code products} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * ReviewMiningParams params = ReviewMiningParams.builder()
 *     .products("https://www.target.com/p/-/A-94602824")
 *     .build();
 * }</pre>
 */
public final class ReviewMiningParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "review-mining";

    private final Map<String, Object> values;

    private ReviewMiningParams(Map<String, Object> values) {
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
        return "ReviewMiningParams" + values;
    }

    /** Builds {@link ReviewMiningParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Product pages: The address of each product's page on Target, Walmart, or an Amazon outside the US
         * (amazon.co.uk, .de, .ca ...). 1 to 30 items. Required.
         *
         * @param products the values; not null
         * @return this builder
         */
        public Builder products(List<String> products) {
            values.put("products", List.copyOf(products));
            return this;
        }

        /**
         * Product pages: The address of each product's page on Target, Walmart, or an Amazon outside the US
         * (amazon.co.uk, .de, .ca ...). 1 to 30 items. Required.
         *
         * @param products the values
         * @return this builder
         */
        public Builder products(String... products) {
            return products(Arrays.asList(products));
        }

        /**
         * Topics: Phrases to look for, matched as words and their forms ("broke" also finds "broken"). 1 to 40
         * items. Default: a list of 20 values, which {@code get} on the formula shows.
         *
         * @param topics the values; not null
         * @return this builder
         */
        public Builder topics(List<String> topics) {
            values.put("topics", List.copyOf(topics));
            return this;
        }

        /**
         * Topics: Phrases to look for, matched as words and their forms ("broke" also finds "broken"). 1 to 40
         * items. Default: a list of 20 values, which {@code get} on the formula shows.
         *
         * @param topics the values
         * @return this builder
         */
        public Builder topics(String... topics) {
            return topics(Arrays.asList(topics));
        }

        /**
         * Most stars: Only reviews of this many stars or fewer. 5 reads every review. From 1 to 5. Default: 3.
         *
         * @param maxStars the value
         * @return this builder
         */
        public Builder maxStars(int maxStars) {
            values.put("max_stars", maxStars);
            return this;
        }

        /**
         * Reviews read first: Target and Walmart read their reviews in this order; Amazon reads the page's own. One
         * of "lowest_rating", "newest", "most_helpful". Default: "lowest_rating".
         *
         * @param order the value
         * @return this builder
         */
        public Builder order(String order) {
            values.put("order", order);
            return this;
        }

        /**
         * Only reviews that mention a topic. Default: true.
         *
         * @param onlyWithTopics the value
         * @return this builder
         */
        public Builder onlyWithTopics(boolean onlyWithTopics) {
            values.put("only_with_topics", onlyWithTopics);
            return this;
        }

        /**
         * Most rows: The run stops at this many reviews, the most topics first. From 1 to 2000. Default: 300.
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
        public ReviewMiningParams build() {
            if (!values.containsKey("products")) {
                throw new IllegalStateException("ReviewMiningParams: products is required");
            }
            return new ReviewMiningParams(values);
        }
    }
}
