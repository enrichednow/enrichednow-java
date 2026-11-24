package com.enrichednow;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Which rows to read, of a formula run or of a saved formula, and how. Every filter given must hold.
 *
 * <pre>{@code
 * FormulaRowListParams.builder()
 *         .fields("company_name", "job_title", "contact_email")   // only these columns
 *         .minScore(10)                                           // rows scored at least 10
 *         .limit(500)                                             // rows a page
 *         .build();
 * }</pre>
 *
 * <p>{@link #view} is for a run's rows; {@link #since} and {@link #order} for a saved formula's.
 */
public final class FormulaRowListParams {
    private static final FormulaRowListParams NONE = builder().build();

    /** How a run's rows are read. */
    public enum View {
        /** A row a result, as the run wrote them. The default. */
        ROWS("rows"),
        /** One row per company, its other values gathered into lists: for a formula with a {@code companyView()}. */
        COMPANY("company");

        final String value;

        View(String value) {
            this.value = value;
        }
    }

    /** The order of a saved formula's rows. */
    public enum Order {
        /** In the order they were written. The default: what a poller wants. */
        ASC("asc"),
        /** Newest first. */
        DESC("desc");

        final String value;

        Order(String value) {
            this.value = value;
        }
    }

    final List<String> fields;
    final Double minScore;
    final View view;
    final Instant since;
    final Order order;
    final Integer limit;
    final String cursor;

    private FormulaRowListParams(Builder builder) {
        this.fields = builder.fields;
        this.minScore = builder.minScore;
        this.view = builder.view;
        this.since = builder.since;
        this.order = builder.order;
        this.limit = builder.limit;
        this.cursor = builder.cursor;
    }

    /** @return the defaults: every column of every row, 100 a page, from the first */
    public static FormulaRowListParams none() {
        return NONE;
    }

    /**
     * @param limit rows per page: 1 to 1000
     * @return parameters with this page size
     */
    public static FormulaRowListParams limit(int limit) {
        return builder().limit(limit).build();
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** These parameters, with this page size when they name none. */
    FormulaRowListParams orLimit(int pageSize) {
        if (limit != null) {
            return this;
        }
        Builder copy = new Builder();
        copy.fields = fields;
        copy.minScore = minScore;
        copy.view = view;
        copy.since = since;
        copy.order = order;
        copy.cursor = cursor;
        copy.limit = pageSize;
        return copy.build();
    }

    /** @return the columns asked for, comma-joined as the API takes them; null for every column */
    String fieldList() {
        return fields == null ? null : String.join(",", fields);
    }

    /** Builds {@link FormulaRowListParams}. */
    public static final class Builder {
        private List<String> fields;
        private Double minScore;
        private View view;
        private Instant since;
        private Order order;
        private Integer limit;
        private String cursor;

        private Builder() {
        }

        /**
         * @param fields only these columns of each row, by key, e.g. {@code company_name}; the formula's
         *               {@code columns()} lists them
         * @return this builder
         */
        public Builder fields(List<String> fields) {
            Objects.requireNonNull(fields, "fields");
            if (fields.isEmpty()) {
                throw new IllegalArgumentException("fields must name at least one column");
            }
            this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
            return this;
        }

        /**
         * @param fields only these columns of each row, by key
         * @return this builder
         */
        public Builder fields(String... fields) {
            return fields(Arrays.asList(fields));
        }

        /**
         * @param minScore only rows scored at least this
         * @return this builder
         */
        public Builder minScore(double minScore) {
            this.minScore = minScore;
            return this;
        }

        /**
         * @param view a run's rows one per company ({@link View#COMPANY}), for a formula that has that view
         * @return this builder
         */
        public Builder view(View view) {
            this.view = view;
            return this;
        }

        /**
         * @param since a saved formula's rows written at or after this time: what is new since the last poll
         * @return this builder
         */
        public Builder since(Instant since) {
            this.since = since;
            return this;
        }

        /**
         * @param order a saved formula's rows in the order they were written ({@link Order#ASC}, the default) or
         *              newest first
         * @return this builder
         */
        public Builder order(Order order) {
            this.order = order;
            return this;
        }

        /**
         * @param limit rows per page: 1 to 1000, default 100
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param cursor the {@code nextCursor} of an earlier page, to start after it
         * @return this builder
         */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /** @return the parameters */
        public FormulaRowListParams build() {
            return new FormulaRowListParams(this);
        }
    }
}
