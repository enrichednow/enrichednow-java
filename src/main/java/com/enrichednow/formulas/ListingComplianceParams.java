// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Listing Compliance ({@code listing-compliance}): Your Amazon listings checked against your own
 * content rules (brand, title words, images, bullets, Buy Box seller), with changes.
 *
 * Only {@code asins} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * ListingComplianceParams params = ListingComplianceParams.builder()
 *     .asins("B09GJVTRNZ", "B08X4YMTPM")
 *     .build();
 * }</pre>
 */
public final class ListingComplianceParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "listing-compliance";

    private final Map<String, Object> values;

    private ListingComplianceParams(Map<String, Object> values) {
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
        return "ListingComplianceParams" + values;
    }

    /** Builds {@link ListingComplianceParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Your listings: The ASINs to check. 1 to 200 items. Required.
         *
         * @param asins the values; not null
         * @return this builder
         */
        public Builder asins(List<String> asins) {
            values.put("asins", List.copyOf(asins));
            return this;
        }

        /**
         * Your listings: The ASINs to check. 1 to 200 items. Required.
         *
         * @param asins the values
         * @return this builder
         */
        public Builder asins(String... asins) {
            return asins(Arrays.asList(asins));
        }

        /**
         * Brand the listing must show: As your brand is written, such as JBL. Empty skips the check. Default: "".
         *
         * @param brand the value
         * @return this builder
         */
        public Builder brand(String brand) {
            values.put("brand", brand);
            return this;
        }

        /**
         * Words every title must hold: Such as your brand or the product line. At most 20 items. Default: [].
         *
         * @param requiredTitleWords the values; not null
         * @return this builder
         */
        public Builder requiredTitleWords(List<String> requiredTitleWords) {
            values.put("required_title_words", List.copyOf(requiredTitleWords));
            return this;
        }

        /**
         * Words every title must hold: Such as your brand or the product line. At most 20 items. Default: [].
         *
         * @param requiredTitleWords the values
         * @return this builder
         */
        public Builder requiredTitleWords(String... requiredTitleWords) {
            return requiredTitleWords(Arrays.asList(requiredTitleWords));
        }

        /**
         * Amazon marketplace. One of "us", "uk", "de", "fr", "it", "es", "ca". Default: "us".
         *
         * @param marketplace the value
         * @return this builder
         */
        public Builder marketplace(String marketplace) {
            values.put("marketplace", marketplace);
            return this;
        }

        /**
         * Words no title may hold. At most 30 items. Default: ["refurbished", "renewed", "used", "replica",
         * "compatible with", "generic", "unbranded"].
         *
         * @param forbiddenWords the values; not null
         * @return this builder
         */
        public Builder forbiddenWords(List<String> forbiddenWords) {
            values.put("forbidden_words", List.copyOf(forbiddenWords));
            return this;
        }

        /**
         * Words no title may hold. At most 30 items. Default: ["refurbished", "renewed", "used", "replica",
         * "compatible with", "generic", "unbranded"].
         *
         * @param forbiddenWords the values
         * @return this builder
         */
        public Builder forbiddenWords(String... forbiddenWords) {
            return forbiddenWords(Arrays.asList(forbiddenWords));
        }

        /**
         * Least images. From 0 to 20. Default: 6.
         *
         * @param minImages the value
         * @return this builder
         */
        public Builder minImages(int minImages) {
            values.put("min_images", minImages);
            return this;
        }

        /**
         * Least bullet points. From 0 to 10. Default: 5.
         *
         * @param minBullets the value
         * @return this builder
         */
        public Builder minBullets(int minBullets) {
            values.put("min_bullets", minBullets);
            return this;
        }

        /**
         * Longest title (characters). From 50 to 500. Default: 200.
         *
         * @param maxTitleLength the value
         * @return this builder
         */
        public Builder maxTitleLength(int maxTitleLength) {
            values.put("max_title_length", maxTitleLength);
            return this;
        }

        /**
         * Sellers who may hold the Buy Box: Such as Amazon.com and your own storefront. Empty skips the check. At
         * most 100 items. Default: [].
         *
         * @param buyBoxSellers the values; not null
         * @return this builder
         */
        public Builder buyBoxSellers(List<String> buyBoxSellers) {
            values.put("buy_box_sellers", List.copyOf(buyBoxSellers));
            return this;
        }

        /**
         * Sellers who may hold the Buy Box: Such as Amazon.com and your own storefront. Empty skips the check. At
         * most 100 items. Default: [].
         *
         * @param buyBoxSellers the values
         * @return this builder
         */
        public Builder buyBoxSellers(String... buyBoxSellers) {
            return buyBoxSellers(Arrays.asList(buyBoxSellers));
        }

        /**
         * Only listings with a breach or a change. Default: false.
         *
         * @param onlyBreaches the value
         * @return this builder
         */
        public Builder onlyBreaches(boolean onlyBreaches) {
            values.put("only_breaches", onlyBreaches);
            return this;
        }

        /**
         * Most rows: The run stops at this many listings, most breaches first. From 1 to 500. Default: 200.
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
        public ListingComplianceParams build() {
            if (!values.containsKey("asins")) {
                throw new IllegalStateException("ListingComplianceParams: asins is required");
            }
            return new ListingComplianceParams(values);
        }
    }
}
