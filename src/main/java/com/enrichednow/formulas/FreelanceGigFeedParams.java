// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Freelance Gig Feed ({@code freelance-gig-feed}): New projects on Upwork and Freelancer that
 * match your skills, with budget and applicants, only the ones you have not seen.
 *
 * Only {@code keywords} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * FreelanceGigFeedParams params = FreelanceGigFeedParams.builder()
 *     .keywords("Shopify", "web scraping")
 *     .build();
 * }</pre>
 */
public final class FreelanceGigFeedParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "freelance-gig-feed";

    private final Map<String, Object> values;

    private FreelanceGigFeedParams(Map<String, Object> values) {
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
        return "FreelanceGigFeedParams" + values;
    }

    /** Builds {@link FreelanceGigFeedParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Keywords: The skills or kinds of project to look for. Each is searched, and a project must mention one of
         * them. 1 to 10 items. Required.
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Keywords: The skills or kinds of project to look for. Each is searched, and a project must mention one of
         * them. 1 to 10 items. Required.
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Posted within (days): Only projects posted in the last this many days. From 1 to 30. Default: 2.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * Least fixed budget: A fixed-price project must offer at least this much, in its own currency. A project
         * with no budget shown is kept. At least 0. Default: 0.
         *
         * @param minFixedBudget the value
         * @return this builder
         */
        public Builder minFixedBudget(double minFixedBudget) {
            values.put("min_fixed_budget", minFixedBudget);
            return this;
        }

        /**
         * Least hourly rate: An hourly project must offer up to at least this much an hour, in its own currency.
         * One with no rate shown is kept. At least 0. Default: 0.
         *
         * @param minHourlyRate the value
         * @return this builder
         */
        public Builder minHourlyRate(double minHourlyRate) {
            values.put("min_hourly_rate", minHourlyRate);
            return this;
        }

        /**
         * Marketplaces: Where to look. Each one of "upwork", "freelancer". At least 1 item. Default: ["upwork",
         * "freelancer"].
         *
         * @param platforms the values; not null
         * @return this builder
         */
        public Builder platforms(List<String> platforms) {
            values.put("platforms", List.copyOf(platforms));
            return this;
        }

        /**
         * Marketplaces: Where to look. Each one of "upwork", "freelancer". At least 1 item. Default: ["upwork",
         * "freelancer"].
         *
         * @param platforms the values
         * @return this builder
         */
        public Builder platforms(String... platforms) {
            return platforms(Arrays.asList(platforms));
        }

        /**
         * How it pays: Fixed price, hourly, or leave empty for both. Each one of "fixed", "hourly". Default: [].
         *
         * @param paymentTypes the values; not null
         * @return this builder
         */
        public Builder paymentTypes(List<String> paymentTypes) {
            values.put("payment_types", List.copyOf(paymentTypes));
            return this;
        }

        /**
         * How it pays: Fixed price, hourly, or leave empty for both. Each one of "fixed", "hourly". Default: [].
         *
         * @param paymentTypes the values
         * @return this builder
         */
        public Builder paymentTypes(String... paymentTypes) {
            return paymentTypes(Arrays.asList(paymentTypes));
        }

        /**
         * Words to leave out: A project whose title or description holds one of these is left out, such as
         * wordpress or unpaid. Default: [].
         *
         * @param excludeKeywords the values; not null
         * @return this builder
         */
        public Builder excludeKeywords(List<String> excludeKeywords) {
            values.put("exclude_keywords", List.copyOf(excludeKeywords));
            return this;
        }

        /**
         * Words to leave out: A project whose title or description holds one of these is left out, such as
         * wordpress or unpaid. Default: [].
         *
         * @param excludeKeywords the values
         * @return this builder
         */
        public Builder excludeKeywords(String... excludeKeywords) {
            return excludeKeywords(Arrays.asList(excludeKeywords));
        }

        /**
         * Most bids so far: Leave out a project with more bids than this, where the count is known. 0 for any. At
         * least 0. Default: 0.
         *
         * @param maxApplicants the value
         * @return this builder
         */
        public Builder maxApplicants(int maxApplicants) {
            values.put("max_applicants", maxApplicants);
            return this;
        }

        /**
         * Show a project again after (days): For a saved formula, how long before a delivered project may come
         * back. From 1 to 365. Default: 30.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, best first. From 1 to 1000. Default: 100.
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
        public FreelanceGigFeedParams build() {
            if (!values.containsKey("keywords")) {
                throw new IllegalStateException("FreelanceGigFeedParams: keywords is required");
            }
            return new FreelanceGigFeedParams(values);
        }
    }
}
