package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * A formula of the catalogue, as {@code GET /v1/formulas} lists it: a ready-made run that turns a few parameters into a
 * finished list of rows. {@link FormulaDetails} adds its parameters, columns and samples.
 */
public class Formula {
    private final String id;
    private final String version;
    private final String title;
    private final String summary;
    private final String useWhen;
    private final String category;
    private final List<String> personas;
    private final String status;
    private final boolean available;
    private final boolean needsProvider;
    private final String recommendedCadence;
    private final List<String> schedules;
    private final boolean needsAttestation;
    private final FormulaHealth health;
    private final List<FormulaInput> basics;

    @JsonCreator
    Formula(@JsonProperty("id") String id,
            @JsonProperty("version") String version,
            @JsonProperty("title") String title,
            @JsonProperty("summary") String summary,
            @JsonProperty("use_when") String useWhen,
            @JsonProperty("category") String category,
            @JsonProperty("personas") List<String> personas,
            @JsonProperty("status") String status,
            @JsonProperty("available") boolean available,
            @JsonProperty("needs_provider") boolean needsProvider,
            @JsonProperty("recommended_cadence") String recommendedCadence,
            @JsonProperty("schedules") List<String> schedules,
            @JsonProperty("needs_attestation") boolean needsAttestation,
            @JsonProperty("health") FormulaHealth health,
            @JsonProperty("basics") List<FormulaInput> basics) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.summary = summary;
        this.useWhen = useWhen;
        this.category = category;
        this.personas = Values.list(personas);
        this.status = status;
        this.available = available;
        this.needsProvider = needsProvider;
        this.recommendedCadence = recommendedCadence;
        this.schedules = Values.list(schedules);
        this.needsAttestation = needsAttestation;
        this.health = health;
        this.basics = Values.list(basics);
    }

    /** @return the formula's id, e.g. {@code job-order-hunter}: what a run is started with */
    public String id() {
        return id;
    }

    /** @return its version, e.g. {@code 1.1.0} */
    public String version() {
        return version;
    }

    /** @return its name */
    public String title() {
        return title;
    }

    /** @return what it returns, in a sentence */
    public String summary() {
        return summary;
    }

    /** @return when it is the one to run */
    public String useWhen() {
        return useWhen;
    }

    /** @return its category, e.g. {@code recruiting} */
    public String category() {
        return category;
    }

    /** @return who it is for, e.g. {@code recruiter} */
    public List<String> personas() {
        return personas;
    }

    /** @return {@code draft}, {@code beta}, {@code live} or {@code deprecated} */
    public String status() {
        return status;
    }

    /** @return whether this account can run it now */
    public boolean available() {
        return available;
    }

    /** @return whether its contact columns stay empty until a provider is connected; the rest of each row is filled */
    public boolean needsProvider() {
        return needsProvider;
    }

    /** @return how often a saved formula of it is best run, e.g. {@code weekdays} */
    public String recommendedCadence() {
        return recommendedCadence;
    }

    /** @return the cadences a saved formula of it may have, e.g. {@code daily}, {@code weekly} */
    public List<String> schedules() {
        return schedules;
    }

    /**
     * @return whether it returns people, so that an owner or an admin of the account must first accept the terms, in the
     *     console ({@code client.attestations().list()} says whether they have)
     */
    public boolean needsAttestation() {
        return needsAttestation;
    }

    /** @return how it has been running, as last measured; null before any run */
    public FormulaHealth health() {
        return health;
    }

    /** @return the questions it asks before it runs, the required ones first: everything else has a default */
    public List<FormulaInput> basics() {
        return basics;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", version=" + version + ", title=" + title + ", status=" + status
                + ", available=" + available + '}';
    }
}
