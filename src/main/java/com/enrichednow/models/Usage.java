package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

/** Units used over a period, as {@code GET /v1/usage} returns them. */
public final class Usage {
    private final Period period;
    private final long totalUnits;
    private final long allowanceUnits;
    private final List<Row> rows;
    private final boolean unmetered;

    @JsonCreator
    Usage(@JsonProperty("period") Period period,
          @JsonProperty("total_units") long totalUnits,
          @JsonProperty("allowance_units") long allowanceUnits,
          @JsonProperty("rows") List<Row> rows,
          @JsonProperty("unmetered") boolean unmetered) {
        this.period = period;
        this.totalUnits = totalUnits;
        this.allowanceUnits = allowanceUnits;
        this.rows = Values.list(rows);
        this.unmetered = unmetered;
    }

    /**
     * @return true for an account with no unit limit (the service's own): nothing it does is refused for units
     */
    public boolean unmetered() {
        return unmetered;
    }

    /** @return the days the report covers */
    public Period period() {
        return period;
    }

    /** @return the units of the rows: of both products, or of the one that was asked for */
    public long totalUnits() {
        return totalUnits;
    }

    /**
     * @return the account's units a calendar month (UTC): the scraper allowance, or the data allowance when the
     *     report was asked for the data product
     */
    public long allowanceUnits() {
        return allowanceUnits;
    }

    /** @return one row per day, scraper or product, as the report was grouped */
    public List<Row> rows() {
        return rows;
    }

    @Override
    public String toString() {
        return "Usage{period=" + period + ", totalUnits=" + totalUnits + ", allowanceUnits=" + allowanceUnits
                + ", rows=" + rows.size() + '}';
    }

    /** The days a usage report covers, both included, in UTC. */
    public static final class Period {
        private final LocalDate from;
        private final LocalDate to;

        @JsonCreator
        Period(@JsonProperty("from") String from, @JsonProperty("to") String to) {
            this.from = Values.date(from);
            this.to = Values.date(to);
        }

        /** @return the first day */
        public LocalDate from() {
            return from;
        }

        /** @return the last day */
        public LocalDate to() {
            return to;
        }

        @Override
        public String toString() {
            return from + ".." + to;
        }
    }

    /** One line of a usage report. */
    public static final class Row {
        private final String key;
        private final long units;
        private final long runs;

        @JsonCreator
        Row(@JsonProperty("key") String key, @JsonProperty("units") long units, @JsonProperty("runs") long runs) {
            this.key = key;
            this.units = units;
            this.runs = runs;
        }

        /**
         * @return a day ({@code YYYY-MM-DD}, UTC), a scraper id, or a product ({@code scrapers}, {@code data}). By
         *     scraper, the data API's usage is under {@code data.<endpoint>}, e.g. {@code data.postings.list}
         */
        public String key() {
            return key;
        }

        /** @return the units charged */
        public long units() {
            return units;
        }

        /**
         * @return what was charged: runs that ended {@code ok} or {@code partial} and, for the data API, requests
         *     that returned at least one record
         */
        public long runs() {
            return runs;
        }

        @Override
        public String toString() {
            return "Row{key=" + key + ", units=" + units + ", runs=" + runs + '}';
        }
    }
}
