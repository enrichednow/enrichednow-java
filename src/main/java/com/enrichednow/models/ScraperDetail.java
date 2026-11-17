package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/** One scraper with the JSON Schema of its input and of its records, as {@code GET /v1/scrapers/{id}} returns it. */
public final class ScraperDetail extends Scraper {
    private final Map<String, Object> inputSchema;
    private final Map<String, Object> recordSchema;

    @JsonCreator
    ScraperDetail(@JsonProperty("id") String id,
                  @JsonProperty("version") int version,
                  @JsonProperty("title") String title,
                  @JsonProperty("summary") String summary,
                  @JsonProperty("mode") String mode,
                  @JsonProperty("billing_unit") String billingUnit,
                  @JsonProperty("data_class") String dataClass,
                  @JsonProperty("availability") String availability,
                  @JsonProperty("input_schema") Map<String, Object> inputSchema,
                  @JsonProperty("record_schema") Map<String, Object> recordSchema) {
        super(id, version, title, summary, mode, billingUnit, dataClass, availability);
        this.inputSchema = Values.map(inputSchema);
        this.recordSchema = Values.map(recordSchema);
    }

    /** @return the JSON Schema of a run's {@code input} */
    public Map<String, Object> inputSchema() {
        return inputSchema;
    }

    /** @return the JSON Schema of one record */
    public Map<String, Object> recordSchema() {
        return recordSchema;
    }
}
