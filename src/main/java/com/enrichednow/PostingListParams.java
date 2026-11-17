package com.enrichednow;

import com.enrichednow.ApiRequest.Query;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Which postings of the dataset to return. Every filter given must hold. Without any, a list returns the postings
 * read from applicant tracking systems and matched to a company, open and closed, newest first.
 */
public final class PostingListParams {
    private static final PostingListParams NONE = builder().build();

    final String companyDomain;
    private final String title;
    private final String location;
    private final String country;
    private final Boolean remote;
    private final String employmentType;
    private final String seniority;
    private final String function;
    private final String status;
    private final String source;
    private final String provenance;
    private final Boolean unmatched;
    private final LocalDate postedFrom;
    private final LocalDate postedTo;
    private final Instant firstSeenFrom;
    private final Instant firstSeenTo;
    private final Instant lastSeenFrom;
    private final Instant lastSeenTo;
    private final String sort;
    private final Integer limit;
    private final String cursor;

    private PostingListParams(Builder builder) {
        this.companyDomain = builder.companyDomain;
        this.title = builder.title;
        this.location = builder.location;
        this.country = builder.country;
        this.remote = builder.remote;
        this.employmentType = builder.employmentType;
        this.seniority = builder.seniority;
        this.function = builder.function;
        this.status = builder.status;
        this.source = builder.source;
        this.provenance = builder.provenance;
        this.unmatched = builder.unmatched;
        this.postedFrom = builder.postedFrom;
        this.postedTo = builder.postedTo;
        this.firstSeenFrom = builder.firstSeenFrom;
        this.firstSeenTo = builder.firstSeenTo;
        this.lastSeenFrom = builder.lastSeenFrom;
        this.lastSeenTo = builder.lastSeenTo;
        this.sort = builder.sort;
        this.limit = builder.limit;
        this.cursor = builder.cursor;
    }

    /** @return no filter */
    public static PostingListParams none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** The query string; {@code cursor}, when given, replaces the one of the parameters. */
    Query query(boolean withCompanyDomain, String cursor) {
        return new Query()
                .add("company_domain", withCompanyDomain ? companyDomain : null)
                .add("title", title)
                .add("location", location)
                .add("country", country)
                .add("remote", remote)
                .add("employment_type", employmentType)
                .add("seniority", seniority)
                .add("function", function)
                .add("status", status)
                .add("source", source)
                .add("provenance", provenance)
                .add("unmatched", unmatched)
                .add("posted_from", postedFrom)
                .add("posted_to", postedTo)
                .add("first_seen_from", firstSeenFrom)
                .add("first_seen_to", firstSeenTo)
                .add("last_seen_from", lastSeenFrom)
                .add("last_seen_to", lastSeenTo)
                .add("sort", sort)
                .add("limit", limit)
                .add("cursor", cursor != null ? cursor : this.cursor);
    }

    /** Builds {@link PostingListParams}. */
    public static final class Builder {
        private String companyDomain;
        private String title;
        private String location;
        private String country;
        private Boolean remote;
        private String employmentType;
        private String seniority;
        private String function;
        private String status;
        private String source;
        private String provenance;
        private Boolean unmatched;
        private LocalDate postedFrom;
        private LocalDate postedTo;
        private Instant firstSeenFrom;
        private Instant firstSeenTo;
        private Instant lastSeenFrom;
        private Instant lastSeenTo;
        private String sort;
        private Integer limit;
        private String cursor;

        private Builder() {
        }

        /**
         * @param companyDomain the company's domain; any host or address of it will do. Not a parameter of
         *                      {@code client.data().companies().postings(domain, ...)}, which names the company itself
         * @return this builder
         */
        public Builder companyDomain(String companyDomain) {
            this.companyDomain = companyDomain;
            return this;
        }

        /**
         * @param title words that must all appear in the cleaned title, in any order and any case
         * @return this builder
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * @param location text that must appear in the location: the raw text, the city, the region or the country
         * @return this builder
         */
        public Builder location(String location) {
            this.location = location;
            return this;
        }

