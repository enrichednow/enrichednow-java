// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Exhibitor Prospects ({@code exhibitor-prospects}): The companies exhibiting at the trade shows
 * you name, with booth, website, location, categories and contact address.
 *
 * Only {@code event_urls} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * ExhibitorProspectsParams params = ExhibitorProspectsParams.builder()
 *     .eventUrls("https://kbis.a2zinc.net/kbis2027/Public/eventmap.aspx")
 *     .build();
 * }</pre>
 */
public final class ExhibitorProspectsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "exhibitor-prospects";

    private final Map<String, Object> values;

    private ExhibitorProspectsParams(Map<String, Object> values) {
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
        return "ExhibitorProspectsParams" + values;
    }

    /** Builds {@link ExhibitorProspectsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Shows: The address of each show's exhibitor list or floor plan on its a2z site (a2zinc.net), as your
         * browser shows it. 1 to 10 items. Required.
         *
         * @param eventUrls the values; not null
         * @return this builder
         */
        public Builder eventUrls(List<String> eventUrls) {
            values.put("event_urls", List.copyOf(eventUrls));
            return this;
        }

        /**
         * Shows: The address of each show's exhibitor list or floor plan on its a2z site (a2zinc.net), as your
         * browser shows it. 1 to 10 items. Required.
         *
         * @param eventUrls the values
         * @return this builder
         */
        public Builder eventUrls(String... eventUrls) {
            return eventUrls(Arrays.asList(eventUrls));
        }

        /**
         * Categories or words: Keep exhibitors whose categories, products, brands or description name one of these,
         * as whole words. Empty keeps every exhibitor. At most 20 items. Default: [].
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Categories or words: Keep exhibitors whose categories, products, brands or description name one of these,
         * as whole words. Empty keeps every exhibitor. At most 20 items. Default: [].
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Your show: The a2z address of your own show. Its exhibitors are left out, to find the companies that
         * exhibit elsewhere but not with you. Default: "".
         *
         * @param yourEventUrl the value
         * @return this builder
         */
        public Builder yourEventUrl(String yourEventUrl) {
            values.put("your_event_url", yourEventUrl);
            return this;
        }

        /**
         * Countries: Keep exhibitors from these countries, in words as the shows write them, such as United States
         * or Canada. Empty keeps all. At most 50 items. Default: [].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Countries: Keep exhibitors from these countries, in words as the shows write them, such as United States
         * or Canada. Empty keeps all. At most 50 items. Default: [].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Only upgraded listings: Keep exhibitors who paid the show for an enhanced listing, a sign of a bigger
         * exhibiting budget. Default: false.
         *
         * @param enhancedOnly the value
         * @return this builder
         */
        public Builder enhancedOnly(boolean enhancedOnly) {
            values.put("enhanced_only", enhancedOnly);
            return this;
        }

        /**
         * Fewest shows: With several shows, keep companies that exhibit at least at this many of them. From 1 to
         * 10. Default: 1.
         *
         * @param minShows the value
         * @return this builder
         */
        public Builder minShows(int minShows) {
            values.put("min_shows", minShows);
            return this;
        }

        /**
         * Your accounts: Company names or domains from your CRM, matched against each exhibitor's name and website.
         * At most 5000 items. Default: [].
         *
         * @param crmCompanies the values; not null
         * @return this builder
         */
        public Builder crmCompanies(List<String> crmCompanies) {
            values.put("crm_companies", List.copyOf(crmCompanies));
            return this;
        }

        /**
         * Your accounts: Company names or domains from your CRM, matched against each exhibitor's name and website.
         * At most 5000 items. Default: [].
         *
         * @param crmCompanies the values
         * @return this builder
         */
        public Builder crmCompanies(String... crmCompanies) {
            return crmCompanies(Arrays.asList(crmCompanies));
        }

        /**
         * Your accounts are: Use your accounts to keep only those exhibiting, or to leave them out and see only new
         * companies. One of "ignore", "only_accounts", "leave_out_accounts". Default: "ignore".
         *
         * @param crmMatch the value
         * @return this builder
         */
        public Builder crmMatch(String crmMatch) {
            values.put("crm_match", crmMatch);
            return this;
        }

        /**
         * Read each website for an address: The address the company's own website publishes, checked. Off saves one
         * read a company. Default: true.
         *
         * @param includeSiteContact the value
         * @return this builder
         */
        public Builder includeSiteContact(boolean includeSiteContact) {
            values.put("include_site_contact", includeSiteContact);
            return this;
        }

        /**
         * Show a company again after (days): For a saved formula, how long before a delivered company may come
         * back. From 1 to 365. Default: 120.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most exhibitors read: The exhibitors whose show pages are read, most shows and upgraded listings first;
         * words and countries then keep those that match. A show page and a website are one read each. From 1 to
         * 1000. Default: 100.
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
        public ExhibitorProspectsParams build() {
            if (!values.containsKey("event_urls")) {
                throw new IllegalStateException("ExhibitorProspectsParams: event_urls is required");
            }
            return new ExhibitorProspectsParams(values);
        }
    }
}
