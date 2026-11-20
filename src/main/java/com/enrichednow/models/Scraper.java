package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** A scraper of the catalogue, as {@code GET /v1/scrapers} lists it. */
public class Scraper {
    private final String id;
    private final int version;
    private final String title;
    private final String summary;
    private final String mode;
    private final String billingUnit;
    private final Integer unitsPerRequest;
    private final Integer unitsPerRecord;
    private final String dataClass;
    private final String availability;

    @JsonCreator
    Scraper(@JsonProperty("id") String id,
            @JsonProperty("version") int version,
            @JsonProperty("title") String title,
            @JsonProperty("summary") String summary,
            @JsonProperty("mode") String mode,
            @JsonProperty("billing_unit") String billingUnit,
            @JsonProperty("units_per_request") Integer unitsPerRequest,
            @JsonProperty("units_per_record") Integer unitsPerRecord,
            @JsonProperty("data_class") String dataClass,
            @JsonProperty("availability") String availability) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.summary = summary;
        this.mode = mode;
        this.billingUnit = billingUnit;
        this.unitsPerRequest = unitsPerRequest;
        this.unitsPerRecord = unitsPerRecord;
        this.dataClass = dataClass;
        this.availability = availability;
    }

    /** @return the scraper's id, e.g. {@code ats.board}; what a run is started with */
    public String id() {
        return id;
    }

    /** @return the scraper's version, a whole number that goes up when the scraper changes */
    public int version() {
        return version;
    }

    /** @return the scraper's name */
    public String title() {
        return title;
    }

    /** @return what the scraper does, in a sentence or two */
    public String summary() {
        return summary;
    }

    /** @return {@code sync} or {@code job} */
    public String mode() {
        return mode;
    }

    /** @return how a charged run is counted: by the {@code request} (the run) or by the {@code record} */
    public String billingUnit() {
        return billingUnit;
    }

    /**
     * @return for a scraper billed per request, the units a run that ends {@code ok} or {@code partial} costs;
     *     null for one billed per record
     */
    public Integer unitsPerRequest() {
        return unitsPerRequest;
    }

    /** @return for a scraper billed per record, the units each record of a charged run costs; null otherwise */
    public Integer unitsPerRecord() {
        return unitsPerRecord;
    }

    /** @return what the scraper costs, a run or a record by its billing unit: 1 when the API did not say */
    public int unitCost() {
        Integer cost = "record".equals(billingUnit) ? unitsPerRecord : unitsPerRequest;
        return cost == null ? 1 : cost;
    }

    /** @return the class of data the scraper returns */
    public String dataClass() {
        return dataClass;
    }

    /** @return {@code public} or {@code internal} */
    public String availability() {
        return availability;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", version=" + version + ", title=" + title + ", billingUnit=" + billingUnit + '}';
    }
}
