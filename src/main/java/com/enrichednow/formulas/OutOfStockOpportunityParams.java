// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Out-of-Stock Opportunity ({@code out-of-stock-opportunity}): Amazon best sellers that are out
 * of stock on Amazon, and the US retailers that have the same product in stock now.
 *
 * Only {@code categories} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * OutOfStockOpportunityParams params = OutOfStockOpportunityParams.builder()
 *     .categories("toys-and-games")
 *     .build();
 * }</pre>
 */
public final class OutOfStockOpportunityParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "out-of-stock-opportunity";

    private final Map<String, Object> values;

    private OutOfStockOpportunityParams(Map<String, Object> values) {
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
        return "OutOfStockOpportunityParams" + values;
    }

    /** Builds {@link OutOfStockOpportunityParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Best Sellers categories: As the list's address names them, such as toys-and-games, electronics, kitchen,
         * or electronics/172456 for a category further down. 1 to 10 items. Required.
         *
         * @param categories the values; not null
         * @return this builder
         */
        public Builder categories(List<String> categories) {
            values.put("categories", List.copyOf(categories));
            return this;
        }

        /**
         * Best Sellers categories: As the list's address names them, such as toys-and-games, electronics, kitchen,
         * or electronics/172456 for a category further down. 1 to 10 items. Required.
         *
         * @param categories the values
         * @return this builder
         */
        public Builder categories(String... categories) {
            return categories(Arrays.asList(categories));
        }

        /**
         * Retailers: Where to look for stock. Walmart often answers automated visits with a check, so its products
         * may be missing. Each one of "target", "bestbuy", "newegg", "walmart". At least 1 item. Default:
         * ["target", "bestbuy"].
         *
         * @param retailers the values; not null
         * @return this builder
         */
        public Builder retailers(List<String> retailers) {
            values.put("retailers", List.copyOf(retailers));
            return this;
        }

        /**
         * Retailers: Where to look for stock. Walmart often answers automated visits with a check, so its products
         * may be missing. Each one of "target", "bestbuy", "newegg", "walmart". At least 1 item. Default:
         * ["target", "bestbuy"].
         *
         * @param retailers the values
         * @return this builder
         */
        public Builder retailers(String... retailers) {
            return retailers(Arrays.asList(retailers));
        }

        /**
         * List pages: Page 1 is ranks 1 to 50, page 2 ranks 51 to 100. Each one of 1, 2. At least 1 item. Default:
         * [1, 2].
         *
         * @param pages the values; not null
         * @return this builder
         */
        public Builder pages(List<Integer> pages) {
            values.put("pages", List.copyOf(pages));
            return this;
        }

        /**
         * List pages: Page 1 is ranks 1 to 50, page 2 ranks 51 to 100. Each one of 1, 2. At least 1 item. Default:
         * [1, 2].
         *
         * @param pages the values
         * @return this builder
         */
        public Builder pages(Integer... pages) {
            return pages(Arrays.asList(pages));
        }

        /**
         * Results compared per retailer. From 1 to 5. Default: 2.
         *
         * @param candidatesPerRetailer the value
         * @return this builder
         */
        public Builder candidatesPerRetailer(int candidatesPerRetailer) {
            values.put("candidates_per_retailer", candidatesPerRetailer);
            return this;
        }

        /**
         * Most rows: The run stops at this many product and retailer pairs, best rank first. From 1 to 500.
         * Default: 100.
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
        public OutOfStockOpportunityParams build() {
            if (!values.containsKey("categories")) {
                throw new IllegalStateException("OutOfStockOpportunityParams: categories is required");
            }
            return new OutOfStockOpportunityParams(values);
        }
    }
}
