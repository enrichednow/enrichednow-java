# EnrichedNow Java SDK

[![CI](https://github.com/enrichednow/enrichednow-java/actions/workflows/ci.yml/badge.svg)](https://github.com/enrichednow/enrichednow-java/actions/workflows/ci.yml)

The official Java client for the [EnrichedNow API](https://docs.enrichednow.com): run scrapers, read their records, and
query the datasets.

- Java 11 or later. One dependency: Jackson (`jackson-databind`).
- Blocking calls, with a `CompletableFuture` variant of each under `client.async()`.
- Lists page themselves; failed requests are retried when that is safe; every error is a typed exception.

## Install

Maven:

```xml
<dependency>
  <groupId>com.enrichednow</groupId>
  <artifactId>enrichednow-java</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle (Kotlin DSL):

```kotlin
implementation("com.enrichednow:enrichednow-java:0.1.0")
```

Gradle (Groovy DSL):

```groovy
implementation 'com.enrichednow:enrichednow-java:0.1.0'
```

> **Not on Maven Central yet.** Until the first release is there, clone this repository, run `./mvnw install`, and
> depend on the same coordinates from your local Maven repository.

## Quickstart

Create an API key in the console, under **API keys**, and keep it out of your code:

```bash
export ENRICHEDNOW_API_KEY="en_live_..."
```

```java
import com.enrichednow.EnrichedNowClient;
import com.enrichednow.ScrapeResult;
import java.util.Map;

public class Quickstart {
    public static void main(String[] args) {
        EnrichedNowClient client = EnrichedNowClient.fromEnv();   // reads ENRICHEDNOW_API_KEY

        // Start a run, wait for it to end, read what it found.
        ScrapeResult result = client.scrape("ats.board", Map.of("board_url", "https://boards.greenhouse.io/acme"));

        System.out.println(result.run().status() + ": " + result.run().recordCount() + " records, "
                + result.run().units() + " unit(s)");
        for (Map<String, Object> job : result.records()) {       // pages are fetched as you go
            System.out.println(job.get("title") + " - " + job.get("apply_url"));
        }
    }
}
```

`scrape` throws a `RunFailedException` when the run ended `failed`, `unsupported` or `cancelled`, so a result you get
back always has records to read (`ok` or `partial`).

## The client

```java
EnrichedNowClient client = EnrichedNowClient.builder()
        .apiKey("en_live_...")                        // default: the ENRICHEDNOW_API_KEY environment variable
        .baseUrl("https://api.enrichednow.com")       // default
        .timeout(Duration.ofSeconds(60))              // per request; default 60 s
        .maxRetries(2)                                // default 2; 0 turns retries off
        .httpClient(HttpClient.newBuilder().build())  // your own java.net.http.HttpClient
        .build();
```

`EnrichedNowClient.fromEnv()` is the same as `builder().build()`. Without a key, given or in the environment,
`build()` throws an `EnrichedNowException` that says so.

A client is immutable and thread-safe. Make one and share it.

## Scrapers

```java
List<Scraper> scrapers = client.scrapers().list();              // GET /v1/scrapers
ScraperDetail board = client.scrapers().get("ats.board");       // GET /v1/scrapers/{id}
board.inputSchema();                                            // JSON Schema of the input, as a Map
board.recordSchema();                                           // JSON Schema of one record
board.billingUnit();                                            // "request" or "record"
```

### Start a run

```java
// Returns as soon as the run is queued.
Run run = client.scrapers().run("careers.company", Map.of("domain", "acme.com"));

// Or ask the API to hold the request open for up to 60 seconds.
Run run = client.scrapers().run("careers.company", Map.of("domain", "acme.com"),
        RunOptions.waitUpTo(Duration.ofSeconds(60)));

if (!run.isFinished()) {
    run = client.runs().waitFor(run.id());                      // asks every 2 s, for up to 10 minutes
}
```

The input is a `Map`, or any object Jackson writes as the JSON object the scraper's `inputSchema` describes.

**Check the status, not only that the call returned.** A run that could not read its target is still a successful
call: read `run.status()` and `run.reason()`.

| `run.status()` | Meaning | Charged |
| --- | --- | --- |
| `QUEUED`, `RUNNING` | In progress. | |
| `OK` | Ended with its result. | Yes |
| `PARTIAL` | Ended with part of its result; `reason()` says what is missing. | Yes |
| `FAILED` | Ended without a result; `reason()` says why. | No |
| `UNSUPPORTED` | The scraper cannot read this target; `reason()` says why. | No |
| `CANCELLED` | Stopped on request. | No |

`run.isFinished()` is true for the last five; `run.isSuccessful()` for `OK` and `PARTIAL`. A status this version of
the SDK does not know is `RunStatus.UNKNOWN`, with the API's own word in `run.rawStatus()`.

### The one-liner

```java
ScrapeResult result = client.scrape("ats.board", Map.of("board_url", "https://boards.greenhouse.io/acme"));
result.run();        // the run as it ended
result.records();    // its records, paged automatically
```

`scrape` starts the run, waits for it to end and returns it with its records. Options:

```java
ScrapeResult result = client.scrape("careers.company", Map.of("domain", "acme.com"),
        ScrapeOptions.builder()
                .timeout(Duration.ofMinutes(5))       // give up after this; default 10 minutes
                .pollInterval(Duration.ofSeconds(2))  // default
                .throwOnFailure(false)                // return the result whatever status the run ended with
                .build());
```

When the wait gives up it throws a `RunTimeoutException`; the run carries on, and `e.runId()` is how you find it.

## Runs

```java
Run run = client.runs().get("run_id");                              // GET /v1/runs/{id}
Run ended = client.runs().waitFor("run_id");                        // poll until it has ended
Run ended = client.runs().waitFor("run_id",
        WaitOptions.builder().timeout(Duration.ofMinutes(2)).pollInterval(Duration.ofSeconds(1)).build());
Run asked = client.runs().cancel("run_id");                         // POST /v1/runs/{id}/cancel

for (Run each : client.runs().list()) { ... }                       // GET /v1/runs, newest first, every page
client.runs().list(RunListParams.builder()
        .scraperId("careers.company").status(RunStatus.PARTIAL).limit(50).build());
```

`waitFor` returns the run whatever status it ended with; only `scrape` throws for a run that did not succeed.

Cancelling is cooperative: a queued run ends `cancelled` at once, a running one is asked to stop and may still
complete. After a cancel, `waitFor` shows how the run really ended.

### Records

Records are whatever the scraper returns, so they come as JSON: a `Map<String, Object>` by default, or your own
class.

```java
for (Map<String, Object> record : client.runs().records(run.id())) {
    System.out.println(record.get("title"));
}

// As your own class. Unknown fields are ignored.
public class Job {
    public String title;
    @JsonProperty("apply_url") public String applyUrl;
}
for (Job job : client.runs().records(run.id(), Job.class)) { ... }

// As a Jackson tree.
client.runs().records(run.id(), JsonNode.class);
```

Records are kept for **7 days** after the run ended. Fetch them and store what you need. After that the list is
empty and `RecordPage.expired()` is `true`.

## Pagination

Every list returns a `PagedIterable`: iterate it and the next page is fetched when the current one runs out.

```java
PagedIterable<Run> runs = client.runs().list();

for (Run run : runs) { ... }                                   // every item, every page
runs.stream().filter(Run::isSuccessful).limit(100)...;         // lazy: stops fetching when it has enough
List<Run> all = runs.toList();                                 // everything, in memory

for (Page<Run> page : runs.pages()) {                          // a page at a time
    page.items();
    page.nextCursor();                                         // null on the last page
}
```

Or drive it yourself:

```java
RecordPage<Map<String, Object>> page = client.runs().recordsPage(run.id(), RecordListParams.limit(1000));
while (true) {
    page.items().forEach(System.out::println);
    if (!page.hasNextPage()) break;
    page = page.nextPage();
}
```

A cursor is an opaque string. To pick a list up later, keep `page.nextCursor()` and pass it back with
`.cursor(...)` on the same parameters.

| List | Page size (`limit`) |
| --- | --- |
| `runs().list` | 1 to 100, default 20 |
| `runs().records` | 1 to 1000, default 100 |
| `data().postings().list`, `data().companies().list`, `data().companies().postings` | 1 to 200, default 50 |

## Usage

```java
Usage usage = client.usage().get();                            // this calendar month (UTC), a row a day
System.out.println(usage.totalUnits() + " of " + usage.allowanceUnits());

Usage byScraper = client.usage().get(UsageParams.builder()
        .from(LocalDate.of(2026, 11, 1)).to(LocalDate.of(2026, 11, 30))
        .groupBy(UsageParams.GroupBy.SCRAPER)                  // DAY, SCRAPER or PRODUCT
        .product(UsageParams.Product.SCRAPERS)                 // SCRAPERS or DATA; both when left out
        .build());
for (Usage.Row row : byScraper.rows()) {
    System.out.println(row.key() + ": " + row.units() + " units in " + row.runs() + " runs");
}
```

## Datasets

Job postings and the companies they belong to, under `/v1/data`. Datasets is a product of its own, read with the
same key; an account that has not been given it gets a `PermissionDeniedException`. Each record returned costs one
data unit.

```java
// Search postings. Every filter given must hold.
PagedIterable<Posting> postings = client.data().postings().list(PostingListParams.builder()
        .companyDomain("acme.com")
        .title("software engineer")
        .country("DE")
        .remote(true)
        .seniority("senior")
        .status("open")                                        // open, closed or all
        .firstSeenFrom(Instant.parse("2026-11-16T00:00:00Z"))
        .sort("last_seen_at")                                  // or first_seen_at, the default
        .limit(200)
        .build());
for (Posting posting : postings) {
    System.out.println(posting.titleClean() + " at " + posting.company().name() + ", " + posting.location().city());
}

// One posting, with its description and history.
PostingDetail posting = client.data().postings().get("posting_id");
posting.description();
posting.history().events();

// Companies.
Company company = client.data().companies().get("acme.com");   // any host of the domain will do
company.postings().open();
client.data().companies().list(CompanyListParams.builder().q("acme").ats("greenhouse").build());
client.data().companies().postings("acme.com", PostingListParams.builder().status("open").build());
```

`PostingListParams` has every filter of the API: `companyDomain`, `title`, `location`, `country`, `remote`,
`employmentType`, `seniority`, `function`, `status`, `source`, `provenance`, `unmatched`, `postedFrom`/`postedTo`,
`firstSeenFrom`/`firstSeenTo`, `lastSeenFrom`/`lastSeenTo`, `sort`, `limit` and `cursor`.

## Service health

```java
client.health().isOk();                                        // GET /v1/health
```

## Exceptions

Everything the SDK throws is unchecked and extends `EnrichedNowException`.

| Exception | When |
| --- | --- |
| `ApiException` | The API answered with an error. The base of the next seven. |
| `AuthenticationException` | `401 unauthorized`: the key is missing, mistyped or revoked. |
| `QuotaExceededException` | `402 quota_exceeded`: this month's allowance is used up. `usedUnits()`, `allowanceUnits()`. |
| `PermissionDeniedException` | `403 forbidden`: for example, the account does not have Datasets. |
| `NotFoundException` | `404 not_found`: no such scraper, run, posting or company. |
| `ValidationException` | `422 invalid_input`: the request does not fit. `fieldErrors()` lists every problem. |
| `RateLimitException` | `429`: `too_many_runs` (`isTooManyRuns()`, `activeRuns()`, `maxConcurrentRuns()`) or `rate_limited` (`retryAfter()`). |
| `ServerException` | `5xx`: `internal`, or `data_unavailable` for Datasets. |
| `ConnectionException` | No answer arrived. |
| `ApiTimeoutException` | The request took longer than the timeout. A `ConnectionException`. |
| `RunFailedException` | `scrape`: the run ended `failed`, `unsupported` or `cancelled`. `status()`, `reason()`, `run()`. |
| `RunTimeoutException` | `scrape` / `waitFor`: the run had not ended in time. `runId()`, `run()`. |

```java
try {
    client.scrape("ats.board", Map.of("board_url", "https://boards.greenhouse.io/acme"));
} catch (RunFailedException e) {
    // The request worked, the scrape did not.
    System.err.println(e.status() + " / " + e.reason());       // e.g. unsupported / render_required
} catch (ValidationException e) {
    e.fieldErrors().forEach(f -> System.err.println(f.path() + ": " + f.message()));
} catch (RateLimitException e) {
    if (e.isTooManyRuns()) { /* wait for one of your runs to end */ }
} catch (ApiException e) {
    System.err.println(e.statusCode() + " " + e.code() + ": " + e.apiMessage());
} catch (ConnectionException e) {
    // no answer
}
```

Every `ApiException` has `statusCode()`, `code()` (the API's stable code; match on this), `apiMessage()` (a sentence
for a person; its wording can change), `details()` and `requestId()` (when the answer carried one).

## Retries

A request that fails is tried again up to `maxRetries` times (default 2), with exponential backoff and jitter
(about 0.5 s, 1 s, 2 s, ... up to 8 s; `initialRetryDelay` and `maxRetryDelay` on the builder change that).

| Failure | Reads (`GET`) and `cancel` | Starting a run |
| --- | --- | --- |
| The connection could not be made | Retried | Retried: nothing was sent |
| `429` | Retried, after `Retry-After` when the answer has one (at most 60 s) | Retried: no run was created |
| `5xx` | Retried | **Not retried** |
| Timeout, or a connection that broke | Retried | **Not retried** |
| `401`, `402`, `403`, `404`, `422` | Not retried: the same request gets the same answer | Not retried |

Starting a run is not idempotent: a second `POST` starts, and may charge for, a second run. So when starting a run
fails with a `ConnectionException` or a `ServerException`, the run may or may not exist. Look at
`client.runs().list()` before starting it again.

## Async

Every call has a non-blocking twin under `client.async()` that returns a `CompletableFuture`, with the same
retries:

```java
client.async().scrape("ats.board", Map.of("board_url", "https://boards.greenhouse.io/acme"))
        .thenAccept(result -> System.out.println(result.run().recordCount()));

CompletableFuture<Run> run = client.async().runs().get("run_id");
CompletableFuture<Run> ended = client.async().runs().waitFor("run_id");

client.async().data().postings().listPage(PostingListParams.none())
        .thenCompose(Page::nextPageAsync);                     // lists come a page at a time
```

A call that fails completes its future with the exception the blocking call would have thrown (`join()` wraps it in
a `CompletionException`).

## Building

```bash
./mvnw verify        # compiles, runs the tests, builds the jar, the sources jar and the javadoc jar into target/
```

`./mvnw` downloads Maven on first use; a JDK 11 or later is all that is needed. The tests run against a
local HTTP server and need no API key.

## Releasing

Pushing a tag `vX.Y.Z` that matches the version in `pom.xml` runs `.github/workflows/release.yml`, which builds
with the `release` profile — the jar, the sources jar and the javadoc jar, each signed — and publishes them to
Maven Central.

## Licence

MIT. See [LICENSE](LICENSE). To report a security problem, see [SECURITY.md](SECURITY.md).
