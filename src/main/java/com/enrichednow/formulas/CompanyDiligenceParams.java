// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Company Diligence ({@code company-diligence}): Each company you name checked on its register,
 * in adverse news, reviews and complaints, and its website, with red flags.
 *
 * Only {@code companies} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * CompanyDiligenceParams params = CompanyDiligenceParams.builder()
 *     .companies("Thames Water Utilities Limited", "Monzo Bank Limited")
 *     .build();
 * }</pre>
 */
public final class CompanyDiligenceParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "company-diligence";

    private final Map<String, Object> values;

    private CompanyDiligenceParams(Map<String, Object> values) {
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
        return "CompanyDiligenceParams" + values;
    }

    /** Builds {@link CompanyDiligenceParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Companies: Each company by its name, its register number (a UK company number such as 09446231, a Swedish
         * organisation number such as 556703-7485) or its website's domain. A name is searched as written, in the
         * news too, so give the name the company is known by (Thames Water, not Thames Water Utilities Limited); a
         * domain also lets its reviews and website be read. 1 to 200 items. Required.
         *
         * @param companies the values; not null
         * @return this builder
         */
        public Builder companies(List<String> companies) {
            values.put("companies", List.copyOf(companies));
            return this;
        }

        /**
         * Companies: Each company by its name, its register number (a UK company number such as 09446231, a Swedish
         * organisation number such as 556703-7485) or its website's domain. A name is searched as written, in the
         * news too, so give the name the company is known by (Thames Water, not Thames Water Utilities Limited); a
         * domain also lets its reviews and website be read. 1 to 200 items. Required.
         *
         * @param companies the values
         * @return this builder
         */
        public Builder companies(String... companies) {
            return companies(Arrays.asList(companies));
        }

        /**
         * Country: Where the companies are registered. The UK and Sweden have their register read; the US and
         * Canada have the Better Business Bureau read. The news, reviews and website checks run everywhere. One of
         * "GB", "US", "CA", "SE", "IE", "AU", "other". Default: "GB".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Place, for the Better Business Bureau: The city or state the companies trade in (Austin, TX). Only used
         * in the US and Canada, to find the right business. Default: "".
         *
         * @param location the value
         * @return this builder
         */
        public Builder location(String location) {
            values.put("location", location);
            return this;
        }

        /**
         * Checks: Which checks to run. Each costs about one unit a company; finding a company's website from its
         * name costs a few more. Each one of "register", "news", "press_releases", "reviews", "website". At least 1
         * item. Default: ["register", "news", "press_releases", "reviews", "website"].
         *
         * @param checks the values; not null
         * @return this builder
         */
        public Builder checks(List<String> checks) {
            values.put("checks", List.copyOf(checks));
            return this;
        }

        /**
         * Checks: Which checks to run. Each costs about one unit a company; finding a company's website from its
         * name costs a few more. Each one of "register", "news", "press_releases", "reviews", "website". At least 1
         * item. Default: ["register", "news", "press_releases", "reviews", "website"].
         *
         * @param checks the values
         * @return this builder
         */
        public Builder checks(String... checks) {
            return checks(Arrays.asList(checks));
        }

        /**
         * Adverse words: The news is searched for the company together with these words, and a headline that holds
         * one is counted as adverse. 1 to 30 items. Default: a list of 18 values, which {@code get} on the formula
         * shows.
         *
         * @param adverseWords the values; not null
         * @return this builder
         */
        public Builder adverseWords(List<String> adverseWords) {
            values.put("adverse_words", List.copyOf(adverseWords));
            return this;
        }

        /**
         * Adverse words: The news is searched for the company together with these words, and a headline that holds
         * one is counted as adverse. 1 to 30 items. Default: a list of 18 values, which {@code get} on the formula
         * shows.
         *
         * @param adverseWords the values
         * @return this builder
         */
        public Builder adverseWords(String... adverseWords) {
            return adverseWords(Arrays.asList(adverseWords));
        }

        /**
         * What the check is for: Counterparty weighs the register and adverse news most. Acquisition also weighs
         * customer reviews and complaints, which say most about a small business a buyer is about to take on. One
         * of "counterparty", "acquisition". Default: "counterparty".
         *
         * @param focus the value
         * @return this builder
         */
        public Builder focus(String focus) {
            values.put("focus", focus);
            return this;
        }

        /**
         * News within (days): How far back the news and the press releases are read. From 30 to 1825. Default: 365.
         *
         * @param windowDays the value
         * @return this builder
         */
        public Builder windowDays(int windowDays) {
            values.put("window_days", windowDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many companies, riskiest first. From 1 to 2000. Default: 200.
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
        public CompanyDiligenceParams build() {
            if (!values.containsKey("companies")) {
                throw new IllegalStateException("CompanyDiligenceParams: companies is required");
            }
            return new CompanyDiligenceParams(values);
        }
    }
}
