// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Tender Alerts ({@code tender-alerts}): New public tenders that match your words and codes, from
 * the EU, UK and US, with buyer, deadline, value and link.
 *
 * A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * TenderAlertsParams params = TenderAlertsParams.builder()
 *     .build();
 * }</pre>
 */
public final class TenderAlertsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "tender-alerts";

    private final Map<String, Object> values;

    private TenderAlertsParams(Map<String, Object> values) {
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
        return "TenderAlertsParams" + values;
    }

    /** Builds {@link TenderAlertsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Words: What you sell, as a buyer would write it, such as cybersecurity or road resurfacing. Each is
         * searched on its own. Leave empty to take every notice of the window, or to match on CPV codes alone. At
         * most 10 items. Default: [].
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Words: What you sell, as a buyer would write it, such as cybersecurity or road resurfacing. Each is
         * searched on its own. Leave empty to take every notice of the window, or to match on CPV codes alone. At
         * most 10 items. Default: [].
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Notice boards: TED (European Union), Find a Tender and Contracts Finder (United Kingdom), Grants.gov, New
         * York City's City Record, Chicago's and the MTA's contracts (awards only), and SAM.gov, which is read
         * where it is offered. Each one of "ted", "find_a_tender", "contracts_finder", "grants_gov",
         * "new_york_city", "chicago", "ny_mta", "sam_gov". At least 1 item. Default: ["ted", "find_a_tender",
         * "contracts_finder", "grants_gov", "new_york_city", "sam_gov"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Notice boards: TED (European Union), Find a Tender and Contracts Finder (United Kingdom), Grants.gov, New
         * York City's City Record, Chicago's and the MTA's contracts (awards only), and SAM.gov, which is read
         * where it is offered. Each one of "ted", "find_a_tender", "contracts_finder", "grants_gov",
         * "new_york_city", "chicago", "ny_mta", "sam_gov". At least 1 item. Default: ["ted", "find_a_tender",
         * "contracts_finder", "grants_gov", "new_york_city", "sam_gov"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Notice stages: Pre-RFP notices (prior information, sources sought, presolicitation, forecasted grants and
         * UK notices of planned procurements), open tenders, or awards. Each one of "planning", "tender", "award".
         * At least 1 item. Default: ["tender"].
         *
         * @param noticeTypes the values; not null
         * @return this builder
         */
        public Builder noticeTypes(List<String> noticeTypes) {
            values.put("notice_types", List.copyOf(noticeTypes));
            return this;
        }

        /**
         * Notice stages: Pre-RFP notices (prior information, sources sought, presolicitation, forecasted grants and
         * UK notices of planned procurements), open tenders, or awards. Each one of "planning", "tender", "award".
         * At least 1 item. Default: ["tender"].
         *
         * @param noticeTypes the values
         * @return this builder
         */
        public Builder noticeTypes(String... noticeTypes) {
            return noticeTypes(Arrays.asList(noticeTypes));
        }

        /**
         * Published within (days): Only notices published in the last this many days, today included. Run daily, 3
         * covers a weekend. From 1 to 30. Default: 3.
         *
         * @param publishedWithinDays the value
         * @return this builder
         */
        public Builder publishedWithinDays(int publishedWithinDays) {
            values.put("published_within_days", publishedWithinDays);
            return this;
        }

        /**
         * CPV codes: Eight-digit CPV codes, such as 72000000 for IT services. TED is searched by them; a UK notice
         * is kept when its main code is one of them. A notice matching your words or your codes is kept. At most 10
         * items. Default: [].
         *
         * @param cpvCodes the values; not null
         * @return this builder
         */
        public Builder cpvCodes(List<String> cpvCodes) {
            values.put("cpv_codes", List.copyOf(cpvCodes));
            return this;
        }

        /**
         * CPV codes: Eight-digit CPV codes, such as 72000000 for IT services. TED is searched by them; a UK notice
         * is kept when its main code is one of them. A notice matching your words or your codes is kept. At most 10
         * items. Default: [].
         *
         * @param cpvCodes the values
         * @return this builder
         */
        public Builder cpvCodes(String... cpvCodes) {
            return cpvCodes(Arrays.asList(cpvCodes));
        }

        /**
         * Buyers' countries: Three-letter codes, such as DEU, FRA, GBR or USA. Leave empty for every country of the
         * boards you chose. At most 30 items. Default: [].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Buyers' countries: Three-letter codes, such as DEU, FRA, GBR or USA. Leave empty for every country of the
         * boards you chose. At most 30 items. Default: [].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * NAICS codes (SAM.gov): Only SAM.gov notices in these industry codes, 2 to 6 digits, such as 541512. At
         * most 10 items. Default: [].
         *
         * @param naicsCodes the values; not null
         * @return this builder
         */
        public Builder naicsCodes(List<String> naicsCodes) {
            values.put("naics_codes", List.copyOf(naicsCodes));
            return this;
        }

        /**
         * NAICS codes (SAM.gov): Only SAM.gov notices in these industry codes, 2 to 6 digits, such as 541512. At
         * most 10 items. Default: [].
         *
         * @param naicsCodes the values
         * @return this builder
         */
        public Builder naicsCodes(String... naicsCodes) {
            return naicsCodes(Arrays.asList(naicsCodes));
        }

        /**
         * Set-asides (SAM.gov): Only SAM.gov notices set aside for these, by SAM.gov's codes, such as SBA, 8A, HZC,
         * SDVOSBC or WOSB. At most 10 items. Default: [].
         *
         * @param setAsides the values; not null
         * @return this builder
         */
        public Builder setAsides(List<String> setAsides) {
            values.put("set_asides", List.copyOf(setAsides));
            return this;
        }

        /**
         * Set-asides (SAM.gov): Only SAM.gov notices set aside for these, by SAM.gov's codes, such as SBA, 8A, HZC,
         * SDVOSBC or WOSB. At most 10 items. Default: [].
         *
         * @param setAsides the values
         * @return this builder
         */
        public Builder setAsides(String... setAsides) {
            return setAsides(Arrays.asList(setAsides));
        }

        /**
         * Places of performance (SAM.gov): Only SAM.gov notices for work in these US states, as two letters. At
         * most 10 items. Default: [].
         *
         * @param usStates the values; not null
         * @return this builder
         */
        public Builder usStates(List<String> usStates) {
            values.put("us_states", List.copyOf(usStates));
            return this;
        }

        /**
         * Places of performance (SAM.gov): Only SAM.gov notices for work in these US states, as two letters. At
         * most 10 items. Default: [].
         *
         * @param usStates the values
         * @return this builder
         */
        public Builder usStates(String... usStates) {
            return usStates(Arrays.asList(usStates));
        }

        /**
         * At least this many days to respond: Leaves out notices due sooner. A notice past its deadline is always
         * left out; one that states no deadline is kept. From 0 to 365. Default: 0.
         *
         * @param minDaysLeft the value
         * @return this builder
         */
        public Builder minDaysLeft(int minDaysLeft) {
            values.put("min_days_left", minDaysLeft);
            return this;
        }

        /**
         * Due within (days): Only notices due within this many days. 0 keeps every deadline, and notices that state
         * none. From 0 to 730. Default: 0.
         *
         * @param dueWithinDays the value
         * @return this builder
         */
        public Builder dueWithinDays(int dueWithinDays) {
            values.put("due_within_days", dueWithinDays);
            return this;
        }

        /**
         * Leave out titles with: Notices whose title holds one of these words are left out, such as cleaning or
         * catering. At most 30 items. Default: [].
         *
         * @param excludeWords the values; not null
         * @return this builder
         */
        public Builder excludeWords(List<String> excludeWords) {
            values.put("exclude_words", List.copyOf(excludeWords));
            return this;
        }

        /**
         * Leave out titles with: Notices whose title holds one of these words are left out, such as cleaning or
         * catering. At most 30 items. Default: [].
         *
         * @param excludeWords the values
         * @return this builder
         */
        public Builder excludeWords(String... excludeWords) {
            return excludeWords(Arrays.asList(excludeWords));
        }

        /**
         * Read each grant in full: Adds a Grants.gov opportunity's funding, largest award, eligibility and
         * description. One more unit for each grant. Default: true.
         *
         * @param grantDetails the value
         * @return this builder
         */
        public Builder grantDetails(boolean grantDetails) {
            values.put("grant_details", grantDetails);
            return this;
        }

        /**
         * Most rows: The run stops at this many notices, best matches first. From 1 to 2000. Default: 200.
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
        public TenderAlertsParams build() {
            return new TenderAlertsParams(values);
        }
    }
}
