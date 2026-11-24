# EnrichedNow Java SDK

[![CI](https://github.com/enrichednow/enrichednow-java/actions/workflows/ci.yml/badge.svg)](https://github.com/enrichednow/enrichednow-java/actions/workflows/ci.yml)

The official Java client for the [EnrichedNow API](https://docs.enrichednow.com): run scrapers and read their records,
run formulas for finished lists of rows, and query the datasets.

- Java 11 or later. One dependency: Jackson (`jackson-databind`).
- Blocking calls, with a `CompletableFuture` variant of each under `client.async()`.
- Lists page themselves; failed requests are retried when that is safe; every error is a typed exception.

## Install

Maven:

```xml
<dependency>
  <groupId>com.enrichednow</groupId>
  <artifactId>enrichednow-java</artifactId>
  <version>0.4.0</version>
</dependency>
```

Gradle (Kotlin DSL):

```kotlin
implementation("com.enrichednow:enrichednow-java:0.4.0")
```

Gradle (Groovy DSL):

```groovy
implementation 'com.enrichednow:enrichednow-java:0.4.0'
```

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
board.unitCost();                                               // units a run, or a record, by the billing unit
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

#### CSV and NDJSON, written by the API

```java
try (OutputStream out = Files.newOutputStream(Path.of("jobs.csv"))) {
    client.runs().exportAll(run.id(), RecordFormat.CSV, out);   // every page; the header row once
}
RecordsExport page = client.runs().exportPage(run.id(), RecordFormat.NDJSON, 500, null);  // text(), nextCursor(), expired()
byte[] png = client.runs().file(run.id(), "screenshot.png");   // a file the run made
```

The CSV has a column for each field of the scraper's record schema, in its order; a nested object becomes
`parent.child` columns, a list one cell of JSON, and a cell a spreadsheet would run as a formula starts with an
apostrophe.

`client.runs().get(id, Duration.ofSeconds(120))` holds the request until the run ends, up to 300 seconds, as
`RunOptions.waitUpTo` does on starting one. `RunListParams` also takes `apiKeyId`, `from` and `to`.

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
| `formulaRuns().list` | 1 to 100, default 20 |
| `formulaRuns().rows`, `savedFormulas().rows` | 1 to 1000, default 100 (`runFormula`'s result: 1000) |
| `savedFormulas().list` | 1 to 100, default 50 |
| `suppressions().list` | 1 to 1000, default 100 |

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

`usage.unmetered()` is `true` for an account with no unit limit. Every answer about runs carries the account's
units and runs in progress in its headers: `client.standing()` is the last one seen, and `client.usage().standing()`
asks for it (`unitsRemaining()`, `unitsTopup()`, `unitsReset()`, `runsRemaining()`, `unitsAvailable()`).

## Webhooks

```java
WebhookEndpoint made = client.webhooks().create("https://example.com/hooks/enrichednow",
        List.of("run.succeeded", "run.failed"), "production");
String secret = made.secret();                                  // shown this once: keep it

client.webhooks().list();
client.webhooks().update(made.id(), WebhookChange.builder().enabled(false).build());
client.webhooks().rotateSecret(made.id());
client.webhooks().delete(made.id());
String runSecret = client.webhooks().createRunSecret();          // for RunOptions.builder().webhookUrl(...)
for (WebhookDelivery d : client.webhooks().deliveries(WebhookDeliveryListParams.builder().status("failed").build())) { ... }
client.webhooks().resend(deliveryId);
```

A request that arrives is checked before it is acted on: give the body exactly as it arrived.

```java
byte[] body = request.getInputStream().readAllBytes();
WebhookEvent event = Webhooks.parse(secret, request.getHeader(Webhooks.TIMESTAMP_HEADER), body,
        request.getHeader(Webhooks.SIGNATURE_HEADER));             // WebhookVerificationException: answer 400
System.out.println(event.id() + " " + event.type() + " " + event.run().id());
```

`Webhooks.verify` only checks. Both compare in constant time and refuse a timestamp more than 5 minutes from now.
`event.id()` is the same on every attempt: use it to ignore a repeat.

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

## Formulas

A formula is a ready-made run: give it a few parameters and it returns a finished list of rows, each with a score and
where its values came from. The catalogue is free to read; a run spends units, never more than its unit cap.

```java
for (Formula formula : client.formulas().list()) {                 // GET /v1/formulas
    System.out.println(formula.id() + ": " + formula.summary());
}
FormulaDetails hunter = client.formulas().get("job-order-hunter"); // parameters, presets, columns, sample rows
hunter.parameters();                                               // JSON Schema of the parameters, as a Map
hunter.columns();                                                  // the columns of a row
```

### Run one and read its rows

Each formula has a parameter class in `com.enrichednow.formulas`, with a builder that refuses to build without the
required parameters. A `Map` works too, for any formula.

```java
JobOrderHunterParams params = JobOrderHunterParams.builder()
        .jobTitles("Java Developer", "DevOps Engineer")
        .locations("London")
        .postedWithinDays(2)
        .build();

FormulaResult result = client.runFormula(params);                  // starts it, waits for it to end
System.out.println(result.run().status() + ": " + result.run().rowsTotal() + " rows, " + result.run().unitsSpent() + " units");
for (FormulaRow row : result) {                                     // every row, best first; pages are fetched as you go
    System.out.println(row.get("company_name") + " " + row.get("contact_email") + " " + row.score());
}

// The same with a Map, for any formula.
client.runFormula("job-order-hunter", Map.of("job_titles", List.of("Java Developer"), "locations", List.of("London")));
```

`runFormula` throws a `FormulaRunFailedException` when the run ended `failed`, `unsupported` or `cancelled`. A run that
ended `partial` is returned: it has rows, and `run().reason()` says what is missing (`unit_cap_reached`,
`contacts_not_connected`, ...). Typed parameters of one formula given with another's id are refused before anything is
sent.

```java
FormulaResult result = client.runFormula(params, FormulaRunOptions.builder()
        .unitCap(2000)                          // the most it may spend; it stops there and ends partial
        .rowCap(100)                            // the most rows
        .timeout(Duration.ofMinutes(15))        // give up waiting after this; default 10 minutes
        .throwOnFailure(false)                  // return the result whatever status the run ended with
        .build());

result.rows(FormulaRowListParams.builder()
        .fields("company_name", "job_title", "contact_email")      // only these columns
        .minScore(10)                                              // rows scored at least 10
        .view(FormulaRowListParams.View.COMPANY)                   // one row per company, where the formula has that view
        .build());

try (OutputStream out = Files.newOutputStream(Path.of("job-orders.csv"))) {
    result.exportAll(RecordFormat.CSV, out);                        // one file, the header row once; or NDJSON
}
```

### Estimate first, or preview

```java
FormulaEstimate cost = client.formulas().estimate(params);           // spends nothing
if (cost.valid()) {
    System.out.println("about " + cost.estimate().expectedUnits() + ", at most " + cost.estimate().maxUnits() + " units");
} else {
    cost.errors().forEach(p -> System.err.println(p.path() + ": " + p.message()));   // not an exception
}

FormulaResult preview = client.runFormula(params, FormulaRunOptions.preview());  // at most 10 rows and 50 units
```

### Runs, one call at a time

```java
FormulaRun run = client.formulas().run(params);                      // POST /v1/formulas/{id}/runs; returns when queued
FormulaRun run = client.formulas().run(params, FormulaRunOptions.builder()
        .wait(Duration.ofSeconds(60)).idempotencyKey("job-orders-2026-11-25").build());
FormulaRun ended = client.formulaRuns().waitFor(run.id());           // each look held open by the API, up to 10 minutes
client.formulaRuns().get(run.id());                                  // with what each step did: run.steps()
client.formulaRuns().rows(run.id());                                 // rows can be read while it runs
client.formulaRuns().exportAll(run.id(), RecordFormat.NDJSON, out);
client.formulaRuns().cancel(run.id());
for (FormulaRun each : client.formulaRuns().list(FormulaRunListParams.builder().formulaId("job-order-hunter").build())) { ... }
```

Starting a run carries an `Idempotency-Key` and is retried like a scraper run. Rows are kept 90 days.

### Saved formulas

A saved formula keeps its parameters and caps, runs on a schedule, and delivers only rows it has not delivered before:
poll its rows with `since` for what is new.

```java
SavedFormula saved = client.savedFormulas().create(SavedFormulaCreate.builder(params)
        .schedule(Schedule.weekdays("07:30", "Europe/London"))  // or hourly, daily, weekly(DayOfWeek, ...), monthly(day, ...)
        .monthlyUnitCap(20000)
        .notify(Notify.DIGEST)                                  // mail about new rows: EACH_RUN, DIGEST or NONE
        .build());

Instant lastPoll = Instant.parse("2026-11-24T07:30:00Z");
for (FormulaRow row : client.savedFormulas().rows(saved.id(), FormulaRowListParams.builder().since(lastPoll).build())) {
    System.out.println(row.get("company_name"));
}

client.savedFormulas().run(saved.id());                       // now, by hand; SavedFormulaRunOptions for preview, includeSeen
client.savedFormulas().update(saved.id(), SavedFormulaChange.builder()
        .name("Java in London")
        .removeMonthlyUnitCap()                                 // also removeSchedule(), autoName()
        .build());
client.savedFormulas().pause(saved.id());
client.savedFormulas().resume(saved.id());
client.savedFormulas().duplicate(saved.id(), DuplicateOptions.builder().copySeen(true).build());
client.savedFormulas().resetSeen(saved.id());                 // its next run may deliver the same rows again
client.savedFormulas().archive(saved.id());
client.savedFormulas().limits();                              // what the plan allows: schedules, shortest interval
```

### The suppression list

Domains, addresses and company names that formula runs leave out.

```java
client.suppressions().add(SuppressionEntry.domain("client-one.example"), SuppressionEntry.email("jo@client-two.example"));
SuppressionUpload upload = client.suppressions().upload(Path.of("do-not-contact.csv"));   // or bytes, text, a stream
System.out.println(upload.added() + " added, " + upload.already() + " already there, " + upload.invalid().size() + " unreadable");
for (Suppression entry : client.suppressions().list()) { ... }
client.suppressions().delete(SuppressionKind.DOMAIN, "client-one.example");
```

A CSV file with a header row of `kind` and `value` says the kind of each line; otherwise each value is read as an address,
a domain or a company name by how it looks, or as the kind given: `upload(file, SuppressionKind.DOMAIN)`.

### Row feedback and the terms

```java
client.formulaRows().feedback(row.id(), RowVerdict.WRONG_COMPANY, "The agency, not the employer.", "acme.example");
client.attestations().list();     // the terms a formula that returns people needs, and whether they are accepted
```

A formula whose `needsAttestation()` is true runs once an owner or an admin of the account has accepted the terms, in
the console; an API key cannot accept them.

### When a formula cannot run

```java
try {
    client.runFormula(params);
} catch (AttestationRequiredException e) {        // 409: the terms are not accepted yet
    System.err.println("Accept the " + e.kind() + " terms in the console first.");
} catch (RequirementMissingException e) {         // 409: e.g. [product:data]
    System.err.println("The account lacks " + e.missing());
} catch (FormulaUnavailableException e) {         // 409: deprecated, or not offered now
    System.err.println(e.formulaStatus());
} catch (ValidationException e) {                 // 422: the parameters, or unit_cap_too_low
    System.err.println(e.code() + " " + e.fieldErrors() + " " + e.minUnitCap());
} catch (LimitReachedException e) {               // 402: a spend limit, e.g. a saved formula's monthly cap
    System.err.println(e.scope() + " " + e.limitUnits());
} catch (FormulaRunFailedException e) {           // the request worked, the run did not
    System.err.println(e.status() + " / " + e.reason());
}
```

`PlanLimitReachedException` (403) comes from saving, changing, duplicating or resuming a saved formula with a schedule
the plan does not allow, and
`SavedFormulaArchivedException` (409) from changing or running an archived saved formula.

## Service health

```java
client.health().isOk();                                        // GET /v1/health
```

## Exceptions

Everything the SDK throws is unchecked and extends `EnrichedNowException`.

| Exception | When |
| --- | --- |
| `ApiException` | The API answered with an error. The base of the next eight. |
| `AuthenticationException` | `401 unauthorized`: the key is missing, mistyped or revoked. |
| `QuotaExceededException` | `402 quota_exceeded`: this month's allowance is used up. `usedUnits()`, `allowanceUnits()`. |
| `LimitReachedException` | `402 limit_reached`: a spend limit set on the account, not the plan's allowance. `scope()` (`account`, `key`, `saved_formula` or `formula_run`), `keyId()`, `savedFormulaId()`, `limitUnits()`, `usedUnits()`, `unitsNeeded()`. Extends `QuotaExceededException`. |
| `PermissionDeniedException` | `403 forbidden`: for example, the account does not have Datasets. |
| `PlanLimitReachedException` | `403 plan_limit_reached`: the plan does not allow this many schedules, a cadence this short, or this many destinations. `limit()`, `allowed()`, `current()`, `asked()`. Extends `PermissionDeniedException`. |
| `NotFoundException` | `404 not_found` or `formula_not_found`: no such scraper, run, posting, company or formula. |
| `ConflictException` | `409`: `idempotency_conflict`, and the base of the four below. |
| `AttestationRequiredException` | `409 attestation_required`: the formula returns people and the terms are not accepted yet. `kind()`, `textVersion()`. |
| `FormulaUnavailableException` | `409 formula_unavailable`: the formula cannot be run now. `formulaStatus()`, or `formulaId()` and `major()` for a saved formula's version. |
| `RequirementMissingException` | `409 requirement_missing`: the account lacks what the formula needs. `missing()`, e.g. `product:data`. |
| `SavedFormulaArchivedException` | `409 saved_formula_archived`: duplicate it to use it again. `savedFormulaId()`. |
| `ValidationException` | `422 invalid_input`: the request does not fit. `fieldErrors()` lists every problem. `422 unit_cap_too_low`: `minUnitCap()`. |
| `RateLimitException` | `429`: `too_many_runs` (`isTooManyRuns()`, `activeRuns()`, `maxConcurrentRuns()`) or `rate_limited` (`retryAfter()`). |
| `ServerException` | `5xx`: `internal`, or `data_unavailable` for Datasets. |
| `ConnectionException` | No answer arrived. |
| `ApiTimeoutException` | The request took longer than the timeout. A `ConnectionException`. |
| `RunFailedException` | `scrape`: the run ended `failed`, `unsupported` or `cancelled`. `status()`, `reason()`, `run()`. |
| `FormulaRunFailedException` | `runFormula`: the formula run ended `failed`, `unsupported` or `cancelled`. `formulaRun()`, `status()`, `reason()`. Extends `RunFailedException`. |
| `RunTimeoutException` | `scrape` / `runFormula` / `waitFor`: the run had not ended in time. `runId()`, `run()` or `formulaRun()`. |

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
| `5xx` | Retried | Retried, with the same idempotency key |
| Timeout, or a connection that broke | Retried | Retried, with the same idempotency key |
| `401`, `402`, `403`, `404`, `422` | Not retried: the same request gets the same answer | Not retried |

Starting a run is safe to repeat: the request carries an `Idempotency-Key`, a new one for each call, so a retry gets
the run the first attempt started and never a second one. To make a call of your own repeatable (a job that may be
restarted, say), give it your own key:

```java
Run run = client.scrapers().run("ats.board", input,
        RunOptions.builder().idempotencyKey("import-2026-11-19-acme").build());
```

For 24 hours the same key with the same request gives the same run. The same key with a different request is refused:
a `ConflictException` (an `ApiException` with status 409) with code `idempotency_conflict`.

Formula runs work the same way (`FormulaRunOptions` and `SavedFormulaRunOptions` take an `idempotencyKey`). Saving a
formula and duplicating one are not sent again after a failure that may have reached the API; changing, pausing,
resuming, the suppression list and row feedback are safe to repeat and are retried as reads are.

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

client.async().runFormula(params).thenAccept(result -> System.out.println(result.run().rowsTotal()));
client.async().formulaRuns().rowsPage(runId, FormulaRowListParams.limit(1000));
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
