package com.enrichednow;

import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.formulas.FormulaParams;
import com.enrichednow.models.Attestation;
import com.enrichednow.models.Company;
import com.enrichednow.models.Formula;
import com.enrichednow.models.FormulaDetails;
import com.enrichednow.models.FormulaEstimate;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.Health;
import com.enrichednow.models.PlanLimits;
import com.enrichednow.models.Posting;
import com.enrichednow.models.PostingDetail;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import com.enrichednow.models.RowFeedback;
import com.enrichednow.models.Run;
import com.enrichednow.models.SavedFormula;
import com.enrichednow.models.Scraper;
import com.enrichednow.models.ScraperDetail;
import com.enrichednow.models.SeenReset;
import com.enrichednow.models.Snapshot;
import com.enrichednow.models.Standing;
import com.enrichednow.models.Suppression;
import com.enrichednow.models.SuppressionUpload;
import com.enrichednow.models.Usage;
import com.enrichednow.models.WebhookDelivery;
import com.enrichednow.models.WebhookEndpoint;
import com.enrichednow.models.WebhookEndpointList;
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
    private final FormulaRunService blockingFormulaRuns;
    private final Scrapers scrapers = new Scrapers();
    private final Runs runs = new Runs();
    private final UsageReports usage = new UsageReports();
    private final Data data = new Data();
    private final WebhookCalls webhooks = new WebhookCalls();
    private final Formulas formulas = new Formulas();
    private final FormulaRuns formulaRuns = new FormulaRuns();
    private final SavedFormulas savedFormulas = new SavedFormulas();
    private final Suppressions suppressions = new Suppressions();
    private final Attestations attestations = new Attestations();
    private final FormulaRows formulaRows = new FormulaRows();

    EnrichedNowAsyncClient(Transport transport, Requests requests, RunService blockingRuns, FormulaRunService blockingFormulaRuns) {
        this.transport = transport;
        this.requests = requests;
        this.blockingRuns = blockingRuns;
        this.blockingFormulaRuns = blockingFormulaRuns;
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

    /** @return webhooks: the endpoints, their secrets and the delivery log */
    public WebhookCalls webhooks() {
        return webhooks;
    }

    /** @return the catalogue of formulas, what a run would cost, and starting one */
    public Formulas formulas() {
        return formulas;
    }

    /** @return formula runs and their rows */
    public FormulaRuns formulaRuns() {
        return formulaRuns;
    }

    /** @return saved formulas */
    public SavedFormulas savedFormulas() {
        return savedFormulas;
    }

    /** @return the suppression list */
    public Suppressions suppressions() {
        return suppressions;
    }

    /** @return the terms some formulas need accepted */
    public Attestations attestations() {
        return attestations;
    }

    /** @return saying what is right or wrong with a formula's row */
    public FormulaRows formulaRows() {
        return formulaRows;
    }

    /**
     * Runs a formula and waits for the result, without blocking. See {@link EnrichedNowClient#runFormula(FormulaParams)}.
     *
     * @param params the parameters of one formula; the formula is theirs
     * @return the run as it ended and its rows, later
     */
    public CompletableFuture<FormulaResult> runFormula(FormulaParams params) {
        return runFormula(params, FormulaRunOptions.none());
    }

    /**
     * Runs a formula and waits for the result, without blocking.
     *
     * @param params  the parameters of one formula; the formula is theirs
     * @param options a preview, the caps, the idempotency key; how long to wait, and whether a run that failed fails
     *                the future
     * @return the run as it ended and its rows, later
     */
    public CompletableFuture<FormulaResult> runFormula(FormulaParams params, FormulaRunOptions options) {
        return runFormula(FormulaService.formulaOf(params), params, options);
    }

    /**
     * Runs a formula and waits for the result, without blocking.
     *
     * @param formulaId the formula's id
     * @param params    its parameters
     * @return the run as it ended and its rows, later
     */
    public CompletableFuture<FormulaResult> runFormula(String formulaId, Object params) {
        return runFormula(formulaId, params, FormulaRunOptions.none());
    }

    /**
     * Runs a formula and waits for the result, without blocking. See
     * {@link EnrichedNowClient#runFormula(String, Object, FormulaRunOptions)}.
     *
     * <p>The rows of the result are fetched when they are iterated, and iterating blocks; to stay asynchronous, read
     * them with {@link FormulaRuns#rowsPage(String, FormulaRowListParams)}.
     *
     * @param formulaId the formula's id
     * @param params    its parameters
     * @param options   a preview, the caps, the idempotency key; how long to wait, and whether a run that failed fails
     *                  the future (with a {@link com.enrichednow.errors.FormulaRunFailedException})
     * @return the run as it ended and its rows, later
     */
    public CompletableFuture<FormulaResult> runFormula(String formulaId, Object params, FormulaRunOptions options) {
        FormulaRunOptions run = options == null ? FormulaRunOptions.none() : options;
        long deadline = System.nanoTime() + run.timeout().toNanos();
        return formulas.run(formulaId, params, run.withWait(EnrichedNowClient.firstWait(run)))
                .thenCompose(started -> awaitFormulaEnd(started.id(), started, deadline, run.pollInterval()))
                .thenApply(ended -> EnrichedNowClient.formulaResult(ended, run, blockingFormulaRuns));
    }

    private CompletableFuture<FormulaRun> awaitFormulaEnd(String runId, FormulaRun known, long deadlineNanos, Duration pollInterval) {
        if (known != null && known.isFinished()) {
            return CompletableFuture.completedFuture(known);
        }
        long remaining = deadlineNanos - System.nanoTime();
        if (known != null && remaining <= 0) {
            return CompletableFuture.failedFuture(new RunTimeoutException(known));
        }
        long asked = System.nanoTime();
        return transport.executeAsync(requests.formulaRun(runId, FormulaRunService.lookWait(remaining))).thenCompose(run -> {
            if (run.isFinished()) {
                return CompletableFuture.completedFuture(run);
            }
            long left = deadlineNanos - System.nanoTime();
            long pause = Math.max(0, Math.min(pollInterval.toNanos() - (System.nanoTime() - asked), left));
            return CompletableFuture.supplyAsync(() -> run, CompletableFuture.delayedExecutor(pause, TimeUnit.NANOSECONDS))
                    .thenCompose(seen -> awaitFormulaEnd(runId, seen, deadlineNanos, pollInterval));
        });
    }

    /** The catalogue of formulas, what a run would cost, and starting one, asynchronously. */
    public final class Formulas {
        private Formulas() {
        }

        /**
         * @return the formulas listed for this account, later
         * @see FormulaService#list()
         */
        public CompletableFuture<List<Formula>> list() {
            return list(FormulaListParams.none());
        }

        /**
         * @param params words to search for, a category, a persona or a status
         * @return the formulas that match, later
         * @see FormulaService#list(FormulaListParams)
         */
        public CompletableFuture<List<Formula>> list(FormulaListParams params) {
            return transport.executeAsync(requests.formulas(params));
        }

        /**
         * @param formulaId the formula's id
         * @return the formula with its parameters, columns and samples, later
         * @see FormulaService#get(String)
         */
        public CompletableFuture<FormulaDetails> get(String formulaId) {
            return transport.executeAsync(requests.formula(formulaId));
        }

        /**
         * @param formulaId the formula's id
         * @param params    its parameters
         * @param options   the run to estimate: a preview, a unit cap, a row cap; or null
         * @return the estimate, later
         * @see FormulaService#estimate(String, Object, FormulaRunOptions)
         */
        public CompletableFuture<FormulaEstimate> estimate(String formulaId, Object params, FormulaRunOptions options) {
            return transport.executeAsync(requests.estimateFormula(formulaId, params, options));
        }

        /**
         * @param params  the parameters of one formula
         * @param options the run to estimate, or null
         * @return the estimate, later
         * @see FormulaService#estimate(FormulaParams, FormulaRunOptions)
         */
        public CompletableFuture<FormulaEstimate> estimate(FormulaParams params, FormulaRunOptions options) {
            return estimate(FormulaService.formulaOf(params), params, options);
        }

        /**
         * @param formulaId the formula's id
         * @param params    its parameters
         * @param options   a preview, the caps, the wait, the idempotency key; or null
         * @return the run, later
         * @see FormulaService#run(String, Object, FormulaRunOptions)
         */
        public CompletableFuture<FormulaRun> run(String formulaId, Object params, FormulaRunOptions options) {
            return transport.executeAsync(requests.startFormulaRun(formulaId, params, options));
        }

        /**
         * @param params  the parameters of one formula
         * @param options a preview, the caps, the wait, the idempotency key; or null
         * @return the run, later
         * @see FormulaService#run(FormulaParams, FormulaRunOptions)
         */
        public CompletableFuture<FormulaRun> run(FormulaParams params, FormulaRunOptions options) {
            return run(FormulaService.formulaOf(params), params, options);
        }
    }

    /** Formula runs and their rows, asynchronously. */
    public final class FormulaRuns {
        private FormulaRuns() {
        }

        /**
         * @param params the filter, the page size and, to continue an earlier list, the cursor
         * @return one page of the account's formula runs, later
         * @see FormulaRunService#listPage(FormulaRunListParams)
         */
        public CompletableFuture<Page<FormulaRun>> listPage(FormulaRunListParams params) {
            return transport.executeAsync(requests.formulaRuns(params, null));
        }

        /**
         * @param runId the run's id
         * @return the run as it is now, later
         * @see FormulaRunService#get(String)
         */
        public CompletableFuture<FormulaRun> get(String runId) {
            return transport.executeAsync(requests.formulaRun(runId));
        }

        /**
         * @param runId the run's id
         * @param wait  from 0 to 300 seconds
         * @return the run when it has ended or the wait is over, later
         * @see FormulaRunService#get(String, Duration)
         */
        public CompletableFuture<FormulaRun> get(String runId, Duration wait) {
            return transport.executeAsync(requests.formulaRun(runId, wait));
        }

        /**
         * @param runId the run's id
         * @return the run with a final status, later
         * @see FormulaRunService#waitFor(String)
         */
        public CompletableFuture<FormulaRun> waitFor(String runId) {
            return waitFor(runId, WaitOptions.defaults());
        }

        /**
         * @param runId   the run's id
         * @param options how long to wait, and the least time between two looks
         * @return the run with a final status, later; the future fails with a {@link RunTimeoutException} when the run
         *     had not ended in time
         * @see FormulaRunService#waitFor(String, WaitOptions)
         */
        public CompletableFuture<FormulaRun> waitFor(String runId, WaitOptions options) {
            WaitOptions wait = options == null ? WaitOptions.defaults() : options;
            return awaitFormulaEnd(runId, null, System.nanoTime() + wait.timeout().toNanos(), wait.pollInterval());
        }

        /**
         * @param runId  the run's id
         * @param params the columns, the least score, the view, the page size and the cursor
         * @return one page of the run's rows, later
         * @see FormulaRunService#rowsPage(String, FormulaRowListParams)
         */
        public CompletableFuture<Page<FormulaRow>> rowsPage(String runId, FormulaRowListParams params) {
            return transport.executeAsync(requests.formulaRows(runId, params, null));
        }

        /**
         * @param runId  the run's id
         * @param format CSV or NDJSON
         * @param params the columns, the least score, the view, the page size and the cursor
         * @return one page of the run's rows as the API wrote it, later
         * @see FormulaRunService#exportPage(String, RecordFormat, FormulaRowListParams)
         */
        public CompletableFuture<RecordsExport> exportPage(String runId, RecordFormat format, FormulaRowListParams params) {
            return transport.executeAsync(requests.exportFormulaRows(runId, format, params, null));
        }

        /**
         * @param runId the run's id
         * @return the run as it is at that moment, later
         * @see FormulaRunService#cancel(String)
         */
        public CompletableFuture<FormulaRun> cancel(String runId) {
            return transport.executeAsync(requests.cancelFormulaRun(runId));
        }
    }

    /** Saved formulas, asynchronously. */
    public final class SavedFormulas {
        private SavedFormulas() {
        }

        /**
         * @param create the formula, its parameters, and what else to set
         * @return the saved formula, later
         * @see SavedFormulaService#create(SavedFormulaCreate)
         */
        public CompletableFuture<SavedFormula> create(SavedFormulaCreate create) {
            return transport.executeAsync(requests.createSavedFormula(create));
        }

        /**
         * @param params the filter, the page size and, to continue an earlier list, the cursor
         * @return one page of the account's saved formulas, later
         * @see SavedFormulaService#listPage(SavedFormulaListParams)
         */
        public CompletableFuture<Page<SavedFormula>> listPage(SavedFormulaListParams params) {
            return transport.executeAsync(requests.savedFormulas(params, null));
        }

        /**
         * @param savedFormulaId its id
         * @return the saved formula, later
         * @see SavedFormulaService#get(String)
         */
        public CompletableFuture<SavedFormula> get(String savedFormulaId) {
            return transport.executeAsync(requests.savedFormula(savedFormulaId));
        }

        /**
         * @param savedFormulaId its id
         * @param change         what to change
         * @return the saved formula as it is now, later
         * @see SavedFormulaService#update(String, SavedFormulaChange)
         */
        public CompletableFuture<SavedFormula> update(String savedFormulaId, SavedFormulaChange change) {
            return transport.executeAsync(requests.updateSavedFormula(savedFormulaId, change));
        }

        /**
         * @param savedFormulaId its id
         * @return the saved formula, archived, later
         * @see SavedFormulaService#archive(String)
         */
        public CompletableFuture<SavedFormula> archive(String savedFormulaId) {
            return transport.executeAsync(requests.archiveSavedFormula(savedFormulaId));
        }

        /**
         * @param savedFormulaId the original's id
         * @param options        the copy's name, whether it starts with what the original delivered, its schedule; or null
         * @return the copy, later
         * @see SavedFormulaService#duplicate(String, DuplicateOptions)
         */
        public CompletableFuture<SavedFormula> duplicate(String savedFormulaId, DuplicateOptions options) {
            return transport.executeAsync(requests.duplicateSavedFormula(savedFormulaId, options));
        }

        /**
         * @param savedFormulaId its id
         * @param options        a preview, rows already delivered too, the wait, the idempotency key; or null
         * @return the run, later
         * @see SavedFormulaService#run(String, SavedFormulaRunOptions)
         */
        public CompletableFuture<FormulaRun> run(String savedFormulaId, SavedFormulaRunOptions options) {
            return transport.executeAsync(requests.startSavedFormulaRun(savedFormulaId, options));
        }

        /**
         * @param savedFormulaId its id
         * @return the saved formula as it is now, later
         * @see SavedFormulaService#pause(String)
         */
        public CompletableFuture<SavedFormula> pause(String savedFormulaId) {
            return transport.executeAsync(requests.pauseSavedFormula(savedFormulaId));
        }

        /**
         * @param savedFormulaId its id
         * @return the saved formula as it is now, later
         * @see SavedFormulaService#resume(String)
         */
        public CompletableFuture<SavedFormula> resume(String savedFormulaId) {
            return transport.executeAsync(requests.resumeSavedFormula(savedFormulaId));
        }

        /**
         * @param savedFormulaId its id
         * @return how many rows were forgotten, and the saved formula, later
         * @see SavedFormulaService#resetSeen(String)
         */
        public CompletableFuture<SeenReset> resetSeen(String savedFormulaId) {
            return transport.executeAsync(requests.resetSeen(savedFormulaId));
        }

        /**
         * @param savedFormulaId its id
         * @param params         since, the order, the columns, the least score, the page size and the cursor
         * @return one page of its rows, later
         * @see SavedFormulaService#rowsPage(String, FormulaRowListParams)
         */
        public CompletableFuture<Page<FormulaRow>> rowsPage(String savedFormulaId, FormulaRowListParams params) {
            return transport.executeAsync(requests.savedFormulaRows(savedFormulaId, params, null));
        }

        /**
         * @param savedFormulaId its id
         * @param format         CSV or NDJSON
         * @param params         since, the order, the columns, the least score, the page size and the cursor
         * @return one page of its rows as the API wrote it, later
         * @see SavedFormulaService#exportPage(String, RecordFormat, FormulaRowListParams)
         */
        public CompletableFuture<RecordsExport> exportPage(String savedFormulaId, RecordFormat format, FormulaRowListParams params) {
            return transport.executeAsync(requests.exportSavedFormulaRows(savedFormulaId, format, params, null));
        }

        /**
         * @param savedFormulaId its id
         * @param entityKey      only this entity; null for all
         * @param limit          how many, 1 to 1000; null for 200
         * @return the snapshots, newest first, later
         * @see SavedFormulaService#snapshots(String, String, Integer)
         */
        public CompletableFuture<List<Snapshot>> snapshots(String savedFormulaId, String entityKey, Integer limit) {
            return transport.executeAsync(requests.snapshots(savedFormulaId, entityKey, limit));
        }

        /**
         * @return what the plan allows of saved formulas, later
         * @see SavedFormulaService#limits()
         */
        public CompletableFuture<PlanLimits> limits() {
            return transport.executeAsync(requests.savedFormulaLimits());
        }
    }

    /** The suppression list, asynchronously. */
    public final class Suppressions {
        private Suppressions() {
        }

        /**
         * @param params the kind, the page size and, to continue an earlier list, the cursor
         * @return one page of the list, later
         * @see SuppressionService#listPage(SuppressionListParams)
         */
        public CompletableFuture<Page<Suppression>> listPage(SuppressionListParams params) {
            return transport.executeAsync(requests.suppressions(params, null));
        }

        /**
         * @param entries 1 to 10,000 entries
         * @param source  {@code manual} or {@code upload}; null for the default
         * @return how many were new to the list, later
         * @see SuppressionService#add(List, String)
         */
        public CompletableFuture<Integer> add(List<SuppressionEntry> entries, String source) {
            return transport.executeAsync(requests.addSuppressions(entries, source));
        }

        /**
         * @param csv  the file, UTF-8
         * @param kind what each value is; null to read each as it looks
         * @return what was added, what was there already, and the lines that could not be read, later
         * @see SuppressionService#upload(byte[], SuppressionKind)
         */
        public CompletableFuture<SuppressionUpload> upload(byte[] csv, SuppressionKind kind) {
            return transport.executeAsync(requests.uploadSuppressions(csv, kind));
        }

        /**
         * @param kind  what the value is
         * @param value the value
         * @return done, later
         * @see SuppressionService#delete(SuppressionKind, String)
         */
        public CompletableFuture<Void> delete(SuppressionKind kind, String value) {
            return transport.executeAsync(requests.deleteSuppression(kind, value));
        }
    }

    /** The terms some formulas need accepted, asynchronously. */
    public final class Attestations {
        private Attestations() {
        }

        /**
         * @return the terms and whether the account accepted them, later
         * @see AttestationService#list()
         */
        public CompletableFuture<List<Attestation>> list() {
            return transport.executeAsync(requests.attestations());
        }
    }

    /** Saying what is right or wrong with a formula's row, asynchronously. */
    public final class FormulaRows {
        private FormulaRows() {
        }

        /**
         * @param rowId         the row's id
         * @param verdict       what is right or wrong with it
         * @param note          a note, or null
         * @param correctDomain with {@link RowVerdict#WRONG_COMPANY}, the company's real domain; otherwise null
         * @return the row's status now, later
         * @see FormulaRowService#feedback(String, RowVerdict, String, String)
         */
        public CompletableFuture<RowFeedback> feedback(String rowId, RowVerdict verdict, String note, String correctDomain) {
            return transport.executeAsync(requests.rowFeedback(rowId, verdict, note, correctDomain));
        }
    }

    /** Webhooks, asynchronously. */
    public final class WebhookCalls {
        private WebhookCalls() {
        }

        /**
         * @return the account's endpoints, later
         * @see WebhookService#list()
         */
        public CompletableFuture<WebhookEndpointList> list() {
            return transport.executeAsync(requests.webhookEndpoints());
        }

        /**
         * @param url         the https URL
         * @param events      the events it is told about
         * @param description your note on it, or null
         * @return the endpoint with its secret, later
         * @see WebhookService#create(String, List, String)
         */
        public CompletableFuture<WebhookEndpoint> create(String url, List<String> events, String description) {
            return transport.executeAsync(requests.createWebhook(url, events, description));
        }

        /**
         * @param endpointId the endpoint's id
         * @return the endpoint, later
         * @see WebhookService#get(String)
         */
        public CompletableFuture<WebhookEndpoint> get(String endpointId) {
            return transport.executeAsync(requests.webhook(endpointId));
        }

        /**
         * @param endpointId the endpoint's id
         * @param change     what to change
         * @return the endpoint as it is now, later
         * @see WebhookService#update(String, WebhookChange)
         */
        public CompletableFuture<WebhookEndpoint> update(String endpointId, WebhookChange change) {
            return transport.executeAsync(requests.updateWebhook(endpointId, change));
        }

        /**
         * @param endpointId the endpoint's id
         * @return done, later
         * @see WebhookService#delete(String)
         */
        public CompletableFuture<Void> delete(String endpointId) {
            return transport.executeAsync(requests.deleteWebhook(endpointId));
        }

        /**
         * @param endpointId the endpoint's id
         * @return the endpoint with its new secret, later
         * @see WebhookService#rotateSecret(String)
         */
        public CompletableFuture<WebhookEndpoint> rotateSecret(String endpointId) {
            return transport.executeAsync(requests.rotateWebhookSecret(endpointId));
        }

        /**
         * @return the run secret, later
         * @see WebhookService#createRunSecret()
         */
        public CompletableFuture<String> createRunSecret() {
            return transport.executeAsync(requests.createRunSecret());
        }

        /**
         * @param params the filter, the page size and the cursor
         * @return one page of the delivery log, later
         * @see WebhookService#deliveriesPage(WebhookDeliveryListParams)
         */
        public CompletableFuture<Page<WebhookDelivery>> deliveriesPage(WebhookDeliveryListParams params) {
            return transport.executeAsync(requests.deliveries(params, null));
        }

        /**
         * @param deliveryId the delivery's id
         * @return the new delivery, later
         * @see WebhookService#resend(String)
         */
        public CompletableFuture<WebhookDelivery> resend(String deliveryId) {
            return transport.executeAsync(requests.resendDelivery(deliveryId));
        }
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
         * @param runId the run's id
         * @param wait  from 0 to 300 seconds
         * @return the run when it has ended or the wait is over, later
         * @see RunService#get(String, Duration)
         */
        public CompletableFuture<Run> get(String runId, Duration wait) {
            return transport.executeAsync(requests.run(runId, wait));
        }

        /**
         * @param runId  the run's id
         * @param format CSV or NDJSON
         * @param limit  records a page, or null
         * @param cursor the cursor of the page before, or null
         * @return one page of records as the API wrote it, later
         * @see RunService#exportPage(String, RecordFormat, Integer, String)
         */
        public CompletableFuture<RecordsExport> exportPage(String runId, RecordFormat format, Integer limit, String cursor) {
            return transport.executeAsync(requests.export(runId, format, limit, cursor));
        }

        /**
         * @param runId the run's id
         * @param name  the file's name
         * @return the file's bytes, later
         * @see RunService#file(String, String)
         */
        public CompletableFuture<byte[]> file(String runId, String name) {
            return transport.executeAsync(requests.file(runId, name));
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

        /**
         * @return where the account stands now, later
         * @see UsageService#standing()
         */
        public CompletableFuture<Standing> standing() {
            return transport.executeAsync(requests.standing()).thenApply(ignored -> transport.standing());
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
