// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of SEC Filing Monitor ({@code sec-filing-monitor}): New SEC filings of the companies you follow,
 * insider trades, 8-Ks and activist stakes first, each linked to the filing.
 *
 * Only {@code companies} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * SecFilingMonitorParams params = SecFilingMonitorParams.builder()
 *     .companies("AAPL", "MSFT")
 *     .build();
 * }</pre>
 */
public final class SecFilingMonitorParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "sec-filing-monitor";

    private final Map<String, Object> values;

    private SecFilingMonitorParams(Map<String, Object> values) {
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
        return "SecFilingMonitorParams" + values;
    }

    /** Builds {@link SecFilingMonitorParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Companies: Tickers (AAPL, BRK-B) or EDGAR's CIK numbers; a fund or a person who files is named by CIK. 1
         * to 100 items. Required.
         *
         * @param companies the values; not null
         * @return this builder
         */
        public Builder companies(List<String> companies) {
            values.put("companies", List.copyOf(companies));
            return this;
        }

        /**
         * Companies: Tickers (AAPL, BRK-B) or EDGAR's CIK numbers; a fund or a person who files is named by CIK. 1
         * to 100 items. Required.
         *
         * @param companies the values
         * @return this builder
         */
        public Builder companies(String... companies) {
            return companies(Arrays.asList(companies));
        }

        /**
         * Form types: The forms to watch, as EDGAR writes them. Leave empty for every form. At most 20 items.
         * Default: a list of 10 values, which {@code get} on the formula shows.
         *
         * @param forms the values; not null
         * @return this builder
         */
        public Builder forms(List<String> forms) {
            values.put("forms", List.copyOf(forms));
            return this;
        }

        /**
         * Form types: The forms to watch, as EDGAR writes them. Leave empty for every form. At most 20 items.
         * Default: a list of 10 values, which {@code get} on the formula shows.
         *
         * @param forms the values
         * @return this builder
         */
        public Builder forms(String... forms) {
            return forms(Arrays.asList(forms));
        }

        /**
         * Filed within (days): Only filings made in the last this many days. From 1 to 90. Default: 7.
         *
         * @param filedWithinDays the value
         * @return this builder
         */
        public Builder filedWithinDays(int filedWithinDays) {
            values.put("filed_within_days", filedWithinDays);
            return this;
        }

        /**
         * Only 8-Ks reporting these items: For example 5.02 (officers leaving or joining) or 1.01 (a material
         * agreement). Leave empty for every 8-K. Default: [].
         *
         * @param eightKItems the values; not null
         * @return this builder
         */
        public Builder eightKItems(List<String> eightKItems) {
            values.put("eight_k_items", List.copyOf(eightKItems));
            return this;
        }

        /**
         * Only 8-Ks reporting these items: For example 5.02 (officers leaving or joining) or 1.01 (a material
         * agreement). Leave empty for every 8-K. Default: [].
         *
         * @param eightKItems the values
         * @return this builder
         */
        public Builder eightKItems(String... eightKItems) {
            return eightKItems(Arrays.asList(eightKItems));
        }

        /**
         * Include amendments: Amended filings, whose form ends in /A. Default: true.
         *
         * @param includeAmendments the value
         * @return this builder
         */
        public Builder includeAmendments(boolean includeAmendments) {
            values.put("include_amendments", includeAmendments);
            return this;
        }

        /**
         * Filings read per company: The newest filings read for each company; raise it for a long window on a
         * company that files often. From 1 to 100. Default: 40.
         *
         * @param perCompany the value
         * @return this builder
         */
        public Builder perCompany(int perCompany) {
            values.put("per_company", perCompany);
            return this;
        }

        /**
         * Most rows: The run stops at this many filings, most significant first. From 1 to 2000. Default: 200.
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
        public SecFilingMonitorParams build() {
            if (!values.containsKey("companies")) {
                throw new IllegalStateException("SecFilingMonitorParams: companies is required");
            }
            return new SecFilingMonitorParams(values);
        }
    }
}
