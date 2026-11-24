// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Permit Status Watch ({@code permit-status-watch}): Building permits in a US city read again on
 * a schedule, with each status change and each newly issued permit.
 *
 * Only {@code city} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * PermitStatusWatchParams params = PermitStatusWatchParams.builder()
 *     .city("los_angeles")
 *     .build();
 * }</pre>
 */
public final class PermitStatusWatchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "permit-status-watch";

    private final Map<String, Object> values;

    private PermitStatusWatchParams(Map<String, Object> values) {
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
        return "PermitStatusWatchParams" + values;
    }

    /** Builds {@link PermitStatusWatchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * City: The US city or county whose permits are watched. One of "austin", "chicago", "new_york",
         * "los_angeles", "san_francisco", "seattle", "dallas", "new_orleans", "cincinnati", "baton_rouge",
         * "montgomery_county_md", "prince_georges_county_md", "norfolk", "buffalo", "orlando", "mesa", "raleigh",
         * "washington_dc", "minneapolis", "detroit". Required.
         *
         * @param city the value
         * @return this builder
         */
        public Builder city(String city) {
            values.put("city", city);
            return this;
        }

        /**
         * Permit numbers: The permits to watch, by their number at the city. Leave empty to watch the permits
         * issued in an area instead. At most 50 items. Default: [].
         *
         * @param permitNumbers the values; not null
         * @return this builder
         */
        public Builder permitNumbers(List<String> permitNumbers) {
            values.put("permit_numbers", List.copyOf(permitNumbers));
            return this;
        }

        /**
         * Permit numbers: The permits to watch, by their number at the city. Leave empty to watch the permits
         * issued in an area instead. At most 50 items. Default: [].
         *
         * @param permitNumbers the values
         * @return this builder
         */
        public Builder permitNumbers(String... permitNumbers) {
            return permitNumbers(Arrays.asList(permitNumbers));
        }

        /**
         * Address holds: Without permit numbers, only permits whose address holds this text, such as a street name.
         * Empty watches the whole city. Default: "".
         *
         * @param addressContains the value
         * @return this builder
         */
        public Builder addressContains(String addressContains) {
            values.put("address_contains", addressContains);
            return this;
        }

        /**
         * Issued within (days): Without permit numbers, the permits issued in the last this many days are watched.
         * From 1 to 365. Default: 30.
         *
         * @param withinDays the value
         * @return this builder
         */
        public Builder withinDays(int withinDays) {
            values.put("within_days", withinDays);
            return this;
        }

        /**
         * Work words: Without permit numbers, only permits whose type, work or description holds this, such as ROOF
         * or SOLAR. Default: "".
         *
         * @param keyword the value
         * @return this builder
         */
        public Builder keyword(String keyword) {
            values.put("keyword", keyword);
            return this;
        }

        /**
         * Permits read: Without permit numbers, the newest this many permits of the area are read on each run. From
         * 10 to 200. Default: 200.
         *
         * @param permitsToRead the value
         * @return this builder
         */
        public Builder permitsToRead(int permitsToRead) {
            values.put("permits_to_read", permitsToRead);
            return this;
        }

        /**
         * Only what changed: On a saved formula's later runs, show only permits that are new or whose status
         * changed. The first run shows every permit. Default: true.
         *
         * @param onlyChanges the value
         * @return this builder
         */
        public Builder onlyChanges(boolean onlyChanges) {
            values.put("only_changes", onlyChanges);
            return this;
        }

        /**
         * Most rows: The run stops at this many permits, changes first. From 1 to 1000. Default: 200.
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
        public PermitStatusWatchParams build() {
            if (!values.containsKey("city")) {
                throw new IllegalStateException("PermitStatusWatchParams: city is required");
            }
            return new PermitStatusWatchParams(values);
        }
    }
}
