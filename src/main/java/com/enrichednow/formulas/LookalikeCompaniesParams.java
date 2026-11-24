// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Lookalike Companies ({@code lookalike-companies}): Startups like the accounts you won, found
 * where the Y Combinator directory files them, with the people to contact.
 *
 * Only {@code seed_companies} is required: {@link Builder#build()} refuses to build without it. A value not set is
 * not sent, and the formula's default applies.
 *
 * <pre>{@code
 * LookalikeCompaniesParams params = LookalikeCompaniesParams.builder()
 *     .seedCompanies("Rippling", "Lattice")
 *     .build();
 * }</pre>
 */
public final class LookalikeCompaniesParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "lookalike-companies";

    private final Map<String, Object> values;

    private LookalikeCompaniesParams(Map<String, Object> values) {
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
        return "LookalikeCompaniesParams" + values;
    }

    /** Builds {@link LookalikeCompaniesParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Accounts you won: Company names as the Y Combinator directory lists them. 1 to 20 items. Required.
         *
         * @param seedCompanies the values; not null
         * @return this builder
         */
        public Builder seedCompanies(List<String> seedCompanies) {
            values.put("seed_companies", List.copyOf(seedCompanies));
            return this;
        }

        /**
         * Accounts you won: Company names as the Y Combinator directory lists them. 1 to 20 items. Required.
         *
         * @param seedCompanies the values
         * @return this builder
         */
        public Builder seedCompanies(String... seedCompanies) {
            return seedCompanies(Arrays.asList(seedCompanies));
        }

        /**
         * Places: As the directory names them, such as United States of America or Europe. Leave empty for
         * anywhere. Default: [].
         *
         * @param regions the values; not null
         * @return this builder
         */
        public Builder regions(List<String> regions) {
            values.put("regions", List.copyOf(regions));
            return this;
        }

        /**
         * Places: As the directory names them, such as United States of America or Europe. Leave empty for
         * anywhere. Default: [].
         *
         * @param regions the values
         * @return this builder
         */
        public Builder regions(String... regions) {
            return regions(Arrays.asList(regions));
        }

        /**
         * Team size: Companies whose size is not known are kept. Default: {"min": 2, "max": 1000}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder teamSize(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("team_size", Collections.unmodifiableMap(range));
            return this;
        }

        /**
         * Only active companies: Leaves out companies the directory marks as acquired, public or inactive. Default:
         * true.
         *
         * @param activeOnly the value
         * @return this builder
         */
        public Builder activeOnly(boolean activeOnly) {
            values.put("active_only", activeOnly);
            return this;
        }

        /**
         * Companies read per account: How many companies to read from the directory for each account you won. From
         * 5 to 100. Default: 40.
         *
         * @param peersPerAccount the value
         * @return this builder
         */
        public Builder peersPerAccount(int peersPerAccount) {
            values.put("peers_per_account", peersPerAccount);
            return this;
        }

        /**
         * Companies to leave out: Names or domains, such as your existing customers. At most 5000 items. Default:
         * [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Companies to leave out: Names or domains, such as your existing customers. At most 5000 items. Default:
         * [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Who to contact: The roles a connected people provider looks for at each company. Default: ["CEO",
         * "Founder", "VP Sales", "Head of Operations"].
         *
         * @param buyerTitles the values; not null
         * @return this builder
         */
        public Builder buyerTitles(List<String> buyerTitles) {
            values.put("buyer_titles", List.copyOf(buyerTitles));
            return this;
        }

        /**
         * Who to contact: The roles a connected people provider looks for at each company. Default: ["CEO",
         * "Founder", "VP Sales", "Head of Operations"].
         *
         * @param buyerTitles the values
         * @return this builder
         */
        public Builder buyerTitles(String... buyerTitles) {
            return buyerTitles(Arrays.asList(buyerTitles));
        }

        /**
         * Contacts per company: 0 keeps one row per company. From 0 to 5. Default: 1.
         *
         * @param contactsPerCompany the value
         * @return this builder
         */
        public Builder contactsPerCompany(int contactsPerCompany) {
            values.put("contacts_per_company", contactsPerCompany);
            return this;
        }

        /**
         * Most companies: The run stops at this many companies, the most alike first. From 1 to 1000. Default: 100.
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
        public LookalikeCompaniesParams build() {
            if (!values.containsKey("seed_companies")) {
                throw new IllegalStateException("LookalikeCompaniesParams: seed_companies is required");
            }
            return new LookalikeCompaniesParams(values);
        }
    }
}
