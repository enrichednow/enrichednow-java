package com.enrichednow;

import com.enrichednow.errors.EnrichedNowException;
import com.enrichednow.errors.RunFailedException;
import com.enrichednow.models.Health;
import com.enrichednow.models.Run;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

/**
 * The client of the EnrichedNow API.
 *
 * <pre>{@code
 * EnrichedNowClient client = EnrichedNowClient.fromEnv();          // reads ENRICHEDNOW_API_KEY
 *
 * ScrapeResult result = client.scrape("ats.board", Map.of("board_url", "https://boards.greenhouse.io/acme"));
 * for (Map<String, Object> job : result.records()) {
 *     System.out.println(job.get("title"));
 * }
 * }</pre>
 *
 * <p>A client is immutable and safe to share between threads; make one and keep it. Every method blocks until the API
 * answers; {@link #async()} has the same calls returning a {@link java.util.concurrent.CompletableFuture}.
 *
 * <p>Every failure is an unchecked {@link EnrichedNowException}: an {@link com.enrichednow.errors.ApiException} for
 * an error answer, a {@link com.enrichednow.errors.ConnectionException} when no answer arrived.
 */
public final class EnrichedNowClient {
    /** The environment variable the API key is read from when none is given. */
    public static final String API_KEY_ENV = "ENRICHEDNOW_API_KEY";
    /** Where the API is. */
    public static final String DEFAULT_BASE_URL = "https://api.enrichednow.com";
    /** How long one request may take, unless the builder says otherwise. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);
    /** How many times a failed request is tried again, unless the builder says otherwise. */
    public static final int DEFAULT_MAX_RETRIES = 2;

    private final Transport transport;
    private final ScraperService scrapers;
    private final RunService runs;
    private final UsageService usage;
    private final DataService data;
    private final EnrichedNowAsyncClient async;

