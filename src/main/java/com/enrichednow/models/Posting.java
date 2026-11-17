package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;

/** A job posting of the dataset, as the lists return it. {@link PostingDetail} adds the description and history. */
public class Posting {
    private final String id;
    private final String title;
    private final String titleClean;
    private final CompanyRef company;
    private final Location location;
    private final String employmentType;
    private final String seniority;
    private final String function;
    private final Salary salary;
    private final String url;
    private final String applyUrl;
    private final Source source;
    private final String status;
    private final LocalDate postedAt;
    private final LocalDate validThrough;
    private final Instant firstSeenAt;
    private final Instant lastSeenAt;
    private final Instant closedAt;

    @JsonCreator
    Posting(@JsonProperty("id") String id,
            @JsonProperty("title") String title,
            @JsonProperty("title_clean") String titleClean,
            @JsonProperty("company") CompanyRef company,
            @JsonProperty("location") Location location,
            @JsonProperty("employment_type") String employmentType,
            @JsonProperty("seniority") String seniority,
            @JsonProperty("function") String function,
            @JsonProperty("salary") Salary salary,
            @JsonProperty("url") String url,
            @JsonProperty("apply_url") String applyUrl,
            @JsonProperty("source") Source source,
            @JsonProperty("status") String status,
            @JsonProperty("posted_at") String postedAt,
            @JsonProperty("valid_through") String validThrough,
            @JsonProperty("first_seen_at") String firstSeenAt,
            @JsonProperty("last_seen_at") String lastSeenAt,
            @JsonProperty("closed_at") String closedAt) {
        this.id = id;
        this.title = title;
        this.titleClean = titleClean;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.seniority = seniority;
        this.function = function;
        this.salary = salary;
        this.url = url;
        this.applyUrl = applyUrl;
        this.source = source;
        this.status = status;
        this.postedAt = Values.date(postedAt);
        this.validThrough = Values.date(validThrough);
        this.firstSeenAt = Values.instant(firstSeenAt);
        this.lastSeenAt = Values.instant(lastSeenAt);
        this.closedAt = Values.instant(closedAt);
    }

    /** @return the posting's id */
    public String id() {
        return id;
    }

    /** @return the title as the source wrote it */
    public String title() {
        return title;
    }

    /** @return the title, cleaned */
    public String titleClean() {
        return titleClean;
    }

    /** @return the company the posting was matched to; its fields are {@code null} for an unmatched posting */
    public CompanyRef company() {
        return company;
    }

    /** @return where the job is */
    public Location location() {
        return location;
    }

    /**
     * @return {@code full_time}, {@code part_time}, {@code contract}, {@code temporary}, {@code internship} or
     *     {@code volunteer}, or {@code null}
     */
    public String employmentType() {
        return employmentType;
    }

    /**
     * @return {@code c_level}, {@code vp}, {@code director}, {@code manager}, {@code lead}, {@code senior},
     *     {@code junior} or {@code intern}, or {@code null}
     */
    public String seniority() {
        return seniority;
    }

    /** @return the job function, e.g. {@code engineering}, {@code sales}, {@code data}, or {@code null} */
    public String function() {
        return function;
    }

    /** @return the salary, or {@code null} when the posting names none */
    public Salary salary() {
        return salary;
    }

    /** @return the address of the posting */
    public String url() {
        return url;
    }

    /** @return the address to apply at, or {@code null} */
    public String applyUrl() {
        return applyUrl;
    }

    /** @return where the posting was read from */
    public Source source() {
        return source;
    }

    /** @return {@code open} or {@code closed} */
    public String status() {
        return status;
    }

    /** @return whether the posting is open */
    public boolean isOpen() {
        return "open".equals(status);
    }

    /** @return the day the source says the job was posted, or {@code null} */
    public LocalDate postedAt() {
        return postedAt;
    }

    /** @return the day the source says the posting is valid through, or {@code null} */
    public LocalDate validThrough() {
        return validThrough;
    }

    /** @return when the posting was first seen */
    public Instant firstSeenAt() {
        return firstSeenAt;
    }

