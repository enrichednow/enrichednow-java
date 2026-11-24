// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Launch and Batch Lists ({@code launch-batch-lists}): The startups of a Y Combinator batch,
 * newest launches first, with their founders and a way to reach each one.
 *
 * Only {@code batch} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * LaunchBatchListsParams params = LaunchBatchListsParams.builder()
 *     .batch("Fall 2026")
 *     .build();
 * }</pre>
 */
public final class LaunchBatchListsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "launch-batch-lists";

    private final Map<String, Object> values;

    private LaunchBatchListsParams(Map<String, Object> values) {
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
        return "LaunchBatchListsParams" + values;
    }

    /** Builds {@link LaunchBatchListsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Batch: As Y Combinator writes it, such as Fall 2026 or F26. Required.
         *
         * @param batch the value
         * @return this builder
         */
        public Builder batch(String batch) {
            values.put("batch", batch);
            return this;
        }

        /**
         * Industry: As the directory names it, such as Fintech, Healthcare or B2B. Leave empty for every industry.
         *
         * @param industry the value
         * @return this builder
         */
        public Builder industry(String industry) {
            values.put("industry", industry);
            return this;
        }

        /**
         * Only companies that are hiring: Keeps the companies the directory marks as hiring. Default: false.
         *
         * @param hiringOnly the value
         * @return this builder
         */
        public Builder hiringOnly(boolean hiringOnly) {
            values.put("hiring_only", hiringOnly);
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
         * Largest team: Companies with a bigger team are left out. At least 1. Default: 500.
         *
         * @param maxTeamSize the value
         * @return this builder
         */
        public Builder maxTeamSize(int maxTeamSize) {
            values.put("max_team_size", maxTeamSize);
            return this;
        }

        /**
         * Who to contact: The roles a connected people provider looks for at each company. Default: ["Founder",
         * "Co-Founder", "CEO"].
         *
         * @param contactTitles the values; not null
         * @return this builder
         */
        public Builder contactTitles(List<String> contactTitles) {
            values.put("contact_titles", List.copyOf(contactTitles));
            return this;
        }

        /**
         * Who to contact: The roles a connected people provider looks for at each company. Default: ["Founder",
         * "Co-Founder", "CEO"].
         *
         * @param contactTitles the values
         * @return this builder
         */
        public Builder contactTitles(String... contactTitles) {
            return contactTitles(Arrays.asList(contactTitles));
        }

        /**
         * Contacts per company: People a connected provider finds at each company. 0 keeps one row per company.
         * From 0 to 5. Default: 1.
         *
         * @param contactsPerCompany the value
         * @return this builder
         */
        public Builder contactsPerCompany(int contactsPerCompany) {
            values.put("contacts_per_company", contactsPerCompany);
            return this;
        }

        /**
         * Most companies: How many companies of the batch to read, newest launches first. From 1 to 500. Default:
         * 100.
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
        public LaunchBatchListsParams build() {
            if (!values.containsKey("batch")) {
                throw new IllegalStateException("LaunchBatchListsParams: batch is required");
            }
            return new LaunchBatchListsParams(values);
        }
    }
}
