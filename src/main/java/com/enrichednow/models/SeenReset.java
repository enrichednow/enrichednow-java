package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** What forgetting a saved formula's delivered rows did. */
public final class SeenReset {
    private final long cleared;
    private final SavedFormula savedFormula;

    @JsonCreator
    SeenReset(@JsonProperty("cleared") long cleared, @JsonProperty("saved_formula") SavedFormula savedFormula) {
        this.cleared = cleared;
        this.savedFormula = savedFormula;
    }

    /** @return how many delivered rows were forgotten: its next run may deliver them again */
    public long cleared() {
        return cleared;
    }

    /** @return the saved formula as it is now */
    public SavedFormula savedFormula() {
        return savedFormula;
    }

    @Override
    public String toString() {
        return "SeenReset{cleared=" + cleared + ", savedFormula=" + (savedFormula == null ? null : savedFormula.id()) + '}';
    }
}
