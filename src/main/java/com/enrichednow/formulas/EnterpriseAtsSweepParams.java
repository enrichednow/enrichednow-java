// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Enterprise ATS Sweep ({@code enterprise-ats-sweep}): The new openings at your target companies,
 * read from their own careers pages, with the companies hiring hardest flagged P1.
 *
 * Only {@code target_companies} is required: {@link Builder#build()} refuses to build without it. A value not set
 * is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * EnterpriseAtsSweepParams params = EnterpriseAtsSweepParams.builder()
 *     .targetCompanies("gitlab.com", "cloudflare.com")
 *     .build();
 * }</pre>
 */
public final class EnterpriseAtsSweepParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "enterprise-ats-sweep";

    private final Map<String, Object> values;

    private EnterpriseAtsSweepParams(Map<String, Object> values) {
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
        return "EnterpriseAtsSweepParams" + values;
    }

    /** Builds {@link EnterpriseAtsSweepParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Target companies: A domain (acme.com) or a careers page address for each company. 1 to 100 items.
         * Required.
         *
         * @param targetCompanies the values; not null
         * @return this builder
         */
        public Builder targetCompanies(List<String> targetCompanies) {
            values.put("target_companies", List.copyOf(targetCompanies));
            return this;
        }

        /**
         * Target companies: A domain (acme.com) or a careers page address for each company. 1 to 100 items.
         * Required.
         *
         * @param targetCompanies the values
         * @return this builder
         */
        public Builder targetCompanies(String... targetCompanies) {
            return targetCompanies(Arrays.asList(targetCompanies));
        }

        /**
         * Job keywords: Openings whose title holds one of these as a whole word or phrase. Leave empty for every
         * opening. At most 20 items. Default: [].
         *
         * @param jobKeywords the values; not null
         * @return this builder
         */
        public Builder jobKeywords(List<String> jobKeywords) {
            values.put("job_keywords", List.copyOf(jobKeywords));
            return this;
        }

        /**
         * Job keywords: Openings whose title holds one of these as a whole word or phrase. Leave empty for every
         * opening. At most 20 items. Default: [].
         *
         * @param jobKeywords the values
         * @return this builder
         */
        public Builder jobKeywords(String... jobKeywords) {
            return jobKeywords(Arrays.asList(jobKeywords));
        }

        /**
         * Locations: Openings in one of these places; remote openings are kept too. Leave empty for anywhere. At
         * most 20 items. Default: [].
         *
         * @param locations the values; not null
         * @return this builder
         */
        public Builder locations(List<String> locations) {
            values.put("locations", List.copyOf(locations));
            return this;
        }

        /**
         * Locations: Openings in one of these places; remote openings are kept too. Leave empty for anywhere. At
         * most 20 items. Default: [].
         *
         * @param locations the values
         * @return this builder
         */
        public Builder locations(String... locations) {
            return locations(Arrays.asList(locations));
        }

        /**
         * Posted within (days): Only openings posted in the last this many days. An opening with no date is kept.
         * From 1 to 365. Default: 30.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * Words to leave out of titles: For example intern or graduate. Default: [].
         *
         * @param excludeKeywords the values; not null
         * @return this builder
         */
        public Builder excludeKeywords(List<String> excludeKeywords) {
            values.put("exclude_keywords", List.copyOf(excludeKeywords));
            return this;
        }

        /**
         * Words to leave out of titles: For example intern or graduate. Default: [].
         *
         * @param excludeKeywords the values
         * @return this builder
         */
        public Builder excludeKeywords(String... excludeKeywords) {
            return excludeKeywords(Arrays.asList(excludeKeywords));
        }

        /**
         * Only openings from the tracking system: Leaves out what was read from a careers page's own text, where a
         * heading or a name can pass for an opening. Off reads those too. Default: true.
         *
         * @param verifiedOnly the value
         * @return this builder
         */
        public Builder verifiedOnly(boolean verifiedOnly) {
            values.put("verified_only", verifiedOnly);
            return this;
        }

        /**
         * Least matching openings: A company with fewer matching openings is left out. At least 1. Default: 1.
         *
         * @param minMatchingRoles the value
         * @return this builder
         */
        public Builder minMatchingRoles(int minMatchingRoles) {
            values.put("min_matching_roles", minMatchingRoles);
            return this;
        }

        /**
         * P1 at this many recent openings: A company with at least this many matching openings posted in the P1
         * window is flagged P1. At least 1. Default: 5.
         *
         * @param p1MinRoles the value
         * @return this builder
         */
        public Builder p1MinRoles(int p1MinRoles) {
            values.put("p1_min_roles", p1MinRoles);
            return this;
        }

        /**
         * P1 window (days): How recent an opening must be to count towards P1. From 1 to 90. Default: 7.
         *
         * @param p1WithinDays the value
         * @return this builder
         */
        public Builder p1WithinDays(int p1WithinDays) {
            values.put("p1_within_days", p1WithinDays);
            return this;
        }

        /**
         * Show an opening again after (days): For a saved formula, how long before a delivered opening may come
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
         * Most rows: The run stops at this many rows, best first. From 1 to 5000. Default: 500.
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
        public EnterpriseAtsSweepParams build() {
            if (!values.containsKey("target_companies")) {
                throw new IllegalStateException("EnterpriseAtsSweepParams: target_companies is required");
            }
            return new EnterpriseAtsSweepParams(values);
        }
    }
}
