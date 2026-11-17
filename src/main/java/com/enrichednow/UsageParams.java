package com.enrichednow;

import java.time.LocalDate;

/** Which usage to report. */
public final class UsageParams {
    private static final UsageParams NONE = builder().build();

    /** What a row of the report stands for. */
    public enum GroupBy {
        /** A row for every day of the period. The default. */
        DAY("day"),
        /** A row for every scraper that was charged, most units first. */
        SCRAPER("scraper"),
        /** The scraper product and the data product apart. */
        PRODUCT("product");

        final String value;

        GroupBy(String value) {
            this.value = value;
        }
    }

    /** One of the two products an account can have. Each draws on its own allowance. */
    public enum Product {
        /** The Scraper API. */
        SCRAPERS("scrapers"),
        /** Datasets. */
        DATA("data");

        final String value;

        Product(String value) {
            this.value = value;
        }
    }

    final LocalDate from;
    final LocalDate to;
    final GroupBy groupBy;
    final Product product;

    private UsageParams(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.groupBy = builder.groupBy;
        this.product = builder.product;
    }

    /** @return the defaults: the current calendar month (UTC) up to today, by day, both products */
    public static UsageParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UsageParams}. */
    public static final class Builder {
        private LocalDate from;
        private LocalDate to;
        private GroupBy groupBy;
        private Product product;

        private Builder() {
        }

        /**
         * @param from the first day (UTC), included; default the first day of the month of {@code to}
         * @return this builder
         */
        public Builder from(LocalDate from) {
            this.from = from;
            return this;
        }

        /**
         * @param to the last day (UTC), included; default today. A period is at most 366 days
         * @return this builder
         */
        public Builder to(LocalDate to) {
            this.to = to;
            return this;
        }

        /**
         * @param groupBy what a row stands for; default {@link GroupBy#DAY}
         * @return this builder
         */
        public Builder groupBy(GroupBy groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        /**
         * @param product only this product's usage, and its allowance; default both products
         * @return this builder
         */
        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        /** @return the parameters */
        public UsageParams build() {
            return new UsageParams(this);
        }
    }
}
