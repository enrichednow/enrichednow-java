// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of People List with Openers ({@code people-list-with-openers}): The people in the roles you sell
 * to at your target companies, with checked addresses and a line from their own site.
 *
 * Only {@code companies}, {@code titles} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * PeopleListWithOpenersParams params = PeopleListWithOpenersParams.builder()
 *     .companies("gong.io", "ramp.com")
 *     .titles("Head of Marketing", "VP Marketing")
 *     .build();
 * }</pre>
 */
public final class PeopleListWithOpenersParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "people-list-with-openers";

    private final Map<String, Object> values;

    private PeopleListWithOpenersParams(Map<String, Object> values) {
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
        return "PeopleListWithOpenersParams" + values;
    }

    /** Builds {@link PeopleListWithOpenersParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Companies: The website domains of the companies to find people at. 1 to 500 items. Required.
         *
         * @param companies the values; not null
         * @return this builder
         */
        public Builder companies(List<String> companies) {
            values.put("companies", List.copyOf(companies));
            return this;
        }

        /**
         * Companies: The website domains of the companies to find people at. 1 to 500 items. Required.
         *
         * @param companies the values
         * @return this builder
         */
        public Builder companies(String... companies) {
            return companies(Arrays.asList(companies));
        }

        /**
         * Roles: The roles of the people to find. 1 to 10 items. Required.
         *
         * @param titles the values; not null
         * @return this builder
         */
        public Builder titles(List<String> titles) {
            values.put("titles", List.copyOf(titles));
            return this;
        }

        /**
         * Roles: The roles of the people to find. 1 to 10 items. Required.
         *
         * @param titles the values
         * @return this builder
         */
        public Builder titles(String... titles) {
            return titles(Arrays.asList(titles));
        }

        /**
         * Seniority: How senior the people should be. Each one of "manager", "director", "vp", "c_level". Default:
         * ["director", "vp", "c_level"].
         *
         * @param seniority the values; not null
         * @return this builder
         */
        public Builder seniority(List<String> seniority) {
            values.put("seniority", List.copyOf(seniority));
            return this;
        }

        /**
         * Seniority: How senior the people should be. Each one of "manager", "director", "vp", "c_level". Default:
         * ["director", "vp", "c_level"].
         *
         * @param seniority the values
         * @return this builder
         */
        public Builder seniority(String... seniority) {
            return seniority(Arrays.asList(seniority));
        }

        /**
         * People per company: The most people to find at each company. From 1 to 10. Default: 2.
         *
         * @param peoplePerCompany the value
         * @return this builder
         */
        public Builder peoplePerCompany(int peoplePerCompany) {
            values.put("people_per_company", peoplePerCompany);
            return this;
        }

        /**
         * Only addresses that pass the check: Keeps addresses whose domain takes mail, and those a verification
         * provider confirmed. Default: true.
         *
         * @param requireVerifiedEmail the value
         * @return this builder
         */
        public Builder requireVerifiedEmail(boolean requireVerifiedEmail) {
            values.put("require_verified_email", requireVerifiedEmail);
            return this;
        }

        /**
         * Most rows: The run stops at this many people. From 1 to 5000. Default: 500.
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
        public PeopleListWithOpenersParams build() {
            if (!values.containsKey("companies")) {
                throw new IllegalStateException("PeopleListWithOpenersParams: companies is required");
            }
            if (!values.containsKey("titles")) {
                throw new IllegalStateException("PeopleListWithOpenersParams: titles is required");
            }
            return new PeopleListWithOpenersParams(values);
        }
    }
}
