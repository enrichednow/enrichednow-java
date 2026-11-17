package com.enrichednow;

import com.enrichednow.models.Run;
import com.enrichednow.models.Scraper;
import com.enrichednow.models.ScraperDetail;
import java.util.List;

/** The catalogue of scrapers, and starting a run of one. Reached through {@link EnrichedNowClient#scrapers()}. */
public final class ScraperService {
    private final Transport transport;
    private final Requests requests;

    ScraperService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * The catalogue: {@code GET /v1/scrapers}.
     *
     * @return the scrapers the account can run
     */
    public List<Scraper> list() {
        return transport.execute(requests.scrapers());
    }

    /**
     * One scraper, with the JSON Schema of its input and of its records: {@code GET /v1/scrapers/{id}}.
     *
     * @param scraperId the scraper's id, e.g. {@code ats.board}
     * @return the scraper
     * @throws com.enrichednow.errors.NotFoundException when there is no such scraper
     */
    public ScraperDetail get(String scraperId) {
        return transport.execute(requests.scraper(scraperId));
    }

    /**
     * Starts a run and returns as soon as it is queued: {@code POST /v1/scrapers/{id}/runs}.
     *
     * @param scraperId the scraper's id
     * @param input     the scraper's input: a {@code Map}, or any object that is written as the JSON object the scraper's
     *                  {@code inputSchema} describes
     * @return the run, usually {@code queued}
     * @see #run(String, Object, RunOptions)
     */
    public Run run(String scraperId, Object input) {
        return run(scraperId, input, RunOptions.none());
    }

    /**
     * Starts a run: {@code POST /v1/scrapers/{id}/runs}.
     *
     * <p>With {@link RunOptions#waitUpTo(java.time.Duration)} the call holds on for the run to end. A run that ended in
     * time comes back with a final status; otherwise it comes back {@code queued} or {@code running}, and
     * {@link RunService#waitFor(String)} picks it up. Either way, read the run's status: a run that could not read its
     * target is still a successful call.
     *
     * <p>Starting a run is not idempotent, so this call is not repeated after a failure in flight or a server error:
     * when it throws a {@link com.enrichednow.errors.ConnectionException} or a
     * {@link com.enrichednow.errors.ServerException}, look at {@link RunService#list()} before starting the run again.
     *
     * @param scraperId the scraper's id
     * @param input     the scraper's input
     * @param options   how to start it
     * @return the run
     * @throws com.enrichednow.errors.ValidationException    when the input does not fit the scraper's schema
     * @throws com.enrichednow.errors.QuotaExceededException when this month's allowance is used up
     * @throws com.enrichednow.errors.RateLimitException     when the account is at its limit of runs at the same time
     */
    public Run run(String scraperId, Object input, RunOptions options) {
        return transport.execute(requests.startRun(scraperId, input, options));
    }
}
