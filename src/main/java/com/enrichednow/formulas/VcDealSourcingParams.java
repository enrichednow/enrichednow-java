// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of VC Deal Sourcing ({@code vc-deal-sourcing}): Young startups gaining traction on GitHub, Hacker
 * News and Y Combinator, one row each with its signals and website.
 *
 * A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * VcDealSourcingParams params = VcDealSourcingParams.builder()
 *     .build();
 * }</pre>
 */
public final class VcDealSourcingParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "vc-deal-sourcing";

    private final Map<String, Object> values;

    private VcDealSourcingParams(Map<String, Object> values) {
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
        return "VcDealSourcingParams" + values;
    }

    /** Builds {@link VcDealSourcingParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Topics: What you invest in, as words a startup would describe itself with (agents, database, robotics).
         * Leave empty for everything. At most 10 items. Default: [].
         *
         * @param topics the values; not null
         * @return this builder
         */
        public Builder topics(List<String> topics) {
            values.put("topics", List.copyOf(topics));
            return this;
        }

        /**
         * Topics: What you invest in, as words a startup would describe itself with (agents, database, robotics).
         * Leave empty for everything. At most 10 items. Default: [].
         *
         * @param topics the values
         * @return this builder
         */
        public Builder topics(String... topics) {
            return topics(Arrays.asList(topics));
        }

        /**
         * Within the last (days): How new a repository, a Show HN or a Y Combinator launch may be. From 1 to 90.
         * Default: 30.
         *
         * @param withinDays the value
         * @return this builder
         */
        public Builder withinDays(int withinDays) {
            values.put("within_days", withinDays);
            return this;
        }

        /**
         * Where to look: GitHub's new and trending repositories, Show HN on Hacker News, and Y Combinator's
         * directory. Each one of "github", "hacker_news", "ycombinator". At least 1 item. Default: ["github",
         * "hacker_news", "ycombinator"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Where to look: GitHub's new and trending repositories, Show HN on Hacker News, and Y Combinator's
         * directory. Each one of "github", "hacker_news", "ycombinator". At least 1 item. Default: ["github",
         * "hacker_news", "ycombinator"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Fewest GitHub stars: A repository with fewer stars is left out. From 0 to 100000. Default: 200.
         *
         * @param minStars the value
         * @return this builder
         */
        public Builder minStars(int minStars) {
            values.put("min_stars", minStars);
            return this;
        }

        /**
         * Fewest Show HN points: A Show HN with fewer points is left out. From 0 to 10000. Default: 50.
         *
         * @param minHnPoints the value
         * @return this builder
         */
        public Builder minHnPoints(int minHnPoints) {
            values.put("min_hn_points", minHnPoints);
            return this;
        }

        /**
         * Leave out: GitHub accounts, domains or company names to leave out; by default, large companies whose
         * repositories trend. At most 1000 items. Default: a list of 47 values, which {@code get} on the formula
         * shows.
         *
         * @param exclude the values; not null
         * @return this builder
         */
        public Builder exclude(List<String> exclude) {
            values.put("exclude", List.copyOf(exclude));
            return this;
        }

        /**
         * Leave out: GitHub accounts, domains or company names to leave out; by default, large companies whose
         * repositories trend. At most 1000 items. Default: a list of 47 values, which {@code get} on the formula
         * shows.
         *
         * @param exclude the values
         * @return this builder
         */
        public Builder exclude(String... exclude) {
            return exclude(Arrays.asList(exclude));
        }

        /**
         * Results read per search: How many repositories, Show HN posts and Y Combinator companies to read for each
         * topic. From 5 to 100. Default: 30.
         *
         * @param resultsPerSearch the value
         * @return this builder
         */
        public Builder resultsPerSearch(int resultsPerSearch) {
            values.put("results_per_search", resultsPerSearch);
            return this;
        }

        /**
         * Most rows: The run stops at this many startups, strongest signals first. From 1 to 2000. Default: 200.
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
        public VcDealSourcingParams build() {
            return new VcDealSourcingParams(values);
        }
    }
}
