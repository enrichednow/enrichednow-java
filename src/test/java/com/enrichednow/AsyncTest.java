package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.errors.NotFoundException;
import com.enrichednow.errors.RunFailedException;
import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.errors.ServerException;
import com.enrichednow.models.Posting;
import com.enrichednow.models.Run;
import com.enrichednow.models.RunStatus;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AsyncTest {
    private static final Map<String, Object> INPUT = Map.of("domain", "acme.com");
    private static final WaitOptions FAST = WaitOptions.builder().pollInterval(Duration.ofMillis(5)).timeout(Duration.ofSeconds(5)).build();

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    private static <T> T get(CompletableFuture<T> future) throws Exception {
        return future.get(10, TimeUnit.SECONDS);
    }

    private static Throwable failure(CompletableFuture<?> future) {
        return assertThrows(CompletionException.class, () -> future.orTimeout(10, TimeUnit.SECONDS).join()).getCause();
    }

    @Test
    void theAsyncCallsSendTheSameRequests() throws Exception {
        EnrichedNowAsyncClient async = api.client().async();

        api.ok("{\"status\":\"ok\"}");
        assertTrue(get(async.health()).isOk());
        assertEquals("GET /v1/health", api.last().toString());
        assertEquals("Bearer " + FakeApi.KEY, api.last().header("Authorization"));

        api.ok("{\"scrapers\":[]}");
        assertTrue(get(async.scrapers().list()).isEmpty());
        assertEquals("GET /v1/scrapers", api.last().toString());

        api.reply(202, FakeApi.run("run_1", "queued", null));
        assertEquals("run_1", get(async.scrapers().run("careers.company", INPUT, RunOptions.waitUpTo(Duration.ofSeconds(5)))).id());
        assertEquals("POST /v1/scrapers/careers.company/runs?wait=5", api.last().toString());
        assertEquals("{\"input\":{\"domain\":\"acme.com\"}}", api.last().body);

        api.ok(FakeApi.run("run_1", "running", null));
        assertEquals(RunStatus.RUNNING, get(async.runs().cancel("run_1")).status());
        assertEquals("POST /v1/runs/run_1/cancel", api.last().toString());

        api.ok("{\"period\":{\"from\":\"2026-11-01\",\"to\":\"2026-11-01\"},\"total_units\":0,\"allowance_units\":10,\"rows\":[]}");
        assertEquals(10, get(async.usage().get(UsageParams.builder().groupBy(UsageParams.GroupBy.PRODUCT).build())).allowanceUnits());
        assertEquals("GET /v1/usage?group_by=product", api.last().toString());

        api.ok(FakeApi.company("acme.com"));
        assertEquals("acme.com", get(async.data().companies().get("acme.com")).domain());
        assertEquals("GET /v1/data/companies/acme.com", api.last().toString());
    }

    @Test
    void pagesAreWalkedAsynchronously() throws Exception {
        api.ok("{\"postings\":[" + FakeApi.posting("p_1") + "],\"next_cursor\":\"n1\"}");
        api.ok("{\"postings\":[" + FakeApi.posting("p_2") + "],\"next_cursor\":null}");
        Page<Posting> first = get(api.client().async().data().postings().listPage(PostingListParams.builder().status("open").build()));
        Page<Posting> second = get(first.nextPageAsync());
        assertEquals("p_2", second.items().get(0).id());
        assertEquals("GET /v1/data/postings?status=open&cursor=n1", api.last().toString());
        assertFalse(second.hasNextPage());

        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":null}");
        RecordPage<Map<String, Object>> records = get(api.client().async().runs().recordsPage("run_1", RecordListParams.limit(10)));
        assertEquals("A", records.items().get(0).get("title"));
        assertEquals("GET /v1/runs/run_1/records?limit=10", api.last().toString());
    }

    @Test
    void anErrorFailsTheFutureWithTheSameException() {
        api.error(404, "not_found", "There is no run 'run_x'.");
        NotFoundException e = assertInstanceOf(NotFoundException.class, failure(api.client().async().runs().get("run_x")));
        assertEquals("not_found", e.code());
    }

    @Test
    void asyncReadsAreRetriedAndAsyncStartsAreNot() throws Exception {
        api.error(500, "internal", "x").error(500, "internal", "x").ok(FakeApi.run("run_1", "ok", null));
        assertEquals("run_1", get(api.client().async().runs().get("run_1")).id());
        assertEquals(3, api.count());

        api.error(500, "internal", "x");
        assertInstanceOf(ServerException.class, failure(api.client().async().scrapers().run("careers.company", INPUT)));
        assertEquals(4, api.count());
    }

    @Test
    void asyncWaitForAsksUntilTheRunHasEnded() throws Exception {
        api.ok(FakeApi.run("run_1", "queued", null)).ok(FakeApi.run("run_1", "running", null)).ok(FakeApi.run("run_1", "ok", null));
        Run run = get(api.client().async().runs().waitFor("run_1", FAST));
        assertEquals(RunStatus.OK, run.status());
        assertEquals(3, api.count());
    }

    @Test
    void asyncWaitForGivesUp() {
        for (int i = 0; i < 50; i++) {
            api.ok(FakeApi.run("run_1", "running", null));
        }
        WaitOptions brief = WaitOptions.builder().timeout(Duration.ofMillis(60)).pollInterval(Duration.ofMillis(20)).build();
        assertInstanceOf(RunTimeoutException.class, failure(api.client().async().runs().waitFor("run_1", brief)));
    }

    @Test
    void asyncScrapeWaitsAndReturnsTheResult() throws Exception {
        api.reply(202, FakeApi.run("run_1", "running", null)).ok(FakeApi.run("run_1", "ok", null));
        ScrapeOptions options = ScrapeOptions.builder().pollInterval(Duration.ofMillis(5)).build();
        ScrapeResult result = get(api.client().async().scrape("careers.company", INPUT, options));
        assertEquals(RunStatus.OK, result.run().status());
        assertEquals("POST /v1/scrapers/careers.company/runs?wait=60", api.received().get(0).toString());
        assertEquals("GET /v1/runs/run_1", api.received().get(1).toString());

        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":null}");
        assertEquals("A", result.records().toList().get(0).get("title"));
    }

    @Test
    void asyncScrapeFailsTheFutureWhenTheRunDidNotSucceed() {
        api.ok(FakeApi.run("run_1", "failed", "fetch_failed"));
        RunFailedException e = assertInstanceOf(RunFailedException.class, failure(api.client().async().scrape("careers.company", INPUT)));
        assertEquals(RunStatus.FAILED, e.status());
        assertEquals("fetch_failed", e.reason());
    }
}
