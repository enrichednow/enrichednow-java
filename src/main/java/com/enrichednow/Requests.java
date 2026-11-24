package com.enrichednow;

import static com.enrichednow.ApiRequest.segment;

import com.enrichednow.ApiRequest.Query;
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
import com.enrichednow.models.RowFeedback;
import com.enrichednow.models.Run;
import com.enrichednow.models.SavedFormula;
import com.enrichednow.models.Scraper;
import com.enrichednow.models.ScraperDetail;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import com.enrichednow.models.SeenReset;
import com.enrichednow.models.Snapshot;
import com.enrichednow.models.Suppression;
import com.enrichednow.models.SuppressionUpload;
import com.enrichednow.models.Usage;
import com.enrichednow.models.WebhookDelivery;
import com.enrichednow.models.WebhookEndpoint;
import com.enrichednow.models.WebhookEndpointList;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/** Every endpoint the SDK calls, described once; the blocking and the asynchronous services both send these. */
final class Requests {
    /** On top of `wait`, how long a request that holds on for a run is allowed. */
    private static final Duration WAIT_MARGIN = Duration.ofSeconds(30);
    /** The most entries one call adds to the suppression list. */
    static final int MAX_SUPPRESSIONS = 10_000;

    private final Transport transport;

    Requests(Transport transport) {
        this.transport = transport;
    }

