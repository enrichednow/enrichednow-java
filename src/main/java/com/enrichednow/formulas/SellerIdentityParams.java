// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Seller Identity ({@code seller-identity}): The legal business behind each marketplace seller of
 * your products, with its address, website, domain record and register entry.
 *
 * Only {@code asins} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * SellerIdentityParams params = SellerIdentityParams.builder()
 *     .asins("B09GJVTRNZ")
 *     .build();
 * }</pre>
 */
public final class SellerIdentityParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "seller-identity";

    private final Map<String, Object> values;

    private SellerIdentityParams(Map<String, Object> values) {
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
        return "SellerIdentityParams" + values;
    }

    /** Builds {@link SellerIdentityParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Your products on Amazon: ASINs whose sellers to identify. Every seller with an offer on them is read. At
         * most 30 items. Required.
         *
         * @param asins the values; not null
         * @return this builder
         */
        public Builder asins(List<String> asins) {
            values.put("asins", List.copyOf(asins));
            return this;
        }

        /**
         * Your products on Amazon: ASINs whose sellers to identify. Every seller with an offer on them is read. At
         * most 30 items. Required.
         *
         * @param asins the values
         * @return this builder
         */
        public Builder asins(String... asins) {
            return asins(Arrays.asList(asins));
        }

        /**
         * Amazon seller ids: Sellers to identify directly, by the id in their storefront's address (seller=...). At
         * most 100 items. Default: [].
         *
         * @param sellerIds the values; not null
         * @return this builder
         */
        public Builder sellerIds(List<String> sellerIds) {
            values.put("seller_ids", List.copyOf(sellerIds));
            return this;
        }

        /**
         * Amazon seller ids: Sellers to identify directly, by the id in their storefront's address (seller=...). At
         * most 100 items. Default: [].
         *
         * @param sellerIds the values
         * @return this builder
         */
        public Builder sellerIds(String... sellerIds) {
            return sellerIds(Arrays.asList(sellerIds));
        }

        /**
         * Walmart sellers: Walmart Marketplace sellers by id or storefront address. At most 100 items. Default: [].
         *
         * @param walmartSellers the values; not null
         * @return this builder
         */
        public Builder walmartSellers(List<String> walmartSellers) {
            values.put("walmart_sellers", List.copyOf(walmartSellers));
            return this;
        }

        /**
         * Walmart sellers: Walmart Marketplace sellers by id or storefront address. At most 100 items. Default: [].
         *
         * @param walmartSellers the values
         * @return this builder
         */
        public Builder walmartSellers(String... walmartSellers) {
            return walmartSellers(Arrays.asList(walmartSellers));
        }

        /**
         * Amazon marketplace: Which Amazon the products and sellers are on. One of "us", "uk", "de", "fr", "it",
         * "es", "ca". Default: "us".
         *
         * @param marketplace the value
         * @return this builder
         */
        public Builder marketplace(String marketplace) {
            values.put("marketplace", marketplace);
            return this;
        }

        /**
         * Authorised sellers: Storefront names to leave out, such as your own and your distributors'. At most 500
         * items. Default: ["Amazon.com", "Amazon"].
         *
         * @param authorisedSellers the values; not null
         * @return this builder
         */
        public Builder authorisedSellers(List<String> authorisedSellers) {
            values.put("authorised_sellers", List.copyOf(authorisedSellers));
            return this;
        }

        /**
         * Authorised sellers: Storefront names to leave out, such as your own and your distributors'. At most 500
         * items. Default: ["Amazon.com", "Amazon"].
         *
         * @param authorisedSellers the values
         * @return this builder
         */
        public Builder authorisedSellers(String... authorisedSellers) {
            return authorisedSellers(Arrays.asList(authorisedSellers));
        }

        /**
         * Find each business's website and its domain record: Looks the business up by name, and reads the
         * registration of the domain it finds. Default: true.
         *
         * @param findWebsite the value
         * @return this builder
         */
        public Builder findWebsite(boolean findWebsite) {
            values.put("find_website", findWebsite);
            return this;
        }

        /**
         * Look UK businesses up at Companies House: For a seller whose address is in the United Kingdom. Default:
         * true.
         *
         * @param checkCompanyRegister the value
         * @return this builder
         */
        public Builder checkCompanyRegister(boolean checkCompanyRegister) {
            values.put("check_company_register", checkCompanyRegister);
            return this;
        }

        /**
         * Most rows: The run stops at this many sellers, the most identified first. From 1 to 500. Default: 100.
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
        public SellerIdentityParams build() {
            if (!values.containsKey("asins")) {
                throw new IllegalStateException("SellerIdentityParams: asins is required");
            }
            return new SellerIdentityParams(values);
        }
    }
}
