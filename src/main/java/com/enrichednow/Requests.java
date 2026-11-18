package com.enrichednow;

import static com.enrichednow.ApiRequest.segment;

import com.enrichednow.ApiRequest.Query;
import com.enrichednow.models.Company;
import com.enrichednow.models.Health;
import com.enrichednow.models.Posting;
import com.enrichednow.models.PostingDetail;
import com.enrichednow.models.Run;
import com.enrichednow.models.Scraper;
import com.enrichednow.models.ScraperDetail;
import com.enrichednow.models.Usage;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Every endpoint the SDK calls, described once; the blocking and the asynchronous services both send these. */
final class Requests {
    /** On top of `wait`, how long a request that holds on for a run is allowed. */
    private static final Duration WAIT_MARGIN = Duration.ofSeconds(30);

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
        Map<String, Object> body = Collections.singletonMap("input", input == null ? Collections.emptyMap() : input);
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
                .add("status", params.status == null ? null : params.status.value());
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
