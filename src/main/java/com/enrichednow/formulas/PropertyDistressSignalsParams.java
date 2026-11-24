// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Property Distress Signals ({@code property-distress-signals}): Properties in a US city with
 * recent code violations, distress permits or a stale listing, stacked by address and ranked.
 *
 * Only {@code city} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * PropertyDistressSignalsParams params = PropertyDistressSignalsParams.builder()
 *     .city("austin")
 *     .build();
 * }</pre>
 */
public final class PropertyDistressSignalsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "property-distress-signals";

    private final Map<String, Object> values;

    private PropertyDistressSignalsParams(Map<String, Object> values) {
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
        return "PropertyDistressSignalsParams" + values;
    }

    /** Builds {@link PropertyDistressSignalsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * City: The US city or county whose open data is read. These publish both code cases and permits. One of
         * "austin", "chicago", "new_york", "los_angeles", "seattle", "new_orleans", "cincinnati",
         * "montgomery_county_md", "norfolk", "buffalo". Required.
         *
         * @param city the value
         * @return this builder
         */
        public Builder city(String city) {
            values.put("city", city);
            return this;
        }

        /**
         * Opened or issued within (days): Only cases opened, and permits issued, in the last this many days. From 1
         * to 365. Default: 60.
         *
         * @param withinDays the value
         * @return this builder
         */
        public Builder withinDays(int withinDays) {
            values.put("within_days", withinDays);
            return this;
        }

        /**
         * Least lists a property is on: 2 keeps only properties on two lists or more (a code case and a distress
         * permit, or either and a listing). From 1 to 3. Default: 1.
         *
         * @param minLists the value
         * @return this builder
         */
        public Builder minLists(int minLists) {
            values.put("min_lists", minLists);
            return this;
        }

        /**
         * Case words: Only cases whose type or description holds one of these, such as structure, vacant or
         * abatement. Each word is one search; empty reads the newest cases of any kind. At most 8 items. Default:
         * [].
         *
         * @param caseKeywords the values; not null
         * @return this builder
         */
        public Builder caseKeywords(List<String> caseKeywords) {
            values.put("case_keywords", List.copyOf(caseKeywords));
            return this;
        }

        /**
         * Case words: Only cases whose type or description holds one of these, such as structure, vacant or
         * abatement. Each word is one search; empty reads the newest cases of any kind. At most 8 items. Default:
         * [].
         *
         * @param caseKeywords the values
         * @return this builder
         */
        public Builder caseKeywords(String... caseKeywords) {
            return caseKeywords(Arrays.asList(caseKeywords));
        }

        /**
         * Only cases still open: Leave out cases the city has closed. Default: false.
         *
         * @param openCasesOnly the value
         * @return this builder
         */
        public Builder openCasesOnly(boolean openCasesOnly) {
            values.put("open_cases_only", openCasesOnly);
            return this;
        }

        /**
         * Distress permit words: Permits whose type, work or description holds one of these are a signal. Each word
         * is one search. Empty leaves permits out. At most 8 items. Default: ["demoli", "fire damage", "vacant",
         * "unsafe"].
         *
         * @param permitKeywords the values; not null
         * @return this builder
         */
        public Builder permitKeywords(List<String> permitKeywords) {
            values.put("permit_keywords", List.copyOf(permitKeywords));
            return this;
        }

        /**
         * Distress permit words: Permits whose type, work or description holds one of these are a signal. Each word
         * is one search. Empty leaves permits out. At most 8 items. Default: ["demoli", "fire damage", "vacant",
         * "unsafe"].
         *
         * @param permitKeywords the values
         * @return this builder
         */
        public Builder permitKeywords(String... permitKeywords) {
            return permitKeywords(Arrays.asList(permitKeywords));
        }

        /**
         * Cases read: The newest this many code cases are read. From 10 to 200. Default: 200.
         *
         * @param casesToRead the value
         * @return this builder
         */
        public Builder casesToRead(int casesToRead) {
            values.put("cases_to_read", casesToRead);
            return this;
        }

        /**
         * Permits read per word: The newest this many permits are read for each permit word. From 10 to 200.
         * Default: 100.
         *
         * @param permitsPerWord the value
         * @return this builder
         */
        public Builder permitsPerWord(int permitsPerWord) {
            values.put("permits_per_word", permitsPerWord);
            return this;
        }

        /**
         * Check each property on Zillow: Whether the home is listed, how long and at what price, with its size and
         * value estimate. One lookup a property. Default: true.
         *
         * @param checkListings the value
         * @return this builder
         */
        public Builder checkListings(boolean checkListings) {
            values.put("check_listings", checkListings);
            return this;
        }

        /**
         * Show a property again after (days): For a saved formula, how long before a delivered property may come
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
         * Most rows: The run stops at this many properties, most signals first. From 1 to 1000. Default: 100.
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
        public PropertyDistressSignalsParams build() {
            if (!values.containsKey("city")) {
                throw new IllegalStateException("PropertyDistressSignalsParams: city is required");
            }
            return new PropertyDistressSignalsParams(values);
        }
    }
}