    /** @return when the posting was last seen */
    public Instant lastSeenAt() {
        return lastSeenAt;
    }

    /** @return when the posting closed, or {@code null} while it is open */
    public Instant closedAt() {
        return closedAt;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", title=" + title + ", company="
                + (company == null ? null : company.domain()) + ", status=" + status + '}';
    }

    /** The company a posting belongs to. */
    public static final class CompanyRef {
        private final String domain;
        private final String name;

        @JsonCreator
        CompanyRef(@JsonProperty("domain") String domain, @JsonProperty("name") String name) {
            this.domain = domain;
            this.name = name;
        }

        /** @return the company's domain, or {@code null} for a posting not matched to a company */
        public String domain() {
            return domain;
        }

        /** @return the company's name, or {@code null} */
        public String name() {
            return name;
        }
    }

    /** Where a job is. */
    public static final class Location {
        private final String raw;
        private final String city;
        private final String region;
        private final String country;
        private final String countryCode;
        private final Boolean remote;

        @JsonCreator
        Location(@JsonProperty("raw") String raw,
                 @JsonProperty("city") String city,
                 @JsonProperty("region") String region,
                 @JsonProperty("country") String country,
                 @JsonProperty("country_code") String countryCode,
                 @JsonProperty("remote") Boolean remote) {
            this.raw = raw;
            this.city = city;
            this.region = region;
            this.country = country;
            this.countryCode = countryCode;
            this.remote = remote;
        }

        /** @return the location as the source wrote it, or {@code null} */
        public String raw() {
            return raw;
        }

        /** @return the city, or {@code null} */
        public String city() {
            return city;
        }

        /** @return the region, or {@code null} */
        public String region() {
            return region;
        }

        /** @return the country's name, or {@code null} */
        public String country() {
            return country;
        }

        /** @return the country's ISO 3166-1 alpha-2 code, or {@code null} */
        public String countryCode() {
            return countryCode;
        }

        /** @return whether the posting is marked remote, or {@code null} when it does not say */
        public Boolean remote() {
            return remote;
        }
    }

    /** What a job pays. */
    public static final class Salary {
        private final String raw;
        private final Double min;
        private final Double max;
        private final String currency;
        private final String period;

        @JsonCreator
        Salary(@JsonProperty("raw") String raw,
               @JsonProperty("min") Double min,
               @JsonProperty("max") Double max,
               @JsonProperty("currency") String currency,
               @JsonProperty("period") String period) {
            this.raw = raw;
            this.min = min;
            this.max = max;
            this.currency = currency;
            this.period = period;
        }

        /** @return the salary as the source wrote it, or {@code null} */
        public String raw() {
            return raw;
        }

        /** @return the lower end, or {@code null} */
        public Double min() {
            return min;
        }

        /** @return the upper end, or {@code null} */
        public Double max() {
            return max;
        }

        /** @return the currency, or {@code null} */
        public String currency() {
            return currency;
        }

        /** @return the period the amounts are for, or {@code null} */
        public String period() {
            return period;
        }
    }

    /** Where a posting was read from. */
    public static final class Source {
        private final String name;
        private final String provenance;
        private final boolean trusted;
        private final String externalId;

        @JsonCreator
        Source(@JsonProperty("name") String name,
               @JsonProperty("provenance") String provenance,
               @JsonProperty("trusted") boolean trusted,
               @JsonProperty("external_id") String externalId) {
            this.name = name;
            this.provenance = provenance;
            this.trusted = trusted;
            this.externalId = externalId;
        }

        /** @return the source's name: an applicant tracking system, a job board, or {@code careers_page} */
        public String name() {
            return name;
        }

        /** @return {@code feed}, {@code page} or {@code board} */
        public String provenance() {
            return provenance;
        }

        /** @return whether the posting is a trusted one */
        public boolean trusted() {
            return trusted;
        }

        /** @return the source's own id of the posting, or {@code null} */
        public String externalId() {
            return externalId;
        }
    }
}
