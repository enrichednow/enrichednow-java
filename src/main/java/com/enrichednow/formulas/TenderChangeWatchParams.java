// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Tender Change Watch ({@code tender-change-watch}): The tenders, grants and awards you watch,
 * read again each run, with a row only when a deadline, status or value changed.
 *
 * Only {@code watch} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * TenderChangeWatchParams params = TenderChangeWatchParams.builder()
 *     .watch("https://www.grants.gov/search-results-detail/361239", "CONT_AWD_FA862118F6251_9700_FA862115D6276_9700")
 *     .build();
 * }</pre>
 */
public final class TenderChangeWatchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "tender-change-watch";

    private final Map<String, Object> values;

    private TenderChangeWatchParams(Map<String, Object> values) {
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
        return "TenderChangeWatchParams" + values;
    }

    /** Builds {@link TenderChangeWatchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Notices to watch: A Grants.gov opportunity's id or link, a USAspending.gov award's id or link, or a UK
         * procurement's Open Contracting id (ocds-…, shown on its Find a Tender or Contracts Finder notice).
         * Anything else is left out. 1 to 150 items. Required.
         *
         * @param watch the values; not null
         * @return this builder
         */
        public Builder watch(List<String> watch) {
            values.put("watch", List.copyOf(watch));
            return this;
        }

        /**
         * Notices to watch: A Grants.gov opportunity's id or link, a USAspending.gov award's id or link, or a UK
         * procurement's Open Contracting id (ocds-…, shown on its Find a Tender or Contracts Finder notice).
         * Anything else is left out. 1 to 150 items. Required.
         *
         * @param watch the values
         * @return this builder
         */
        public Builder watch(String... watch) {
            return watch(Arrays.asList(watch));
        }

        /**
         * UK notices read (days): For UK procurements, how many days of new notices are read for their updates. Run
         * daily, 3 is enough. From 1 to 30. Default: 7.
         *
         * @param ukDays the value
         * @return this builder
         */
        public Builder ukDays(int ukDays) {
            values.put("uk_days", ukDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many changed notices. From 1 to 1000. Default: 200.
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
        public TenderChangeWatchParams build() {
            if (!values.containsKey("watch")) {
                throw new IllegalStateException("TenderChangeWatchParams: watch is required");
            }
            return new TenderChangeWatchParams(values);
        }
    }
}