        /**
         * @param country a country code (ISO 3166-1 alpha-2) or name
         * @return this builder
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * @param remote {@code true}: marked remote; {@code false}: marked not remote. A posting that does not say is in
         *               neither
         * @return this builder
         */
        public Builder remote(boolean remote) {
            this.remote = remote;
            return this;
        }

        /**
         * @param employmentType {@code full_time}, {@code part_time}, {@code contract}, {@code temporary},
         *                       {@code internship} or {@code volunteer}
         * @return this builder
         */
        public Builder employmentType(String employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        /**
         * @param seniority {@code c_level}, {@code vp}, {@code director}, {@code manager}, {@code lead}, {@code senior},
         *                  {@code junior} or {@code intern}
         * @return this builder
         */
        public Builder seniority(String seniority) {
            this.seniority = seniority;
            return this;
        }

        /**
         * @param function {@code data}, {@code sales}, {@code customer_support}, {@code product}, {@code design},
         *                 {@code marketing}, {@code finance}, {@code legal}, {@code people}, {@code it},
         *                 {@code healthcare}, {@code education}, {@code operations}, {@code engineering},
         *                 {@code skilled_trades} or {@code retail_hospitality}
         * @return this builder
         */
        public Builder function(String function) {
            this.function = function;
            return this;
        }

        /**
         * @param status {@code open}, {@code closed} or {@code all} (the default)
         * @return this builder
         */
        public Builder status(String status) {
            this.status = status;
            return this;
        }

        /**
         * @param source the source's name: an applicant tracking system ({@code greenhouse}), a job board, or
         *               {@code careers_page}
         * @return this builder
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * @param provenance which provenances to return, comma-separated: {@code feed}, {@code page}, {@code board}; or
         *                   {@code all}. Only {@code feed} is returned unless asked
         * @return this builder
         */
        public Builder provenance(String provenance) {
            this.provenance = provenance;
            return this;
        }

        /**
         * @param unmatched {@code true} to also return postings not matched to a company
         * @return this builder
         */
        public Builder unmatched(boolean unmatched) {
            this.unmatched = unmatched;
            return this;
        }

        /**
         * @param postedFrom posted on or after this date
         * @return this builder
         */
        public Builder postedFrom(LocalDate postedFrom) {
            this.postedFrom = postedFrom;
            return this;
        }

        /**
         * @param postedTo posted on or before this date
         * @return this builder
         */
        public Builder postedTo(LocalDate postedTo) {
            this.postedTo = postedTo;
            return this;
        }

        /**
         * @param firstSeenFrom first seen at or after this time
         * @return this builder
         */
        public Builder firstSeenFrom(Instant firstSeenFrom) {
            this.firstSeenFrom = firstSeenFrom;
            return this;
        }

        /**
         * @param firstSeenTo first seen before this time
         * @return this builder
         */
        public Builder firstSeenTo(Instant firstSeenTo) {
            this.firstSeenTo = firstSeenTo;
            return this;
        }

        /**
         * @param lastSeenFrom last seen at or after this time
         * @return this builder
         */
        public Builder lastSeenFrom(Instant lastSeenFrom) {
            this.lastSeenFrom = lastSeenFrom;
            return this;
        }

        /**
         * @param lastSeenTo last seen before this time
         * @return this builder
         */
        public Builder lastSeenTo(Instant lastSeenTo) {
            this.lastSeenTo = lastSeenTo;
            return this;
        }

        /**
         * @param sort {@code first_seen_at} (the default) or {@code last_seen_at}; either way the newest comes first
         * @return this builder
         */
        public Builder sort(String sort) {
            this.sort = sort;
            return this;
        }

        /**
         * @param limit postings per page: 1 to 200, default 50
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param cursor the {@code nextCursor} of an earlier page of the same list, to start after it
         * @return this builder
         */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /** @return the parameters */
        public PostingListParams build() {
            return new PostingListParams(this);
        }
    }
}
