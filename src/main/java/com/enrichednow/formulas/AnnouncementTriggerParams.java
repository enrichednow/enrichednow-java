// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Funding and Announcement Trigger ({@code announcement-trigger}): Companies that just announced
 * funding, an expansion, a launch or a senior hire, with who to contact and the fact.
 *
 * Only {@code event_types} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * AnnouncementTriggerParams params = AnnouncementTriggerParams.builder()
 *     .eventTypes("funding")
 *     .build();
 * }</pre>
 */
public final class AnnouncementTriggerParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "announcement-trigger";

    private final Map<String, Object> values;

    private AnnouncementTriggerParams(Map<String, Object> values) {
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
        return "AnnouncementTriggerParams" + values;
    }

    /** Builds {@link AnnouncementTriggerParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Kinds of news: Which announcements to look for. Each one of "funding", "expansion", "product_launch",
         * "executive_hire", "acquisition". At least 1 item. Required.
         *
         * @param eventTypes the values; not null
         * @return this builder
         */
        public Builder eventTypes(List<String> eventTypes) {
            values.put("event_types", List.copyOf(eventTypes));
            return this;
        }

        /**
         * Kinds of news: Which announcements to look for. Each one of "funding", "expansion", "product_launch",
         * "executive_hire", "acquisition". At least 1 item. Required.
         *
         * @param eventTypes the values
         * @return this builder
         */
        public Builder eventTypes(String... eventTypes) {
            return eventTypes(Arrays.asList(eventTypes));
        }

        /**
         * Words in the news: Only releases that hold one of these words, such as fintech or healthcare. Leave empty
         * for any. At most 5 items. Default: [].
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Words in the news: Only releases that hold one of these words, such as fintech or healthcare. Leave empty
         * for any. At most 5 items. Default: [].
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Published within (days): Only releases from the last this many days. From 1 to 30. Default: 3.
         *
         * @param publishedWithinDays the value
         * @return this builder
         */
        public Builder publishedWithinDays(int publishedWithinDays) {
            values.put("published_within_days", publishedWithinDays);
            return this;
        }

        /**
         * News sources: The press-release wires, and Google News for stories as well as releases. Each one of
         * "prnewswire", "globenewswire", "google_news". At least 1 item. Default: ["prnewswire", "globenewswire"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * News sources: The press-release wires, and Google News for stories as well as releases. Each one of
         * "prnewswire", "globenewswire", "google_news". At least 1 item. Default: ["prnewswire", "globenewswire"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Releases read in full: How many of the newest matching releases are opened to read who, how much and who
         * invested. From 5 to 200. Default: 60.
         *
         * @param releasesToRead the value
         * @return this builder
         */
        public Builder releasesToRead(int releasesToRead) {
            values.put("releases_to_read", releasesToRead);
            return this;
        }

        /**
         * Places: Countries, states or cities the company or the release is from. Leave empty for anywhere.
         * Default: [].
         *
         * @param regions the values; not null
         * @return this builder
         */
        public Builder regions(List<String> regions) {
            values.put("regions", List.copyOf(regions));
            return this;
        }

        /**
         * Places: Countries, states or cities the company or the release is from. Leave empty for anywhere.
         * Default: [].
         *
         * @param regions the values
         * @return this builder
         */
        public Builder regions(String... regions) {
            return regions(Arrays.asList(regions));
        }

        /**
         * Company size (employees): Companies whose size is not known are kept. Default: {"min": 1, "max": 10000}.
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
         * Only these industries: Leave empty for any. Default: [].
         *
         * @param industriesInclude the values; not null
         * @return this builder
         */
        public Builder industriesInclude(List<String> industriesInclude) {
            values.put("industries_include", List.copyOf(industriesInclude));
            return this;
        }

        /**
         * Only these industries: Leave empty for any. Default: [].
         *
         * @param industriesInclude the values
         * @return this builder
         */
        public Builder industriesInclude(String... industriesInclude) {
            return industriesInclude(Arrays.asList(industriesInclude));
        }

        /**
         * Companies to leave out: Names or domains, such as customers. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Companies to leave out: Names or domains, such as customers. At most 5000 items. Default: [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Who to contact: The roles of the people to find at each company. Default: ["Chief Executive Officer",
         * "Founder", "VP Sales"].
         *
         * @param buyerTitles the values; not null
         * @return this builder
         */
        public Builder buyerTitles(List<String> buyerTitles) {
            values.put("buyer_titles", List.copyOf(buyerTitles));
            return this;
        }

        /**
         * Who to contact: The roles of the people to find at each company. Default: ["Chief Executive Officer",
         * "Founder", "VP Sales"].
         *
         * @param buyerTitles the values
         * @return this builder
         */
        public Builder buyerTitles(String... buyerTitles) {
            return buyerTitles(Arrays.asList(buyerTitles));
        }

        /**
         * Contact the new executive: For a senior hire, look for the person in the new role rather than the roles
         * above. Default: false.
         *
         * @param contactNewExecutive the value
         * @return this builder
         */
        public Builder contactNewExecutive(boolean contactNewExecutive) {
            values.put("contact_new_executive", contactNewExecutive);
            return this;
        }

        /**
         * Contacts per company: 0 turns contacts off. From 0 to 5. Default: 1.
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
         * Most rows: The run stops at this many rows, newest first. From 1 to 1000. Default: 100.
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
        public AnnouncementTriggerParams build() {
            if (!values.containsKey("event_types")) {
                throw new IllegalStateException("AnnouncementTriggerParams: event_types is required");
            }
            return new AnnouncementTriggerParams(values);
        }
    }
}
