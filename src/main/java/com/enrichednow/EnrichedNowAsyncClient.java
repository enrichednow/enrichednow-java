package com.enrichednow;

import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.models.Company;
import com.enrichednow.models.Health;
import com.enrichednow.models.Posting;
import com.enrichednow.models.PostingDetail;
import com.enrichednow.models.Run;
import com.enrichednow.models.Scraper;
import com.enrichednow.models.ScraperDetail;
import com.enrichednow.models.Usage;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * The calls of {@link EnrichedNowClient}, each returning a {@link CompletableFuture} instead of blocking. Reached
 * through {@link EnrichedNowClient#async()}; it shares the client's settings, retries included.
 *
 * <pre>{@code
 * client.async().scrape("ats.board", Map.of("board_url", "https://boards.greenhouse.io/acme"))
 *       .thenAccept(result -> System.out.println(result.run().recordCount()));
 * }</pre>
 *
 * <p>A call that fails completes its future exceptionally with the exception the blocking call would have thrown;
 * {@code join()} wraps it in a {@link java.util.concurrent.CompletionException}. Lists come a page at a time: ask a
 * page for the next with {@link Page#nextPageAsync()}.
 */
public final class EnrichedNowAsyncClient {
    private final Transport transport;
    private final Requests requests;
    private final RunService blockingRuns;
    private final Scrapers scrapers = new Scrapers();
    private final Runs runs = new Runs();
    private final UsageReports usage = new UsageReports();
    private final Data data = new Data();

    EnrichedNowAsyncClient(Transport transport, Requests requests, RunService blockingRuns) {
        this.transport = transport;
        this.requests = requests;
        this.blockingRuns = blockingRuns;
    }

    Requests requests() {
        return requests;
    }

    /** @return the catalogue of scrapers, and starting a run */
    public Scrapers scrapers() {
        return scrapers;
    }

    /** @return runs and their records */
    public Runs runs() {
        return runs;
    }

    /** @return units used */
    public UsageReports usage() {
        return usage;
    }

    /** @return Datasets: job postings and companies */
    public Data data() {
        return data;
    }

    /**
     * Whether the service is up: {@code GET /v1/health}.
     *
     * @return the service's answer, later
     */
    public CompletableFuture<Health> health() {
        return transport.executeAsync(requests.health());
    }

    /**
     * Runs a scraper and waits for the result, without blocking. See {@link EnrichedNowClient#scrape(String, Object)}.
     *
     * @param scraperId the scraper's id
     * @param input     the scraper's input
     * @return the run as it ended and its records, later
     */
    public CompletableFuture<ScrapeResult> scrape(String scraperId, Object input) {
        return scrape(scraperId, input, ScrapeOptions.defaults());
    }

    /**
     * Runs a scraper and waits for the result, without blocking. See
     * {@link EnrichedNowClient#scrape(String, Object, ScrapeOptions)}.
     *
     * <p>The records of the result are fetched when they are iterated, and iterating blocks; to stay asynchronous,
     * read them with {@link Runs#recordsPage(String, RecordListParams)}.
     *
     * @param scraperId the scraper's id
     * @param input     the scraper's input
     * @param options   how long to wait, and whether a run that did not succeed fails the future
     * @return the run as it ended and its records, later
     */
    public CompletableFuture<ScrapeResult> scrape(String scraperId, Object input, ScrapeOptions options) {
        ScrapeOptions scrape = options == null ? ScrapeOptions.defaults() : options;
        long deadline = System.nanoTime() + scrape.timeout().toNanos();
        return scrapers.run(scraperId, input, RunOptions.waitUpTo(EnrichedNowClient.firstWait(scrape)))
                .thenCompose(run -> awaitEnd(run, deadline, scrape.pollInterval()))
                .thenApply(run -> EnrichedNowClient.result(run, scrape, blockingRuns));
    }

    private CompletableFuture<Run> awaitEnd(Run run, long deadlineNanos, Duration pollInterval) {
        if (run.isFinished()) {
            return CompletableFuture.completedFuture(run);
        }
        long remaining = deadlineNanos - System.nanoTime();
        if (remaining <= 0) {
            return CompletableFuture.failedFuture(new RunTimeoutException(run));
        }
        long pause = Math.min(remaining, pollInterval.toNanos());
        return CompletableFuture.supplyAsync(() -> run.id(), CompletableFuture.delayedExecutor(pause, TimeUnit.NANOSECONDS))
                .thenCompose(id -> transport.executeAsync(requests.run(id)))
                .thenCompose(next -> awaitEnd(next, deadlineNanos, pollInterval));
    }

    /** The catalogue of scrapers, and starting a run, asynchronously. */
    public final class Scrapers {
        private Scrapers() {
        }

        /**
         * @return the scrapers the account can run, later
         * @see ScraperService#list()
         */
        public CompletableFuture<List<Scraper>> list() {
            return transport.executeAsync(requests.scrapers());
        }

        /**
         * @param scraperId the scraper's id
         * @return the scraper with its schemas, later
         * @see ScraperService#get(String)
         */
        public CompletableFuture<ScraperDetail> get(String scraperId) {
            return transport.executeAsync(requests.scraper(scraperId));
        }

        /**
         * @param scraperId the scraper's id
         * @param input     the scraper's input
         * @return the run, later
         * @see ScraperService#run(String, Object)
         */
        public CompletableFuture<Run> run(String scraperId, Object input) {
            return run(scraperId, input, RunOptions.none());
        }

        /**
         * @param scraperId the scraper's id
         * @param input     the scraper's input
         * @param options   how to start it
         * @return the run, later
         * @see ScraperService#run(String, Object, RunOptions)
         */
        public CompletableFuture<Run> run(String scraperId, Object input, RunOptions options) {
            return transport.executeAsync(requests.startRun(scraperId, input, options));
        }
    }

    /** Runs and their records, asynchronously. */
    public final class Runs {
        private Runs() {
        }

        /**
         * @param runId the run's id
         * @return the run as it is now, later
         * @see RunService#get(String)
         */
        public CompletableFuture<Run> get(String runId) {
            return transport.executeAsync(requests.run(runId));
        }

        /**
         * @param params the filter, the page size and, to continue an earlier list, the cursor
         * @return one page of the account's runs, later
         * @see RunService#listPage(RunListParams)
         */
        public CompletableFuture<Page<Run>> listPage(RunListParams params) {
            return transport.executeAsync(requests.runs(params, null));
        }

        /**
         * @param runId the run's id
         * @return the run as it is at that moment, later
         * @see RunService#cancel(String)
         */
        public CompletableFuture<Run> cancel(String runId) {
            return transport.executeAsync(requests.cancelRun(runId));
        }

        /**
         * @param runId the run's id
         * @return the run with a final status, later
         * @see RunService#waitFor(String)
         */
        public CompletableFuture<Run> waitFor(String runId) {
            return waitFor(runId, WaitOptions.defaults());
        }

        /**
         * @param runId   the run's id
         * @param options how long to wait and how often to ask
         * @return the run with a final status, later; the future fails with a {@link RunTimeoutException} when the run
         *     had not ended in time
         * @see RunService#waitFor(String, WaitOptions)
         */
        public CompletableFuture<Run> waitFor(String runId, WaitOptions options) {
            WaitOptions wait = options == null ? WaitOptions.defaults() : options;
            long deadline = System.nanoTime() + wait.timeout().toNanos();
            return get(runId).thenCompose(run -> awaitEnd(run, deadline, wait.pollInterval()));
        }

        /**
         * @param runId  the run's id
         * @param params the page size and, to continue an earlier list, the cursor
         * @return one page of the run's records, later
         * @see RunService#recordsPage(String, RecordListParams)
         */
        public CompletableFuture<RecordPage<Map<String, Object>>> recordsPage(String runId, RecordListParams params) {
            return transport.executeAsync(requests.<Map<String, Object>>records(runId, params, requests.mapType(), null));
        }

        /**
         * @param runId  the run's id
         * @param params the page size and, to continue an earlier list, the cursor
         * @param type   the class of one record
         * @param <T>    the class of one record
         * @return one page of the run's records, each read as your own class, later
         * @see RunService#recordsPage(String, RecordListParams, Class)
         */
        public <T> CompletableFuture<RecordPage<T>> recordsPage(String runId, RecordListParams params, Class<T> type) {
            return transport.executeAsync(requests.<T>records(runId, params, requests.type(type), null));
        }
    }

    /** Units used, asynchronously. */
    public final class UsageReports {
        private UsageReports() {
        }

        /**
         * @return this month's usage, later
         * @see UsageService#get()
         */
        public CompletableFuture<Usage> get() {
            return get(UsageParams.none());
        }

        /**
         * @param params the period, the grouping and the product
         * @return the report, later
         * @see UsageService#get(UsageParams)
         */
        public CompletableFuture<Usage> get(UsageParams params) {
            return transport.executeAsync(requests.usage(params));
        }
    }

    /** Datasets, asynchronously. */
    public final class Data {
        private final Postings postings = new Postings();
        private final Companies companies = new Companies();

        private Data() {
        }

        /** @return the job postings of the dataset */
        public Postings postings() {
            return postings;
        }

        /** @return the companies of the dataset */
        public Companies companies() {
            return companies;
        }
    }

    /** The job postings of the dataset, asynchronously. */
    public final class Postings {
        private Postings() {
        }

        /**
         * @param params the filters, the order, the page size and, to continue an earlier list, the cursor
         * @return one page of the postings that match, later
         * @see DataService.PostingService#listPage(PostingListParams)
         */
        public CompletableFuture<Page<Posting>> listPage(PostingListParams params) {
            return transport.executeAsync(requests.postings(params, null));
        }

        /**
         * @param postingId the posting's id
         * @return the posting with its description and history, later
         * @see DataService.PostingService#get(String)
         */
        public CompletableFuture<PostingDetail> get(String postingId) {
            return transport.executeAsync(requests.posting(postingId));
        }
    }

    /** The companies of the dataset, asynchronously. */
    public final class Companies {
        private Companies() {
        }

        /**
         * @param params the filters, the page size and, to continue an earlier list, the cursor
         * @return one page of the companies that match, later
         * @see DataService.CompanyService#listPage(CompanyListParams)
         */
        public CompletableFuture<Page<Company>> listPage(CompanyListParams params) {
            return transport.executeAsync(requests.companies(params, null));
        }

        /**
         * @param domain the company's domain
         * @return the company, later
         * @see DataService.CompanyService#get(String)
         */
        public CompletableFuture<Company> get(String domain) {
            return transport.executeAsync(requests.company(domain));
        }

        /**
         * @param domain the company's domain
         * @param params the filters, the page size and, to continue an earlier list, the cursor
         * @return one page of the company's postings, later
         * @see DataService.CompanyService#postingsPage(String, PostingListParams)
         */
        public CompletableFuture<Page<Posting>> postingsPage(String domain, PostingListParams params) {
            return transport.executeAsync(requests.companyPostings(domain, params, null));
        }
    }
}