    JavaType mapType() {
        return transport.mapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, Object.class);
    }

    JavaType type(Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        return transport.mapper.getTypeFactory().constructType(type);
    }

    // --- service, scrapers ------------------------------------------------------------------------------------------

    ApiRequest<Health> health() {
        return ApiRequest.get("/v1/health", null, Health.class);
    }

    ApiRequest<List<Scraper>> scrapers() {
        JavaType scraper = type(Scraper.class);
        return ApiRequest.get("/v1/scrapers", null,
                (mapper, body) -> Collections.unmodifiableList(Requests.<Scraper>items(mapper, mapper.readTree(body), "scrapers", scraper)));
    }

    ApiRequest<ScraperDetail> scraper(String scraperId) {
        return ApiRequest.get("/v1/scrapers/" + segment(scraperId, "scraperId"), null, ScraperDetail.class);
    }

    // --- runs ---------------------------------------------------------------------------------------------------------

    /** Safe to repeat: it carries an idempotency key (the caller's, or one made here), so a second POST starts nothing. */
    ApiRequest<Run> startRun(String scraperId, Object input, RunOptions options) {
        Duration wait = options == null ? null : options.waitDuration();
        Query query = new Query().add("wait", wait == null ? null : seconds(wait));
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("input", input == null ? Collections.emptyMap() : input);
        if (options != null && options.webhookUrl() != null) {
            body.put("webhook_url", options.webhookUrl());
        }
        String key = options == null || options.idempotencyKey() == null ? UUID.randomUUID().toString() : options.idempotencyKey();
        return ApiRequest.post("/v1/scrapers/" + segment(scraperId, "scraperId") + "/runs", query, body, false,
                wait == null ? null : wait.plus(WAIT_MARGIN), Run.class).withIdempotencyKey(key);
    }

    static String seconds(Duration duration) {
        return BigDecimal.valueOf(duration.toMillis()).movePointLeft(3).stripTrailingZeros().toPlainString();
    }

    ApiRequest<Run> run(String runId) {
        return ApiRequest.get("/v1/runs/" + segment(runId, "runId"), null, Run.class);
    }

    ApiRequest<Run> run(String runId, Duration wait) {
        RunOptions.checkWait(wait);
        if (wait == null || wait.isZero()) {
            return run(runId);
        }
        return ApiRequest.get("/v1/runs/" + segment(runId, "runId"), new Query().add("wait", seconds(wait)), wait.plus(WAIT_MARGIN), Run.class);
    }

    ApiRequest<RecordsExport> export(String runId, RecordFormat format, Integer limit, String cursor) {
        if (format == null) {
            throw new IllegalArgumentException("format must not be null");
        }
        Query query = new Query().add("format", format.value()).add("limit", limit).add("cursor", cursor);
        return ApiRequest.raw("/v1/runs/" + segment(runId, "runId") + "/records", query, format.mediaType(),
                (body, headers) -> new RecordsExport(format, body, headers.firstValue("x-next-cursor").orElse(null),
                        "true".equals(headers.firstValue("x-records-expired").orElse(null))));
    }

    ApiRequest<byte[]> file(String runId, String name) {
        return ApiRequest.raw("/v1/runs/" + segment(runId, "runId") + "/files/" + segment(name, "name"), null, "*/*", (body, headers) -> body);
    }

    /** The smallest call whose answer carries the account's standing. */
    ApiRequest<Void> standing() {
        return ApiRequest.raw("/v1/runs", new Query().add("limit", 1), "application/json", (body, headers) -> null);
    }

    // --- webhooks -----------------------------------------------------------------------------------------------------

    ApiRequest<WebhookEndpointList> webhookEndpoints() {
        JavaType endpoint = type(WebhookEndpoint.class);
        return ApiRequest.get("/v1/webhooks", null, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new WebhookEndpointList(Requests.<WebhookEndpoint>items(mapper, root, "endpoints", endpoint), root.path("run_secret_set").asBoolean(false));
        });
    }

    /** Not sent again after a failure that may have reached the API: one call never makes two endpoints. */
    ApiRequest<WebhookEndpoint> createWebhook(String url, List<String> events, String description) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("url", url);
        body.put("events", events);
        if (description != null) {
            body.put("description", description);
        }
        return ApiRequest.of("POST", "/v1/webhooks", body, false, (mapper, bytes) -> mapper.readValue(bytes, WebhookEndpoint.class));
    }

    ApiRequest<WebhookEndpoint> webhook(String endpointId) {
        return ApiRequest.get("/v1/webhooks/" + segment(endpointId, "endpointId"), null, WebhookEndpoint.class);
    }

    ApiRequest<WebhookEndpoint> updateWebhook(String endpointId, WebhookChange change) {
        if (change == null || change.body().isEmpty()) {
            throw new IllegalArgumentException("give at least one of url, events, description, enabled");
        }
        return ApiRequest.of("PATCH", "/v1/webhooks/" + segment(endpointId, "endpointId"), change.body(), true,
                (mapper, bytes) -> mapper.readValue(bytes, WebhookEndpoint.class));
    }

    ApiRequest<Void> deleteWebhook(String endpointId) {
        return ApiRequest.of("DELETE", "/v1/webhooks/" + segment(endpointId, "endpointId"), null, true, (mapper, bytes) -> null);
    }

    ApiRequest<WebhookEndpoint> rotateWebhookSecret(String endpointId) {
        return ApiRequest.of("POST", "/v1/webhooks/" + segment(endpointId, "endpointId") + "/rotate-secret", null, false,
                (mapper, bytes) -> mapper.readValue(bytes, WebhookEndpoint.class));
    }

    ApiRequest<String> createRunSecret() {
        return ApiRequest.of("POST", "/v1/webhooks/run-secret", null, false, (mapper, bytes) -> mapper.readTree(bytes).path("secret").asText(null));
    }

    ApiRequest<Page<WebhookDelivery>> deliveries(WebhookDeliveryListParams given, String cursor) {
        WebhookDeliveryListParams params = given == null ? WebhookDeliveryListParams.none() : given;
        Query query = new Query()
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor)
                .add("endpoint_id", params.endpointId)
                .add("run_id", params.runId)
                .add("status", params.status);
        JavaType delivery = type(WebhookDelivery.class);
        return ApiRequest.get("/v1/webhooks/deliveries", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<WebhookDelivery>items(mapper, root, "deliveries", delivery), cursorOf(root), transport,
                    next -> deliveries(params, next));
        });
    }

    ApiRequest<WebhookDelivery> resendDelivery(String deliveryId) {
        return ApiRequest.of("POST", "/v1/webhooks/deliveries/" + segment(deliveryId, "deliveryId") + "/resend", null, false,
                (mapper, bytes) -> mapper.readValue(bytes, WebhookDelivery.class));
    }

    /** Idempotent: a run that has ended is returned unchanged. */
    ApiRequest<Run> cancelRun(String runId) {
        return ApiRequest.post("/v1/runs/" + segment(runId, "runId") + "/cancel", null, null, true, null, Run.class);
    }

    ApiRequest<Page<Run>> runs(RunListParams given, String cursor) {
        RunListParams params = given == null ? RunListParams.none() : given;
        Query query = new Query()
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor)
                .add("scraper_id", params.scraperId)
                .add("status", params.status == null ? null : params.status.value())
                .add("api_key_id", params.apiKeyId)
                .add("from", params.from)
                .add("to", params.to);
        JavaType run = type(Run.class);
        return ApiRequest.get("/v1/runs", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<Run>items(mapper, root, "runs", run), cursorOf(root), transport, next -> runs(params, next));
        });
    }

    <T> ApiRequest<RecordPage<T>> records(String runId, RecordListParams given, JavaType type, String cursor) {
        RecordListParams params = given == null ? RecordListParams.none() : given;
        Query query = new Query().add("limit", params.limit).add("cursor", cursor != null ? cursor : params.cursor);
        return ApiRequest.get("/v1/runs/" + segment(runId, "runId") + "/records", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new RecordPage<>(Requests.<T>items(mapper, root, "records", type), cursorOf(root), root.path("expired").asBoolean(false),
                    transport, next -> this.<T>records(runId, params, type, next));
        });
    }

    // --- usage --------------------------------------------------------------------------------------------------------

    ApiRequest<Usage> usage(UsageParams given) {
        UsageParams params = given == null ? UsageParams.none() : given;
        Query query = new Query()
                .add("from", params.from)
                .add("to", params.to)
                .add("group_by", params.groupBy == null ? null : params.groupBy.value)
                .add("product", params.product == null ? null : params.product.value);
        return ApiRequest.get("/v1/usage", query, Usage.class);
    }

    // --- datasets -----------------------------------------------------------------------------------------------------

    ApiRequest<Page<Posting>> postings(PostingListParams given, String cursor) {
        PostingListParams params = given == null ? PostingListParams.none() : given;
        return postingPage("/v1/data/postings", params.query(true, cursor), next -> postings(params, next));
    }

    ApiRequest<Page<Posting>> companyPostings(String domain, PostingListParams given, String cursor) {
        PostingListParams params = given == null ? PostingListParams.none() : given;
        if (params.companyDomain != null) {
            throw new IllegalArgumentException("companyDomain is not a filter of a company's postings: the company is named by its domain");
        }
        return postingPage("/v1/data/companies/" + segment(domain, "domain") + "/postings", params.query(false, cursor),
                next -> companyPostings(domain, params, next));
    }

    private ApiRequest<Page<Posting>> postingPage(String path, Query query,
                                                  java.util.function.Function<String, ApiRequest<Page<Posting>>> following) {
        JavaType posting = type(Posting.class);
        return ApiRequest.get(path, query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<Posting>items(mapper, root, "postings", posting), cursorOf(root), transport, following);
        });
    }

    ApiRequest<PostingDetail> posting(String postingId) {
        return ApiRequest.get("/v1/data/postings/" + segment(postingId, "postingId"), null, PostingDetail.class);
    }

    ApiRequest<Page<Company>> companies(CompanyListParams given, String cursor) {
        CompanyListParams params = given == null ? CompanyListParams.none() : given;
        Query query = new Query()
                .add("q", params.q)
                .add("ats", params.ats)
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor);
        JavaType company = type(Company.class);
        return ApiRequest.get("/v1/data/companies", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<Company>items(mapper, root, "companies", company), cursorOf(root), transport,
                    next -> companies(params, next));
        });
    }

    ApiRequest<Company> company(String domain) {
        return ApiRequest.get("/v1/data/companies/" + segment(domain, "domain"), null, Company.class);
    }

    // --- formulas -----------------------------------------------------------------------------------------------------

    /**
     * What is sent as {@code params}: a typed {@link FormulaParams} as its map, once it is known to be the parameters of
     * {@code formulaId} (when that is given); null as no parameters; anything else as it is.
     */
    static Object params(String formulaId, Object params) {
        if (params == null) {
            return Collections.emptyMap();
        }
        if (params instanceof FormulaParams) {
            FormulaParams typed = (FormulaParams) params;
            if (formulaId != null && !formulaId.equals(typed.formulaId())) {
                throw new IllegalArgumentException("These are the parameters of " + typed.formulaId() + ", not of " + formulaId + ".");
            }
            return typed.toMap();
        }
        return params;
    }

    ApiRequest<List<Formula>> formulas(FormulaListParams given) {
        FormulaListParams params = given == null ? FormulaListParams.none() : given;
        Query query = new Query()
                .add("query", params.query)
                .add("category", params.category)
                .add("persona", params.persona)
                .add("status", params.status);
        JavaType formula = type(Formula.class);
        return ApiRequest.get("/v1/formulas", query,
                (mapper, body) -> Collections.unmodifiableList(Requests.<Formula>items(mapper, mapper.readTree(body), "formulas", formula)));
    }

    ApiRequest<FormulaDetails> formula(String formulaId) {
        return ApiRequest.get("/v1/formulas/" + segment(formulaId, "formulaId"), null, FormulaDetails.class);
    }

    /** Spends nothing, so it is safe to send again. */
    ApiRequest<FormulaEstimate> estimateFormula(String formulaId, Object params, FormulaRunOptions options) {
        FormulaRunOptions run = options == null ? FormulaRunOptions.none() : options;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("params", params(formulaId, params));
        caps(body, run);
        return ApiRequest.send("POST", "/v1/formulas/" + segment(formulaId, "formulaId") + "/estimate", null, body, true, null,
                FormulaEstimate.class);
    }

    /** Safe to repeat: it carries an idempotency key (the caller's, or one made here), so a second POST starts nothing. */
    ApiRequest<FormulaRun> startFormulaRun(String formulaId, Object params, FormulaRunOptions options) {
        FormulaRunOptions run = options == null ? FormulaRunOptions.none() : options;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("params", params(formulaId, params));
        caps(body, run);
        return keyed(ApiRequest.send("POST", "/v1/formulas/" + segment(formulaId, "formulaId") + "/runs", waitQuery(run.waitDuration()), body,
                false, waitTimeout(run.waitDuration()), FormulaRun.class), run.idempotencyKey());
    }

    /** The caps and the mode, each only when set: the API's defaults are not sent. */
    private static void caps(Map<String, Object> body, FormulaRunOptions run) {
        if (run.unitCap() != null) {
            body.put("unit_cap", run.unitCap());
        }
        if (run.rowCap() != null) {
            body.put("row_cap", run.rowCap());
        }
        if (run.isPreview()) {
            body.put("mode", "preview");
        }
    }

    private static Query waitQuery(Duration wait) {
        return new Query().add("wait", wait == null ? null : seconds(wait));
    }

    private static Duration waitTimeout(Duration wait) {
        return wait == null ? null : wait.plus(WAIT_MARGIN);
    }

    private static <T> ApiRequest<T> keyed(ApiRequest<T> request, String idempotencyKey) {
        return request.withIdempotencyKey(idempotencyKey == null ? UUID.randomUUID().toString() : idempotencyKey);
    }

    ApiRequest<Page<FormulaRun>> formulaRuns(FormulaRunListParams given, String cursor) {
        FormulaRunListParams params = given == null ? FormulaRunListParams.none() : given;
        Query query = new Query()
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor)
                .add("formula_id", params.formulaId)
                .add("saved_formula_id", params.savedFormulaId)
                .add("status", params.status == null ? null : params.status.value());
        JavaType run = type(FormulaRun.class);
        return ApiRequest.get("/v1/formula-runs", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<FormulaRun>items(mapper, root, "runs", run), cursorOf(root), transport,
                    next -> formulaRuns(params, next));
        });
    }

    ApiRequest<FormulaRun> formulaRun(String runId) {
        return ApiRequest.get("/v1/formula-runs/" + segment(runId, "runId"), null, FormulaRun.class);
    }

    ApiRequest<FormulaRun> formulaRun(String runId, Duration wait) {
        RunOptions.checkWait(wait);
        if (wait == null || wait.isZero()) {
            return formulaRun(runId);
        }
        return ApiRequest.get("/v1/formula-runs/" + segment(runId, "runId"), waitQuery(wait), waitTimeout(wait), FormulaRun.class);
    }

    /** Idempotent: a run that has ended is returned unchanged. */
    ApiRequest<FormulaRun> cancelFormulaRun(String runId) {
        return ApiRequest.send("POST", "/v1/formula-runs/" + segment(runId, "runId") + "/cancel", null, null, true, null, FormulaRun.class);
    }

    ApiRequest<Page<FormulaRow>> formulaRows(String runId, FormulaRowListParams given, String cursor) {
        FormulaRowListParams params = runRows(given);
        String path = "/v1/formula-runs/" + segment(runId, "runId") + "/rows";
        return rowPage(path, rowQuery(params, cursor, null), next -> formulaRows(runId, params, next));
    }

    ApiRequest<RecordsExport> exportFormulaRows(String runId, RecordFormat format, FormulaRowListParams given, String cursor) {
        FormulaRowListParams params = runRows(given);
        return rowExport("/v1/formula-runs/" + segment(runId, "runId") + "/rows", format, params, cursor);
    }

    private static FormulaRowListParams runRows(FormulaRowListParams given) {
        FormulaRowListParams params = given == null ? FormulaRowListParams.none() : given;
        if (params.since != null || params.order != null) {
            throw new IllegalArgumentException("since and order are for a saved formula's rows, not a run's");
        }
        return params;
    }

    private static FormulaRowListParams savedRows(FormulaRowListParams given) {
        FormulaRowListParams params = given == null ? FormulaRowListParams.none() : given;
        if (params.view != null) {
            throw new IllegalArgumentException("view is for a run's rows, not a saved formula's");
        }
        return params;
    }

    private static Query rowQuery(FormulaRowListParams params, String cursor, RecordFormat format) {
        return new Query()
                .add("format", format == null ? null : format.value())
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor)
                .add("fields", params.fieldList())
                .add("min_score", params.minScore == null ? null : number(params.minScore))
                .add("view", params.view == null ? null : params.view.value)
                .add("since", params.since == null ? null : time(params.since))
                .add("order", params.order == null ? null : params.order.value);
    }

    private ApiRequest<Page<FormulaRow>> rowPage(String path, Query query, java.util.function.Function<String, ApiRequest<Page<FormulaRow>>> following) {
        JavaType row = type(FormulaRow.class);
        return ApiRequest.get(path, query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<FormulaRow>items(mapper, root, "rows", row), cursorOf(root), transport, following);
        });
    }

    private static ApiRequest<RecordsExport> rowExport(String path, RecordFormat format, FormulaRowListParams params, String cursor) {
        if (format == null) {
            throw new IllegalArgumentException("format must not be null");
        }
        return ApiRequest.raw(path, rowQuery(params, cursor, format), format.mediaType(),
                (body, headers) -> new RecordsExport(format, body, headers.firstValue("x-next-cursor").orElse(null), false));
    }

    /** A number as the API reads it in a query: {@code 10}, not {@code 10.0}. */
    static String number(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    /** A time in ISO 8601, at UTC to the microsecond, as the API writes its own: {@code 2026-11-25T09:02:55.250000+00:00}. */
    static String time(Instant instant) {
        OffsetDateTime utc = instant.atOffset(ZoneOffset.UTC);
        StringBuilder text = new StringBuilder(utc.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss", Locale.ROOT)));
        int micros = utc.getNano() / 1000;
        if (micros != 0) {
            text.append(String.format(Locale.ROOT, ".%06d", micros));
        }
        return text.append("+00:00").toString();
    }

    // --- saved formulas -----------------------------------------------------------------------------------------------

    ApiRequest<Page<SavedFormula>> savedFormulas(SavedFormulaListParams given, String cursor) {
        SavedFormulaListParams params = given == null ? SavedFormulaListParams.none() : given;
        Query query = new Query()
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor)
                .add("formula_id", params.formulaId)
                .add("status", params.status == null ? null : params.status.value);
        JavaType saved = type(SavedFormula.class);
        return ApiRequest.get("/v1/saved-formulas", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<SavedFormula>items(mapper, root, "saved_formulas", saved), cursorOf(root), transport,
                    next -> savedFormulas(params, next));
        });
    }

    ApiRequest<PlanLimits> savedFormulaLimits() {
        return ApiRequest.get("/v1/saved-formulas/limits", null, PlanLimits.class);
    }

    /** Not sent again after a failure that may have reached the API: one call never saves two. */
    ApiRequest<SavedFormula> createSavedFormula(SavedFormulaCreate create) {
        if (create == null) {
            throw new IllegalArgumentException("create must not be null");
        }
        return ApiRequest.send("POST", "/v1/saved-formulas", null, create.body(), false, null, SavedFormula.class);
    }

    ApiRequest<SavedFormula> savedFormula(String savedFormulaId) {
        return ApiRequest.get(savedPath(savedFormulaId), null, SavedFormula.class);
    }

    /** The same change twice is one change, so it is safe to send again. */
    ApiRequest<SavedFormula> updateSavedFormula(String savedFormulaId, SavedFormulaChange change) {
        if (change == null || change.body().isEmpty()) {
            throw new IllegalArgumentException("give at least one of name, params, resetSeen, unitCap, rowCap, monthlyUnitCap, schedule, notify");
        }
        return ApiRequest.send("PATCH", savedPath(savedFormulaId), null, change.body(), true, null, SavedFormula.class);
    }

    ApiRequest<SavedFormula> archiveSavedFormula(String savedFormulaId) {
        return ApiRequest.send("DELETE", savedPath(savedFormulaId), null, null, true, null, SavedFormula.class);
    }

    /** Not sent again after a failure that may have reached the API: one call never makes two copies. */
    ApiRequest<SavedFormula> duplicateSavedFormula(String savedFormulaId, DuplicateOptions options) {
        Map<String, Object> body = (options == null ? DuplicateOptions.none() : options).body();
        return ApiRequest.send("POST", savedPath(savedFormulaId) + "/duplicate", null, body, false, null, SavedFormula.class);
    }

    /** Safe to repeat: it carries an idempotency key, as starting a formula run does. */
    ApiRequest<FormulaRun> startSavedFormulaRun(String savedFormulaId, SavedFormulaRunOptions options) {
        SavedFormulaRunOptions run = options == null ? SavedFormulaRunOptions.none() : options;
        Map<String, Object> body = new LinkedHashMap<>();
        if (run.isPreview()) {
            body.put("mode", "preview");
        }
        if (run.includeSeen()) {
            body.put("include_seen", true);
        }
        return keyed(ApiRequest.send("POST", savedPath(savedFormulaId) + "/runs", waitQuery(run.waitDuration()), body, false,
                waitTimeout(run.waitDuration()), FormulaRun.class), run.idempotencyKey());
    }

    ApiRequest<SavedFormula> pauseSavedFormula(String savedFormulaId) {
        return ApiRequest.send("POST", savedPath(savedFormulaId) + "/pause", null, null, true, null, SavedFormula.class);
    }

    ApiRequest<SavedFormula> resumeSavedFormula(String savedFormulaId) {
        return ApiRequest.send("POST", savedPath(savedFormulaId) + "/resume", null, null, true, null, SavedFormula.class);
    }

    /** Not sent again after a failure that may have reached the API, so that {@code cleared} counts what it forgot. */
    ApiRequest<SeenReset> resetSeen(String savedFormulaId) {
        return ApiRequest.send("POST", savedPath(savedFormulaId) + "/seen/reset", null, null, false, null, SeenReset.class);
    }

    ApiRequest<Page<FormulaRow>> savedFormulaRows(String savedFormulaId, FormulaRowListParams given, String cursor) {
        FormulaRowListParams params = savedRows(given);
        return rowPage(savedPath(savedFormulaId) + "/rows", rowQuery(params, cursor, null), next -> savedFormulaRows(savedFormulaId, params, next));
    }

    ApiRequest<RecordsExport> exportSavedFormulaRows(String savedFormulaId, RecordFormat format, FormulaRowListParams given, String cursor) {
        return rowExport(savedPath(savedFormulaId) + "/rows", format, savedRows(given), cursor);
    }

    ApiRequest<List<Snapshot>> snapshots(String savedFormulaId, String entityKey, Integer limit) {
        JavaType snapshot = type(Snapshot.class);
        return ApiRequest.get(savedPath(savedFormulaId) + "/snapshots", new Query().add("entity_key", entityKey).add("limit", limit),
                (mapper, body) -> Collections.unmodifiableList(Requests.<Snapshot>items(mapper, mapper.readTree(body), "snapshots", snapshot)));
    }

    private static String savedPath(String savedFormulaId) {
        return "/v1/saved-formulas/" + segment(savedFormulaId, "savedFormulaId");
    }

    // --- suppressions, attestations, row feedback ---------------------------------------------------------------------

    ApiRequest<Page<Suppression>> suppressions(SuppressionListParams given, String cursor) {
        SuppressionListParams params = given == null ? SuppressionListParams.none() : given;
        Query query = new Query()
                .add("kind", params.kind == null ? null : params.kind.value())
                .add("limit", params.limit)
                .add("cursor", cursor != null ? cursor : params.cursor);
        JavaType suppression = type(Suppression.class);
        return ApiRequest.get("/v1/suppressions", query, (mapper, body) -> {
            JsonNode root = mapper.readTree(body);
            return new Page<>(Requests.<Suppression>items(mapper, root, "suppressions", suppression), cursorOf(root), transport,
                    next -> suppressions(params, next));
        });
    }

    /** An entry the list already holds is left as it is, so it is safe to send again. */
    ApiRequest<Integer> addSuppressions(List<SuppressionEntry> entries, String source) {
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("entries must hold at least one entry");
        }
        if (entries.size() > MAX_SUPPRESSIONS) {
            throw new IllegalArgumentException("at most " + MAX_SUPPRESSIONS + " entries a call: split them, or upload a CSV file");
        }
        List<Map<String, Object>> list = new ArrayList<>(entries.size());
        for (SuppressionEntry entry : entries) {
            list.add(java.util.Objects.requireNonNull(entry, "entry").toMap());
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("entries", list);
        if (source != null) {
            body.put("source", source);
        }
        return ApiRequest.send("POST", "/v1/suppressions", null, body, true, (mapper, bytes) -> mapper.readTree(bytes).path("added").asInt());
    }

    /** An entry the list already holds is left as it is, so it is safe to send again: the repeat counts it as {@code already}. */
    ApiRequest<SuppressionUpload> uploadSuppressions(byte[] csv, SuppressionKind kind) {
        if (csv == null || csv.length == 0) {
            throw new IllegalArgumentException("the CSV file is empty");
        }
        return ApiRequest.send("POST", "/v1/suppressions/upload", new Query().add("kind", kind == null ? null : kind.value()), null, true,
                (mapper, bytes) -> mapper.readValue(bytes, SuppressionUpload.class)).withRawBody(csv, "text/csv");
    }

    ApiRequest<Void> deleteSuppression(SuppressionKind kind, String value) {
        if (kind == null) {
            throw new IllegalArgumentException("kind must not be null");
        }
        return ApiRequest.send("DELETE", "/v1/suppressions/" + kind.value() + "/" + segment(value, "value"), null, null, true,
                (mapper, bytes) -> null);
    }

    ApiRequest<List<Attestation>> attestations() {
        JavaType attestation = type(Attestation.class);
        return ApiRequest.get("/v1/attestations", null,
                (mapper, body) -> Collections.unmodifiableList(Requests.<Attestation>items(mapper, mapper.readTree(body), "attestations", attestation)));
    }

    /** The same verdict twice is one, so it is safe to send again. */
    ApiRequest<RowFeedback> rowFeedback(String rowId, RowVerdict verdict, String note, String correctDomain) {
        if (verdict == null) {
            throw new IllegalArgumentException("verdict must not be null");
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("verdict", verdict.value());
        if (note != null) {
            body.put("note", note);
        }
        if (correctDomain != null) {
            body.put("correct_domain", correctDomain);
        }
        return ApiRequest.send("POST", "/v1/formula-rows/" + segment(rowId, "rowId") + "/feedback", null, body, true, null, RowFeedback.class);
    }

    // --- reading a page -----------------------------------------------------------------------------------------------

    private static <T> List<T> items(ObjectMapper mapper, JsonNode root, String field, JavaType type) throws IOException {
        JsonNode array = root.path(field);
        if (!array.isArray()) {
            throw new IOException("the answer has no list `" + field + "`");
        }
        List<T> items = new ArrayList<>(array.size());
        for (JsonNode item : array) {
            items.add(mapper.readValue(mapper.treeAsTokens(item), type));
        }
        return items;
    }

    private static String cursorOf(JsonNode root) {
        JsonNode cursor = root.path("next_cursor");
        return cursor.isTextual() && !cursor.asText().isEmpty() ? cursor.asText() : null;
    }
}
