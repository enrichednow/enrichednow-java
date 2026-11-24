// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Ad-Activity Trigger ({@code ad-activity-trigger}): Companies running ads about your topic on
 * Meta and LinkedIn now, sized up, with their marketing lead to contact.
 *
 * Only {@code keywords} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * AdActivityTriggerParams params = AdActivityTriggerParams.builder()
 *     .keywords("payroll software")
 *     .build();
 * }</pre>
 */
public final class AdActivityTriggerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "ad-activity-trigger";

    private final Map<String, Object> values;

    private AdActivityTriggerParams(Map<String, Object> values) {
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
        return "AdActivityTriggerParams" + values;
    }

    /** Builds {@link AdActivityTriggerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * What the ads are about: Words the ads say, each searched in each library, such as "payroll software". 1
         * to 5 items. Required.
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * What the ads are about: Words the ads say, each searched in each library, such as "payroll software". 1
         * to 5 items. Required.
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Countries: Where the ads are shown, as two-letter codes such as US or GB. 1 to 5 items. Default: ["US"].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Countries: Where the ads are shown, as two-letter codes such as US or GB. 1 to 5 items. Default: ["US"].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Ad libraries: Meta covers Facebook and Instagram. Each one of "meta", "linkedin". At least 1 item.
         * Default: ["meta", "linkedin"].
         *
         * @param platforms the values; not null
         * @return this builder
         */
        public Builder platforms(List<String> platforms) {
            values.put("platforms", List.copyOf(platforms));
            return this;
        }

        /**
         * Ad libraries: Meta covers Facebook and Instagram. Each one of "meta", "linkedin". At least 1 item.
         * Default: ["meta", "linkedin"].
         *
         * @param platforms the values
         * @return this builder
         */
        public Builder platforms(String... platforms) {
            return platforms(Arrays.asList(platforms));
        }

        /**
         * Ads started within (days): Meta says when each ad started; older ones are left out. LinkedIn's library
         * does not say, and its ads are kept. From 1 to 365. Default: 90.
         *
         * @param startedWithinDays the value
         * @return this builder
         */
        public Builder startedWithinDays(int startedWithinDays) {
            values.put("started_within_days", startedWithinDays);
            return this;
        }

        /**
         * Ads read per search: How many ads to read for each phrase, country and library. From 5 to 100. Default:
         * 30.
         *
         * @param adsPerSearch the value
         * @return this builder
         */
        public Builder adsPerSearch(int adsPerSearch) {
            values.put("ads_per_search", adsPerSearch);
            return this;
        }

        /**
         * Least ads: Ads a company needs to be running to be listed. At least 1. Default: 1.
         *
         * @param minAds the value
         * @return this builder
         */
        public Builder minAds(int minAds) {
            values.put("min_ads", minAds);
            return this;
        }

        /**
         * Company size (employees): Companies whose size is not known are kept. Default: {"min": 2, "max": 10000}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder companySize(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("company_size", Collections.unmodifiableMap(range));
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
         * Who to contact: The roles a connected people provider looks for at each company. Default: ["Chief
         * Marketing Officer", "VP Marketing", "Head of Marketing", "Marketing Director", "Head of Growth"].
         *
         * @param buyerTitles the values; not null
         * @return this builder
         */
        public Builder buyerTitles(List<String> buyerTitles) {
            values.put("buyer_titles", List.copyOf(buyerTitles));
            return this;
        }

        /**
         * Who to contact: The roles a connected people provider looks for at each company. Default: ["Chief
         * Marketing Officer", "VP Marketing", "Head of Marketing", "Marketing Director", "Head of Growth"].
         *
         * @param buyerTitles the values
         * @return this builder
         */
        public Builder buyerTitles(String... buyerTitles) {
            return buyerTitles(Arrays.asList(buyerTitles));
        }

        /**
         * Contacts per company: 0 keeps one row per company. From 0 to 5. Default: 2.
         *
         * @param contactsPerCompany the value
         * @return this builder
         */
        public Builder contactsPerCompany(int contactsPerCompany) {
            values.put("contacts_per_company", contactsPerCompany);
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
         * Most rows: The run stops at this many rows, the most active advertisers first. From 1 to 2000. Default:
         * 200.
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
        public AdActivityTriggerParams build() {
            if (!values.containsKey("keywords")) {
                throw new IllegalStateException("AdActivityTriggerParams: keywords is required");
            }
            return new AdActivityTriggerParams(values);
        }
    }
}
