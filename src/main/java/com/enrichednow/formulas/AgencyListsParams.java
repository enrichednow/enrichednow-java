// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Agency Lists ({@code agency-lists}): Agencies by service and place with size, age, rates,
 * minimum project, services and reviews, for partnering or buying.
 *
 * Only {@code directories} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * AgencyListsParams params = AgencyListsParams.builder()
 *     .directories("uk/agencies/digital-marketing/london")
 *     .build();
 * }</pre>
 */
public final class AgencyListsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "agency-lists";

    private final Map<String, Object> values;

    private AgencyListsParams(Map<String, Object> values) {
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
        return "AgencyListsParams" + values;
    }

    /** Builds {@link AgencyListsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Clutch directories: Each as in its address on clutch.co, the service then the place: agencies/seo,
         * web-developers/new-york, uk/agencies/digital-marketing/london, or the whole address. 1 to 10 items.
         * Required.
         *
         * @param directories the values; not null
         * @return this builder
         */
        public Builder directories(List<String> directories) {
            values.put("directories", List.copyOf(directories));
            return this;
        }

        /**
         * Clutch directories: Each as in its address on clutch.co, the service then the place: agencies/seo,
         * web-developers/new-york, uk/agencies/digital-marketing/london, or the whole address. 1 to 10 items.
         * Required.
         *
         * @param directories the values
         * @return this builder
         */
        public Builder directories(String... directories) {
            return directories(Arrays.asList(directories));
        }

        /**
         * Pages to read: Pages of each directory to read; a page lists about 50 to 80 agencies. From 1 to 10.
         * Default: 2.
         *
         * @param pages the value
         * @return this builder
         */
        public Builder pages(int pages) {
            values.put("pages", pages);
            return this;
        }

        /**
         * What the list is for: Partner ranks agencies by rating, reviews and a published email. Acquisition ranks
         * established agencies first: ten years or more in business, 10 to 249 staff, and a body of reviews. One of
         * "partner", "acquisition". Default: "partner".
         *
         * @param purpose the value
         * @return this builder
         */
        public Builder purpose(String purpose) {
            values.put("purpose", purpose);
            return this;
        }

        /**
         * Head count: Agencies whose head count on Clutch falls outside this range are left out; an agency that
         * does not say is kept. Default: {"min": 2, "max": 1000}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder employees(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("employees", Collections.unmodifiableMap(range));
            return this;
        }

        /**
         * Founded in or before: Only agencies founded in this year or earlier, such as 2015 for ten years in
         * business. Empty for any. From 1900 to 2100. Default: null.
         *
         * @param foundedBefore the value, as the API takes it
         * @return this builder
         */
        public Builder foundedBefore(Map<String, Object> foundedBefore) {
            values.put("founded_before", Collections.unmodifiableMap(new LinkedHashMap<>(foundedBefore)));
            return this;
        }

        /**
         * Highest hourly rate (USD): Agencies whose lowest hourly rate is above this are left out. 0 for any. From
         * 0 to 1000. Default: 0.
         *
         * @param maxHourlyRate the value
         * @return this builder
         */
        public Builder maxHourlyRate(int maxHourlyRate) {
            values.put("max_hourly_rate", maxHourlyRate);
            return this;
        }

        /**
         * Largest minimum project (USD): Agencies that take no project smaller than this are left out. 0 for any.
         * From 0 to 10000000. Default: 0.
         *
         * @param maxMinProject the value
         * @return this builder
         */
        public Builder maxMinProject(int maxMinProject) {
            values.put("max_min_project", maxMinProject);
            return this;
        }

        /**
         * Sells one of these services: Words from Clutch's service names, such as SEO, Pay Per Click or Web
         * Development. Leave empty for any. At most 20 items. Default: [].
         *
         * @param services the values; not null
         * @return this builder
         */
        public Builder services(List<String> services) {
            values.put("services", List.copyOf(services));
            return this;
        }

        /**
         * Sells one of these services: Words from Clutch's service names, such as SEO, Pay Per Click or Web
         * Development. Leave empty for any. At most 20 items. Default: [].
         *
         * @param services the values
         * @return this builder
         */
        public Builder services(String... services) {
            return services(Arrays.asList(services));
        }

        /**
         * Office in these countries: Two-letter country codes (GB, US). A directory of a place also lists sponsors
         * based elsewhere; this leaves them out. At most 50 items. Default: [].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Office in these countries: Two-letter country codes (GB, US). A directory of a place also lists sponsors
         * based elsewhere; this leaves them out. At most 50 items. Default: [].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Lowest rating: Agencies rated below this on Clutch are left out. From 0 to 5. Default: 0.
         *
         * @param minRating the value
         * @return this builder
         */
        public Builder minRating(double minRating) {
            values.put("min_rating", minRating);
            return this;
        }

        /**
         * Fewest reviews: Agencies with fewer Clutch reviews are left out. At least 0. Default: 0.
         *
         * @param minReviews the value
         * @return this builder
         */
        public Builder minReviews(int minReviews) {
            values.put("min_reviews", minReviews);
            return this;
        }

        /**
         * Only agencies with an email: Keeps the agencies whose own website publishes an email address that passes
         * the check. Default: false.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Agencies to leave out: Names or domains, such as agencies you already work with. At most 5000 items.
         * Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Agencies to leave out: Names or domains, such as agencies you already work with. At most 5000 items.
         * Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Most rows: The run stops at this many agencies, best first. Each agency's profile and website are read,
         * so this sets the cost. From 1 to 2000. Default: 100.
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
        public AgencyListsParams build() {
            if (!values.containsKey("directories")) {
                throw new IllegalStateException("AgencyListsParams: directories is required");
            }
            return new AgencyListsParams(values);
        }
    }
}