    private EnrichedNowClient(Builder builder) {
        String apiKey = builder.apiKey != null ? builder.apiKey : builder.environment.apply(API_KEY_ENV);
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new EnrichedNowException("No API key. Pass one with EnrichedNowClient.builder().apiKey(...) or set the "
                    + API_KEY_ENV + " environment variable. Keys are created in the console, under API keys.");
        }
        HttpClient http = builder.httpClient != null ? builder.httpClient
                : HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.transport = new Transport(http, builder.baseUrl, apiKey.trim(), builder.timeout, builder.maxRetries,
                builder.initialRetryDelay, builder.maxRetryDelay);
        Requests requests = new Requests(transport);
        this.scrapers = new ScraperService(transport, requests);
        this.runs = new RunService(transport, requests);
        this.usage = new UsageService(transport, requests);
        this.data = new DataService(transport, requests);
        this.async = new EnrichedNowAsyncClient(transport, requests, runs);
    }

    /**
     * A client with the defaults and the API key of the {@code ENRICHEDNOW_API_KEY} environment variable.
     *
     * @return the client
     * @throws EnrichedNowException when the variable is not set
     */
    public static EnrichedNowClient fromEnv() {
        return builder().build();
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    /** @return the catalogue of scrapers, and starting a run */
    public ScraperService scrapers() {
        return scrapers;
    }

    /** @return runs and their records */
    public RunService runs() {
        return runs;
    }

    /** @return units used */
    public UsageService usage() {
        return usage;
    }

    /** @return Datasets: job postings and companies */
    public DataService data() {
        return data;
    }

    /** @return the same calls, each returning a {@link java.util.concurrent.CompletableFuture} instead of blocking */
    public EnrichedNowAsyncClient async() {
        return async;
    }

    /**
     * Whether the service is up: {@code GET /v1/health}.
     *
     * @return the service's answer
     */
    public Health health() {
        return transport.execute(async.requests().health());
    }

    /**
     * Runs a scraper and waits for the result: starts a run, waits for it to end (up to 10 minutes), and returns the
     * run with its records.
     *
     * @param scraperId the scraper's id, e.g. {@code ats.board}
     * @param input     the scraper's input: a {@code Map}, or any object that is written as the JSON object the
     *                  scraper's {@code inputSchema} describes
     * @return the run, ended {@code ok} or {@code partial}, and its records
     * @throws RunFailedException                             when the run ended {@code failed}, {@code unsupported} or
     *                                                        {@code cancelled}
     * @throws com.enrichednow.errors.RunTimeoutException     when the run had not ended in time; it carries on
     * @throws com.enrichednow.errors.ApiException            when the API refused the request
     */
    public ScrapeResult scrape(String scraperId, Object input) {
        return scrape(scraperId, input, ScrapeOptions.defaults());
    }

    /**
     * Runs a scraper and waits for the result.
     *
     * <p>The run is started with the API holding the request open (up to 60 seconds); a run that takes longer is then
     * asked about every {@link ScrapeOptions#pollInterval()} until it ends or {@link ScrapeOptions#timeout()} is up.
     *
     * @param scraperId the scraper's id
     * @param input     the scraper's input
     * @param options   how long to wait, and whether a run that did not succeed throws
     * @return the run as it ended, and its records
     * @throws RunFailedException                         when the run ended {@code failed}, {@code unsupported} or
     *                                                    {@code cancelled}, unless the options say not to throw
     * @throws com.enrichednow.errors.RunTimeoutException when the run had not ended in time; it carries on
     */
    public ScrapeResult scrape(String scraperId, Object input, ScrapeOptions options) {
        ScrapeOptions scrape = options == null ? ScrapeOptions.defaults() : options;
        long deadline = System.nanoTime() + scrape.timeout().toNanos();
        Run run = scrapers.run(scraperId, input, RunOptions.waitUpTo(firstWait(scrape)));
        run = runs.awaitEnd(run, deadline, scrape.pollInterval());
        return result(run, scrape, runs);
    }

    static Duration firstWait(ScrapeOptions options) {
        return options.timeout().compareTo(RunOptions.MAX_WAIT) < 0 ? options.timeout() : RunOptions.MAX_WAIT;
    }

    static ScrapeResult result(Run run, ScrapeOptions options, RunService runs) {
        if (options.throwOnFailure() && !run.isSuccessful()) {
            throw new RunFailedException(run);
        }
        return new ScrapeResult(run, runs);
    }

    /** Builds an {@link EnrichedNowClient}. */
    public static final class Builder {
        private String apiKey;
        private String baseUrl = DEFAULT_BASE_URL;
        private Duration timeout = DEFAULT_TIMEOUT;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private Duration initialRetryDelay = Duration.ofMillis(500);
        private Duration maxRetryDelay = Duration.ofSeconds(8);
        private HttpClient httpClient;
        private Function<String, String> environment = System::getenv;

        private Builder() {
        }

        /**
         * @param apiKey the API key ({@code en_live_...}). Without one, the {@code ENRICHEDNOW_API_KEY} environment
         *               variable is read
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * @param baseUrl where the API is; default {@code https://api.enrichednow.com}
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            Objects.requireNonNull(baseUrl, "baseUrl");
            String trimmed = baseUrl.trim();
            while (trimmed.endsWith("/")) {
                trimmed = trimmed.substring(0, trimmed.length() - 1);
            }
            URI uri = URI.create(trimmed);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("baseUrl must be an absolute http(s) address, e.g. " + DEFAULT_BASE_URL);
            }
            this.baseUrl = trimmed;
            return this;
        }

        /**
         * @param timeout how long one request may take; default 60 seconds. A request that asks the API to wait for a
         *                run is allowed that wait on top
         * @return this builder
         */
        public Builder timeout(Duration timeout) {
            this.timeout = WaitOptions.positive(timeout, "timeout");
            return this;
        }

        /**
         * @param maxRetries how many times a failed request is tried again; default 2, and 0 turns retries off
         * @return this builder
         */
        public Builder maxRetries(int maxRetries) {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("maxRetries must not be negative");
            }
            this.maxRetries = maxRetries;
            return this;
        }

        /**
         * @param initialRetryDelay the pause before the first retry, which doubles with each one after it; default
         *                          half a second
         * @return this builder
         */
        public Builder initialRetryDelay(Duration initialRetryDelay) {
            this.initialRetryDelay = nonNegative(initialRetryDelay, "initialRetryDelay");
            return this;
        }

        /**
         * @param maxRetryDelay the longest pause between two retries; default 8 seconds
         * @return this builder
         */
        public Builder maxRetryDelay(Duration maxRetryDelay) {
            this.maxRetryDelay = nonNegative(maxRetryDelay, "maxRetryDelay");
            return this;
        }

        /**
         * @param httpClient the {@link HttpClient} to send requests with, for an executor, TLS or network settings of
         *                   your own; default one with a 10-second connect timeout
         * @return this builder
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        Builder environment(Function<String, String> environment) {
            this.environment = environment;
            return this;
        }

        /**
         * @return the client
         * @throws EnrichedNowException when there is no API key, given or in the environment
         */
        public EnrichedNowClient build() {
            return new EnrichedNowClient(this);
        }

        private static Duration nonNegative(Duration value, String name) {
            Objects.requireNonNull(value, name);
            if (value.isNegative()) {
                throw new IllegalArgumentException(name + " must not be negative");
            }
            return value;
        }
    }
}
