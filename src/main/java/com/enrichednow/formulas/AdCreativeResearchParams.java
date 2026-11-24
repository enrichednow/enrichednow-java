// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Ad Creative Research ({@code ad-creative-research}): A competitor's ads on Meta, Google Ads and
 * LinkedIn, with copy, landing page and how long each has run, longest first.
 *
 * Only {@code competitors} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * AdCreativeResearchParams params = AdCreativeResearchParams.builder()
 *     .competitors("Canva")
 *     .build();
 * }</pre>
 */
public final class AdCreativeResearchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "ad-creative-research";

    private final Map<String, Object> values;

    private AdCreativeResearchParams(Map<String, Object> values) {
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
        return "AdCreativeResearchParams" + values;
    }

    /** Builds {@link AdCreativeResearchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Competitors: Each competitor's name as it advertises, such as Canva or HubSpot. Ads are kept where the
         * advertiser's name holds it as a whole word. 1 to 10 items. Required.
         *
         * @param competitors the values; not null
         * @return this builder
         */
        public Builder competitors(List<String> competitors) {
            values.put("competitors", List.copyOf(competitors));
            return this;
        }

        /**
         * Competitors: Each competitor's name as it advertises, such as Canva or HubSpot. Ads are kept where the
         * advertiser's name holds it as a whole word. 1 to 10 items. Required.
         *
         * @param competitors the values
         * @return this builder
         */
        public Builder competitors(String... competitors) {
            return competitors(Arrays.asList(competitors));
        }

        /**
         * Their websites: The competitors' websites, such as canva.com. Google Ads are found by the website they
         * lead to, so Google Ads is searched only when you give one. At most 10 items. Default: [].
         *
         * @param domains the values; not null
         * @return this builder
         */
        public Builder domains(List<String> domains) {
            values.put("domains", List.copyOf(domains));
            return this;
        }

        /**
         * Their websites: The competitors' websites, such as canva.com. Google Ads are found by the website they
         * lead to, so Google Ads is searched only when you give one. At most 10 items. Default: [].
         *
         * @param domains the values
         * @return this builder
         */
        public Builder domains(String... domains) {
            return domains(Arrays.asList(domains));
        }

        /**
         * Ad libraries: Meta covers Facebook, Instagram, Messenger and Threads; Google Ads covers Search, YouTube
         * and the display network. Each one of "meta", "google", "linkedin". At least 1 item. Default: ["meta",
         * "google", "linkedin"].
         *
         * @param libraries the values; not null
         * @return this builder
         */
        public Builder libraries(List<String> libraries) {
            values.put("libraries", List.copyOf(libraries));
            return this;
        }

        /**
         * Ad libraries: Meta covers Facebook, Instagram, Messenger and Threads; Google Ads covers Search, YouTube
         * and the display network. Each one of "meta", "google", "linkedin". At least 1 item. Default: ["meta",
         * "google", "linkedin"].
         *
         * @param libraries the values
         * @return this builder
         */
        public Builder libraries(String... libraries) {
            return libraries(Arrays.asList(libraries));
        }

        /**
         * Country: Only ads shown in this country, as a two-letter code such as US or DE. Leave empty for every
         * country. Default: "".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Only ads running now: Leaves out ads that have stopped. Off, a stopped ad still shows how long an angle
         * lasted. Default: false.
         *
         * @param runningOnly the value
         * @return this builder
         */
        public Builder runningOnly(boolean runningOnly) {
            values.put("running_only", runningOnly);
            return this;
        }

        /**
         * Least days running: Only ads that have run at least this many days; 30 keeps the angles an advertiser
         * kept paying for. From 0 to 3650. Default: 0.
         *
         * @param minDaysRunning the value
         * @return this builder
         */
        public Builder minDaysRunning(int minDaysRunning) {
            values.put("min_days_running", minDaysRunning);
            return this;
        }

        /**
         * Facebook Page ids: For an exact Meta search, the competitor's Page id (the number after view_all_page_id=
         * in the Ad Library's address). Every ad of the Page is read. At most 10 items. Default: [].
         *
         * @param facebookPageIds the values; not null
         * @return this builder
         */
        public Builder facebookPageIds(List<String> facebookPageIds) {
            values.put("facebook_page_ids", List.copyOf(facebookPageIds));
            return this;
        }

        /**
         * Facebook Page ids: For an exact Meta search, the competitor's Page id (the number after view_all_page_id=
         * in the Ad Library's address). Every ad of the Page is read. At most 10 items. Default: [].
         *
         * @param facebookPageIds the values
         * @return this builder
         */
        public Builder facebookPageIds(String... facebookPageIds) {
            return facebookPageIds(Arrays.asList(facebookPageIds));
        }

        /**
         * Ads read per search: The most ads read for each competitor in each library. Each ad read is one unit.
         * From 5 to 120. Default: 30.
         *
         * @param adsPerSearch the value
         * @return this builder
         */
        public Builder adsPerSearch(int adsPerSearch) {
            values.put("ads_per_search", adsPerSearch);
            return this;
        }

        /**
         * Read each LinkedIn ad's page: LinkedIn's search shows no dates or landing page; each ad's own page has
         * them, for one unit an ad. Default: true.
         *
         * @param linkedinAdPages the value
         * @return this builder
         */
        public Builder linkedinAdPages(boolean linkedinAdPages) {
            values.put("linkedin_ad_pages", linkedinAdPages);
            return this;
        }

        /**
         * Most rows: The run stops at this many ads, longest running first. From 1 to 2000. Default: 200.
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
        public AdCreativeResearchParams build() {
            if (!values.containsKey("competitors")) {
                throw new IllegalStateException("AdCreativeResearchParams: competitors is required");
            }
            return new AdCreativeResearchParams(values);
        }
    }
}
