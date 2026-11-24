// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Competitor Review Mining ({@code competitor-review-mining}): Your competitors' newest one- and
 * two-star reviews, each with what went wrong, to sell against and to win their customers.
 *
 * Only {@code competitors} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * CompetitorReviewMiningParams params = CompetitorReviewMiningParams.builder()
 *     .competitors("gusto.com", "paychex.com")
 *     .build();
 * }</pre>
 */
public final class CompetitorReviewMiningParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "competitor-review-mining";

    private final Map<String, Object> values;

    private CompetitorReviewMiningParams(Map<String, Object> values) {
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
        return "CompetitorReviewMiningParams" + values;
    }

    /** Builds {@link CompetitorReviewMiningParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Competitors: Each competitor's website domain, as Trustpilot lists it, such as gusto.com. 1 to 20 items.
         * Required.
         *
         * @param competitors the values; not null
         * @return this builder
         */
        public Builder competitors(List<String> competitors) {
            values.put("competitors", List.copyOf(competitors));
            return this;
        }

        /**
         * Competitors: Each competitor's website domain, as Trustpilot lists it, such as gusto.com. 1 to 20 items.
         * Required.
         *
         * @param competitors the values
         * @return this builder
         */
        public Builder competitors(String... competitors) {
            return competitors(Arrays.asList(competitors));
        }

        /**
         * Stars: Which reviews to read, by their stars. At least 1 item. Default: [1, 2].
         *
         * @param stars the values; not null
         * @return this builder
         */
        public Builder stars(List<Integer> stars) {
            values.put("stars", List.copyOf(stars));
            return this;
        }

        /**
         * Stars: Which reviews to read, by their stars. At least 1 item. Default: [1, 2].
         *
         * @param stars the values
         * @return this builder
         */
        public Builder stars(Integer... stars) {
            return stars(Arrays.asList(stars));
        }

        /**
         * Phrases to look for: Words you can answer, such as "customer service" or "hidden fees"; each match is
         * quoted. At most 30 items. Default: [].
         *
         * @param phrases the values; not null
         * @return this builder
         */
        public Builder phrases(List<String> phrases) {
            values.put("phrases", List.copyOf(phrases));
            return this;
        }

        /**
         * Phrases to look for: Words you can answer, such as "customer service" or "hidden fees"; each match is
         * quoted. At most 30 items. Default: [].
         *
         * @param phrases the values
         * @return this builder
         */
        public Builder phrases(String... phrases) {
            return phrases(Arrays.asList(phrases));
        }

        /**
         * Written within (days): Only reviews written in the last this many days. From 1 to 730. Default: 180.
         *
         * @param publishedWithinDays the value
         * @return this builder
         */
        public Builder publishedWithinDays(int publishedWithinDays) {
            values.put("published_within_days", publishedWithinDays);
            return this;
        }

        /**
         * Pages per competitor: Twenty reviews a page, newest first. From 1 to 3. Default: 2.
         *
         * @param pagesPerCompetitor the value
         * @return this builder
         */
        public Builder pagesPerCompetitor(int pagesPerCompetitor) {
            values.put("pages_per_competitor", pagesPerCompetitor);
            return this;
        }

        /**
         * Only reviews with a phrase: Keeps only the reviews that contain one of your phrases. Default: false.
         *
         * @param onlyMatching the value
         * @return this builder
         */
        public Builder onlyMatching(boolean onlyMatching) {
            values.put("only_matching", onlyMatching);
            return this;
        }

        /**
         * Show a review again after (days): For a saved formula, a review once delivered is not delivered again
         * before this. From 1 to 365. Default: 365.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many reviews, the best matches first. From 1 to 2000. Default: 200.
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
        public CompetitorReviewMiningParams build() {
            if (!values.containsKey("competitors")) {
                throw new IllegalStateException("CompetitorReviewMiningParams: competitors is required");
            }
            return new CompetitorReviewMiningParams(values);
        }
    }
}
