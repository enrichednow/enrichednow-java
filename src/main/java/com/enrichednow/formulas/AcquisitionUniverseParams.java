// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Acquisition Universe ({@code acquisition-universe}): Every business of your acquisition thesis
 * in your metros, off the market from Google Maps and on it from sale listings.
 *
 * Only {@code thesis}, {@code metros} are required: {@link Builder#build()} refuses to build without them. A value
 * not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * AcquisitionUniverseParams params = AcquisitionUniverseParams.builder()
 *     .thesis("HVAC contractor")
 *     .metros("Austin, TX")
 *     .build();
 * }</pre>
 */
public final class AcquisitionUniverseParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "acquisition-universe";

    private final Map<String, Object> values;

    private AcquisitionUniverseParams(Map<String, Object> values) {
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
        return "AcquisitionUniverseParams" + values;
    }

    /** Builds {@link AcquisitionUniverseParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Kinds of business: What you would buy, as you would search Google Maps for it, such as HVAC contractor or
         * landscaping company. 1 to 5 items. Required.
         *
         * @param thesis the values; not null
         * @return this builder
         */
        public Builder thesis(List<String> thesis) {
            values.put("thesis", List.copyOf(thesis));
            return this;
        }

        /**
         * Kinds of business: What you would buy, as you would search Google Maps for it, such as HVAC contractor or
         * landscaping company. 1 to 5 items. Required.
         *
         * @param thesis the values
         * @return this builder
         */
        public Builder thesis(String... thesis) {
            return thesis(Arrays.asList(thesis));
        }

        /**
         * Metros: A city or metro for each place, with its state. 1 to 10 items. Required.
         *
         * @param metros the values; not null
         * @return this builder
         */
        public Builder metros(List<String> metros) {
            values.put("metros", List.copyOf(metros));
            return this;
        }

        /**
         * Metros: A city or metro for each place, with its state. 1 to 10 items. Required.
         *
         * @param metros the values
         * @return this builder
         */
        public Builder metros(String... metros) {
            return metros(Arrays.asList(metros));
        }

        /**
         * States for listings: The US states, by name, whose businesses for sale are read. Empty reads none. At
         * most 10 items. Default: [].
         *
         * @param states the values; not null
         * @return this builder
         */
        public Builder states(List<String> states) {
            values.put("states", List.copyOf(states));
            return this;
        }

        /**
         * States for listings: The US states, by name, whose businesses for sale are read. Empty reads none. At
         * most 10 items. Default: [].
         *
         * @param states the values
         * @return this builder
         */
        public Builder states(String... states) {
            return states(Arrays.asList(states));
        }

        /**
         * Words a listing must hold: Whole words a for-sale listing's title or summary must hold, such as HVAC or
         * air conditioning. Empty uses the kinds of business. At most 10 items. Default: [].
         *
         * @param listingWords the values; not null
         * @return this builder
         */
        public Builder listingWords(List<String> listingWords) {
            values.put("listing_words", List.copyOf(listingWords));
            return this;
        }

        /**
         * Words a listing must hold: Whole words a for-sale listing's title or summary must hold, such as HVAC or
         * air conditioning. Empty uses the kinds of business. At most 10 items. Default: [].
         *
         * @param listingWords the values
         * @return this builder
         */
        public Builder listingWords(String... listingWords) {
            return listingWords(Arrays.asList(listingWords));
        }

        /**
         * Places read per search: How many places Google Maps is read for, for each kind of business in each metro.
         * From 10 to 300. Default: 60.
         *
         * @param placesPerSearch the value
         * @return this builder
         */
        public Builder placesPerSearch(int placesPerSearch) {
            values.put("places_per_search", placesPerSearch);
            return this;
        }

        /**
         * Fewest reviews: Places with fewer reviews are left out; a business with none may be too small or not
         * trading. At least 0. Default: 0.
         *
         * @param minReviews the value
         * @return this builder
         */
        public Builder minReviews(int minReviews) {
            values.put("min_reviews", minReviews);
            return this;
        }

        /**
         * Most reviews: Places with more reviews are left out; the largest are rarely owner-run. At least 0.
         * Default: 1000000.
         *
         * @param maxReviews the value
         * @return this builder
         */
        public Builder maxReviews(int maxReviews) {
            values.put("max_reviews", maxReviews);
            return this;
        }

        /**
         * Leave out chains: A name found five or more times in the results is taken for a chain and left out.
         * Default: true.
         *
         * @param excludeChains the value
         * @return this builder
         */
        public Builder excludeChains(boolean excludeChains) {
            values.put("exclude_chains", excludeChains);
            return this;
        }

        /**
         * Larger companies without their name: BusinessesForSale.com's M&amp;A Vault describes larger companies
         * without naming them, from any country. Default: false.
         *
         * @param includeVault the value
         * @return this builder
         */
        public Builder includeVault(boolean includeVault) {
            values.put("include_vault", includeVault);
            return this;
        }

        /**
         * Read each website for an address: The address each business's own website publishes, checked. Off saves
         * one read a business. Default: true.
         *
         * @param findEmail the value
         * @return this builder
         */
        public Builder findEmail(boolean findEmail) {
            values.put("find_email", findEmail);
            return this;
        }

        /**
         * Show a business again after (days): For a saved formula, how long before a delivered business or listing
         * may come back. From 1 to 365. Default: 180.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, listings first, then the places with the most reviews. From 1
         * to 5000. Default: 300.
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
        public AcquisitionUniverseParams build() {
            if (!values.containsKey("thesis")) {
                throw new IllegalStateException("AcquisitionUniverseParams: thesis is required");
            }
            if (!values.containsKey("metros")) {
                throw new IllegalStateException("AcquisitionUniverseParams: metros is required");
            }
            return new AcquisitionUniverseParams(values);
        }
    }
}
