package com.enrichednow;

import com.enrichednow.models.Run;
import java.util.Map;

/** What {@link EnrichedNowClient#scrape(String, Object)} returns: the run as it ended, and its records. */
public final class ScrapeResult {
    private final Run run;
    private final RunService runs;

    ScrapeResult(Run run, RunService runs) {
        this.run = run;
        this.runs = runs;
    }

    /** @return the run, with its final status, reason, record count and units */
    public Run run() {
        return run;
    }

    /**
     * The run's records, each a {@code Map} of the JSON object the scraper returns. They are fetched a page at a time
     * as they are iterated, not when the result is made.
     *
     * @return the records
     */
    public PagedIterable<Map<String, Object>> records() {
        return runs.records(run.id());
    }

    /**
     * The run's records, each read as your own class.
     *
     * @param type the class of one record
     * @param <T>  the class of one record
     * @return the records
     */
    public <T> PagedIterable<T> records(Class<T> type) {
        return runs.records(run.id(), type);
    }

    @Override
    public String toString() {
        return "ScrapeResult{run=" + run + '}';
    }
}
