// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Supplier Discovery ({@code supplier-discovery}): Suppliers of a product from IndiaMART, Google
 * Maps, Gelbe Seiten and ThomasNet, each with location, website and email.
 *
 * Only {@code products} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * SupplierDiscoveryParams params = SupplierDiscoveryParams.builder()
 *     .products("CNC machining", "sheet metal fabrication")
 *     .build();
 * }</pre>
 */
public final class SupplierDiscoveryParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "supplier-discovery";

    private final Map<String, Object> values;

    private SupplierDiscoveryParams(Map<String, Object> values) {
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
        return "SupplierDiscoveryParams" + values;
    }

    /** Builds {@link SupplierDiscoveryParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Products: What you buy, as a supplier would list it, such as CNC machined parts or corrugated boxes. Each
         * is searched on its own. 1 to 5 items. Required.
         *
         * @param products the values; not null
         * @return this builder
         */
        public Builder products(List<String> products) {
            values.put("products", List.copyOf(products));
            return this;
        }

        /**
         * Products: What you buy, as a supplier would list it, such as CNC machined parts or corrugated boxes. Each
         * is searched on its own. 1 to 5 items. Required.
         *
         * @param products the values
         * @return this builder
         */
        public Builder products(String... products) {
            return products(Arrays.asList(products));
        }

        /**
         * Places: Towns or regions to find suppliers in on Google Maps and Gelbe Seiten, such as Cleveland, OH or
         * Stuttgart. IndiaMART is searched across India whatever is given here. At most 5 items. Default: [].
         *
         * @param places the values; not null
         * @return this builder
         */
        public Builder places(List<String> places) {
            values.put("places", List.copyOf(places));
            return this;
        }

        /**
         * Places: Towns or regions to find suppliers in on Google Maps and Gelbe Seiten, such as Cleveland, OH or
         * Stuttgart. IndiaMART is searched across India whatever is given here. At most 5 items. Default: [].
         *
         * @param places the values
         * @return this builder
         */
        public Builder places(String... places) {
            return places(Arrays.asList(places));
        }

        /**
         * Directories: IndiaMART (India), Google Maps (anywhere, by place), Gelbe Seiten (Germany, by town) and
         * ThomasNet (the profiles you give). Each one of "indiamart", "google_maps", "gelbeseiten", "thomasnet". At
         * least 1 item. Default: ["indiamart", "google_maps"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Directories: IndiaMART (India), Google Maps (anywhere, by place), Gelbe Seiten (Germany, by town) and
         * ThomasNet (the profiles you give). Each one of "indiamart", "google_maps", "gelbeseiten", "thomasnet". At
         * least 1 item. Default: ["indiamart", "google_maps"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * ThomasNet profiles: The links or ids of ThomasNet supplier profiles you have shortlisted, read in full
         * when ThomasNet is chosen. At most 50 items. Default: [].
         *
         * @param thomasnetProfiles the values; not null
         * @return this builder
         */
        public Builder thomasnetProfiles(List<String> thomasnetProfiles) {
            values.put("thomasnet_profiles", List.copyOf(thomasnetProfiles));
            return this;
        }

        /**
         * ThomasNet profiles: The links or ids of ThomasNet supplier profiles you have shortlisted, read in full
         * when ThomasNet is chosen. At most 50 items. Default: [].
         *
         * @param thomasnetProfiles the values
         * @return this builder
         */
        public Builder thomasnetProfiles(String... thomasnetProfiles) {
            return thomasnetProfiles(Arrays.asList(thomasnetProfiles));
        }

        /**
         * Map listings per search: How many Google Maps listings are read for each product and place. From 5 to 60.
         * Default: 20.
         *
         * @param perSearch the value
         * @return this builder
         */
        public Builder perSearch(int perSearch) {
            values.put("per_search", perSearch);
            return this;
        }

        /**
         * Only verified IndiaMART sellers: Keeps the IndiaMART sellers it shows as verified or holding its
         * TrustSEAL. Default: false.
         *
         * @param verifiedOnly the value
         * @return this builder
         */
        public Builder verifiedOnly(boolean verifiedOnly) {
            values.put("verified_only", verifiedOnly);
            return this;
        }

        /**
         * Lowest rating: Leaves out suppliers rated lower by their buyers or reviewers. A supplier with no rating
         * is kept. From 0 to 5. Default: 0.
         *
         * @param minRating the value
         * @return this builder
         */
        public Builder minRating(double minRating) {
            values.put("min_rating", minRating);
            return this;
        }

        /**
         * Suppliers to leave out: Names, such as the suppliers you already buy from. At most 1000 items. Default:
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
         * Suppliers to leave out: Names, such as the suppliers you already buy from. At most 1000 items. Default:
         * [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Websites to leave out: Domains, such as acme.com, of the suppliers you already buy from. At most 1000
         * items. Default: [].
         *
         * @param excludeDomains the values; not null
         * @return this builder
         */
        public Builder excludeDomains(List<String> excludeDomains) {
            values.put("exclude_domains", List.copyOf(excludeDomains));
            return this;
        }

        /**
         * Websites to leave out: Domains, such as acme.com, of the suppliers you already buy from. At most 1000
         * items. Default: [].
         *
         * @param excludeDomains the values
         * @return this builder
         */
        public Builder excludeDomains(String... excludeDomains) {
            return excludeDomains(Arrays.asList(excludeDomains));
        }

        /**
         * Read each IndiaMART product page: Adds the supplier's town and business type, such as manufacturer or
         * exporter. One unit for each IndiaMART supplier. Default: true.
         *
         * @param indiamartDetails the value
         * @return this builder
         */
        public Builder indiamartDetails(boolean indiamartDetails) {
            values.put("indiamart_details", indiamartDetails);
            return this;
        }

        /**
         * Only suppliers with an email: Keeps the suppliers whose own website publishes an email address that
         * passes the check. Default: false.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Most rows: The run stops at this many suppliers. From 1 to 1000. Default: 100.
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
        public SupplierDiscoveryParams build() {
            if (!values.containsKey("products")) {
                throw new IllegalStateException("SupplierDiscoveryParams: products is required");
            }
            return new SupplierDiscoveryParams(values);
        }
    }
}
