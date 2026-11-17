package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Received;
import com.enrichednow.errors.RunFailedException;
import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.models.Run;
import com.enrichednow.models.RunStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class RunsTest {
    private static final Map<String, Object> INPUT = Map.of("board_url", "https://boards.greenhouse.io/acme");
    private static final WaitOptions FAST = WaitOptions.builder().pollInterval(Duration.ofMillis(5)).timeout(Duration.ofSeconds(5)).build();

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    /** What a test reads a record as. */
    static final class Job {
        @JsonProperty("title")
        String title;
        @JsonProperty("external_id")
        String externalId;
    }

    // --- starting -----------------------------------------------------------------------------------------------------

    @Test
    void aRunIsStartedWithItsInputUnderInput() {
        api.reply(202, FakeApi.run("run_1", "queued", null));
        Run run = api.client().scrapers().run("ats.board", INPUT);
        Received request = api.only();
        assertEquals("POST /v1/scrapers/ats.board/runs", request.toString());
        assertEquals("{\"input\":{\"board_url\":\"https://boards.greenhouse.io/acme\"}}", request.body);
        assertEquals("application/json", request.header("Content-Type"));
        assertEquals("run_1", run.id());
        assertEquals(RunStatus.QUEUED, run.status());
        assertFalse(run.isFinished());
        assertNull(run.diagnostics());
        assertNull(run.finishedAt());
    }

    @Test
    void waitIsSentInSecondsAndAFinishedRunIsReadWhole() {
        api.ok(FakeApi.run("run_1", "ok", null));
        Run run = api.client().scrapers().run("ats.board", INPUT, RunOptions.waitUpTo(Duration.ofSeconds(60)));
        assertEquals("wait=60", api.only().query);
        assertEquals(RunStatus.OK, run.status());
        assertEquals("ok", run.rawStatus());
        assertTrue(run.isFinished());
        assertTrue(run.isSuccessful());
        assertNull(run.reason());
        assertEquals("ats.board", run.scraperId());
        assertEquals(3, run.scraperVersion());
        assertEquals(INPUT, run.input());
        assertEquals(3, run.recordCount());
        assertEquals(1, run.units());
        assertEquals(4, run.diagnostics().requests());
        assertNull(run.diagnostics().bytes());
        assertEquals(900L, run.diagnostics().wallMs());
        assertEquals("greenhouse", run.diagnostics().facts().get("ats"));
        assertEquals(Instant.parse("2026-11-17T09:30:00Z"), run.createdAt());
        assertEquals(Instant.parse("2026-11-17T09:30:00.123456Z"), run.startedAt());
        assertEquals(Instant.parse("2026-11-17T09:30:01Z"), run.finishedAt());
    }

    @Test
    void aFractionOfASecondOfWaitIsKeptAndTooMuchIsRefused() {
        api.reply(202, FakeApi.run("run_1", "running", null));
        api.client().scrapers().run("ats.board", INPUT, RunOptions.waitUpTo(Duration.ofMillis(500)));
        assertEquals("wait=0.5", api.only().query);
        assertThrows(IllegalArgumentException.class, () -> RunOptions.waitUpTo(Duration.ofSeconds(61)));
    }

    @Test
    void anInputOfYourOwnClassIsWrittenAsJson() {
        api.reply(202, FakeApi.run("run_1", "queued", null));
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("domain", "acme.com");
        input.put("max_detail_pages", 20);
        api.client().scrapers().run("careers.company", input);
        assertEquals("/v1/scrapers/careers.company/runs", api.only().path);
        assertEquals("{\"input\":{\"domain\":\"acme.com\",\"max_detail_pages\":20}}", api.only().body);

        api.reply(202, FakeApi.run("run_2", "queued", null));
        Job job = new Job();
        job.title = "t";
        api.client().scrapers().run("careers.company", job);
        assertEquals("{\"input\":{\"title\":\"t\",\"external_id\":null}}", api.last().body);

        api.reply(202, FakeApi.run("run_3", "queued", null));
        api.client().scrapers().run("careers.company", null);
        assertEquals("{\"input\":{}}", api.last().body);
    }

    @Test
    void aStatusThisSdkDoesNotKnowDoesNotBreakReading() {
        api.ok(FakeApi.run("run_1", "paused", null));
        Run run = api.client().runs().get("run_1");
        assertEquals(RunStatus.UNKNOWN, run.status());
        assertEquals("paused", run.rawStatus());
        assertFalse(run.isFinished());
    }

    // --- reading, listing, cancelling -----------------------------------------------------------------------------------

    @Test
    void oneRunIsRead() {
        api.ok(FakeApi.run("run_9", "failed", "fetch_failed"));
        Run run = api.client().runs().get("run_9");
        assertEquals("GET /v1/runs/run_9", api.only().toString());
        assertEquals(RunStatus.FAILED, run.status());
        assertEquals("fetch_failed", run.reason());
        assertTrue(run.isFinished());
        assertFalse(run.isSuccessful());
    }

    @Test
    void runsAreListedAcrossPagesWithTheSameFilters() {
        api.ok("{\"runs\":[" + FakeApi.run("run_3", "ok", null) + "," + FakeApi.run("run_2", "ok", null) + "],\"next_cursor\":\"c1\"}");
        api.ok("{\"runs\":[" + FakeApi.run("run_1", "ok", null) + "],\"next_cursor\":null}");
        RunListParams params = RunListParams.builder().limit(2).scraperId("ats.board").status(RunStatus.OK).build();

        List<String> ids = new ArrayList<>();
        for (Run run : api.client().runs().list(params)) {
            ids.add(run.id());
        }

        assertEquals(List.of("run_3", "run_2", "run_1"), ids);
        List<Received> requests = api.received();
        assertEquals(2, requests.size());
        assertEquals("GET /v1/runs?limit=2&scraper_id=ats.board&status=ok", requests.get(0).toString());
        assertEquals("GET /v1/runs?limit=2&cursor=c1&scraper_id=ats.board&status=ok", requests.get(1).toString());
    }

    @Test
    void aPageOfRunsCanBeWalkedByHand() {
        api.ok("{\"runs\":[" + FakeApi.run("run_2", "ok", null) + "],\"next_cursor\":\"c1\"}");
        api.ok("{\"runs\":[" + FakeApi.run("run_1", "ok", null) + "],\"next_cursor\":null}");
        Page<Run> first = api.client().runs().listPage(RunListParams.none());
        assertEquals("GET /v1/runs", api.last().toString());
        assertTrue(first.hasNextPage());
        assertEquals("c1", first.nextCursor());
        Page<Run> second = first.nextPage();
        assertEquals("cursor=c1", api.last().query);
        assertEquals("run_1", second.items().get(0).id());
        assertFalse(second.hasNextPage());
        assertNull(second.nextCursor());
        assertThrows(NoSuchElementException.class, second::nextPage);
        assertThrows(UnsupportedOperationException.class, () -> second.items().clear());
    }

    @Test
    void aListCanStartFromACursor() {
        api.ok("{\"runs\":[],\"next_cursor\":null}");
        assertTrue(api.client().runs().list(RunListParams.builder().cursor("c7").build()).toList().isEmpty());
        assertEquals("cursor=c7", api.only().query);
    }

    @Test
    void nothingIsRequestedUntilAListIsIterated() {
        PagedIterable<Run> runs = api.client().runs().list();
        assertEquals(0, api.count());
        api.ok("{\"runs\":[],\"next_cursor\":null}");
        assertFalse(runs.iterator().hasNext());
        assertEquals(1, api.count());
    }

    @Test
    void aRunIsCancelled() {
        api.ok(FakeApi.run("run_1", "running", null));
        Run run = api.client().runs().cancel("run_1");
        Received request = api.only();
        assertEquals("POST /v1/runs/run_1/cancel", request.toString());
        assertEquals("", request.body);
        assertEquals(RunStatus.RUNNING, run.status());
    }

    // --- waiting ------------------------------------------------------------------------------------------------------

    @Test
    void waitForAsksUntilTheRunHasEnded() {
        api.ok(FakeApi.run("run_1", "queued", null));
        api.ok(FakeApi.run("run_1", "running", null));
        api.ok(FakeApi.run("run_1", "partial", "render_required"));
        Run run = api.client().runs().waitFor("run_1", FAST);
        assertEquals(RunStatus.PARTIAL, run.status());
        assertEquals("render_required", run.reason());
        assertEquals(3, api.count());
        assertTrue(api.received().stream().allMatch(r -> r.toString().equals("GET /v1/runs/run_1")));
    }

    @Test
    void waitForReturnsARunThatFailedWithoutThrowing() {
        api.ok(FakeApi.run("run_1", "failed", "no_careers_page"));
        assertEquals(RunStatus.FAILED, api.client().runs().waitFor("run_1", FAST).status());
        assertEquals(1, api.count());
    }

    @Test
    void waitForGivesUpWithTheRunAsItWas() {
        for (int i = 0; i < 50; i++) {
            api.ok(FakeApi.run("run_1", "running", null));
        }
        WaitOptions brief = WaitOptions.builder().timeout(Duration.ofMillis(60)).pollInterval(Duration.ofMillis(20)).build();
        RunTimeoutException e = assertThrows(RunTimeoutException.class, () -> api.client().runs().waitFor("run_1", brief));
        assertEquals("run_1", e.runId());
        assertEquals(RunStatus.RUNNING, e.run().status());
        assertTrue(api.count() >= 2 && api.count() <= 6, "polled " + api.count() + " times");
    }

    // --- records ------------------------------------------------------------------------------------------------------

    @Test
    void recordsArePagedThroughAutomatically() {
        api.ok("{\"records\":[" + FakeApi.record("A") + "," + FakeApi.record("B") + "],\"next_cursor\":\"Mg\"}");
        api.ok("{\"records\":[" + FakeApi.record("C") + "],\"next_cursor\":null}");

        List<String> titles = new ArrayList<>();
        for (Map<String, Object> record : api.client().runs().records("run_1", RecordListParams.limit(2))) {
            titles.add((String) record.get("title"));
            assertNull(record.get("company_domain"));
            assertEquals(false, ((Map<?, ?>) record.get("location")).get("remote"));
        }

        assertEquals(List.of("A", "B", "C"), titles);
        assertEquals("GET /v1/runs/run_1/records?limit=2", api.received().get(0).toString());
        assertEquals("GET /v1/runs/run_1/records?limit=2&cursor=Mg", api.received().get(1).toString());
    }

    @Test
    void recordsAreReadAsYourOwnClass() {
        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":\"Mg\"}");
        api.ok("{\"records\":[" + FakeApi.record("B") + "],\"next_cursor\":null}");
        List<Job> jobs = api.client().runs().records("run_1", Job.class).toList();
        assertEquals(2, jobs.size());
        assertEquals("A", jobs.get(0).title);
        assertEquals("4567890", jobs.get(1).externalId);
        assertEquals("GET /v1/runs/run_1/records", api.received().get(0).toString());
    }

    @Test
    void recordsCanBeReadAsAJsonTree() {
        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":null}");
        JsonNode record = api.client().runs().records("run_1", JsonNode.class).iterator().next();
        assertEquals("Austin, TX", record.path("location").path("raw").asText());
    }

    @Test
    void aStreamOfRecordsStopsFetchingWhenItHasEnough() {
        api.ok("{\"records\":[" + FakeApi.record("A") + "," + FakeApi.record("B") + "],\"next_cursor\":\"Mg\"}");
        List<Object> titles = api.client().runs().records("run_1").stream().limit(2).map(r -> r.get("title")).collect(Collectors.toList());
        assertEquals(List.of("A", "B"), titles);
        assertEquals(1, api.count());
    }

    @Test
    void recordsCanBeReadAPageAtATime() {
        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":\"MQ\"}");
        api.ok("{\"records\":[" + FakeApi.record("B") + "],\"next_cursor\":null}");
        RecordPage<Map<String, Object>> first = api.client().runs().recordsPage("run_1", RecordListParams.builder().limit(1).build());
        assertFalse(first.expired());
        assertEquals("MQ", first.nextCursor());
        RecordPage<Map<String, Object>> second = first.nextPage();
        assertEquals("limit=1&cursor=MQ", api.last().query);
        assertEquals("B", second.items().get(0).get("title"));

        api.ok("{\"records\":[" + FakeApi.record("C") + "],\"next_cursor\":null}");
        RecordPage<Job> typed = api.client().runs().recordsPage("run_1", RecordListParams.builder().cursor("MQ").build(), Job.class);
        assertEquals("cursor=MQ", api.last().query);
        assertEquals("C", typed.items().get(0).title);

        int pages = 0;
        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":\"MQ\"}");
        api.ok("{\"records\":[],\"next_cursor\":null}");
        for (Page<Map<String, Object>> page : api.client().runs().records("run_1").pages()) {
            assertNotNull(page.items());
            pages++;
        }
        assertEquals(2, pages);
    }

    @Test
    void recordsThatAreGoneSaySo() {
        api.ok("{\"records\":[],\"next_cursor\":null,\"expired\":true}");
        RecordPage<Map<String, Object>> page = api.client().runs().recordsPage("run_1", RecordListParams.none());
        assertTrue(page.expired());
        assertTrue(page.items().isEmpty());
        assertFalse(page.hasNextPage());
    }

    // --- the one-liner --------------------------------------------------------------------------------------------------

    @Test
    void scrapeReturnsTheRunAndItsRecordsWhenTheRunEndsWithinTheWait() {
        api.ok(FakeApi.run("run_1", "ok", null));
        api.ok("{\"records\":[" + FakeApi.record("A") + "," + FakeApi.record("B") + "],\"next_cursor\":\"Mg\"}");
        api.ok("{\"records\":[" + FakeApi.record("C") + "],\"next_cursor\":null}");

        ScrapeResult result = api.client().scrape("ats.board", INPUT);

        assertEquals("POST /v1/scrapers/ats.board/runs?wait=60", api.only().toString());
        assertEquals("{\"input\":{\"board_url\":\"https://boards.greenhouse.io/acme\"}}", api.only().body);
        assertEquals(RunStatus.OK, result.run().status());
        List<Object> titles = new ArrayList<>();
        for (Map<String, Object> record : result.records()) {
            titles.add(record.get("title"));
        }
        assertEquals(List.of("A", "B", "C"), titles);
        assertEquals("GET /v1/runs/run_1/records", api.received().get(1).toString());
        assertEquals("GET /v1/runs/run_1/records?cursor=Mg", api.received().get(2).toString());

        api.ok("{\"records\":[" + FakeApi.record("A") + "],\"next_cursor\":null}");
        assertEquals("A", result.records(Job.class).toList().get(0).title);
    }

    @Test
    void scrapeKeepsAskingWhenTheWaitRanOut() {
        api.reply(202, FakeApi.run("run_1", "running", null));
        api.ok(FakeApi.run("run_1", "running", null));
        api.ok(FakeApi.run("run_1", "partial", "render_required"));
        ScrapeOptions options = ScrapeOptions.builder().pollInterval(Duration.ofMillis(5)).timeout(Duration.ofSeconds(20)).build();

        ScrapeResult result = api.client().scrape("ats.board", INPUT, options);

        assertEquals(RunStatus.PARTIAL, result.run().status());
        List<Received> requests = api.received();
        assertEquals(3, requests.size());
        assertEquals("POST /v1/scrapers/ats.board/runs?wait=20", requests.get(0).toString());
        assertEquals("GET /v1/runs/run_1", requests.get(1).toString());
        assertEquals("GET /v1/runs/run_1", requests.get(2).toString());
    }

    @Test
    void scrapeThrowsATypedExceptionWhenTheRunDidNotSucceed() {
        for (String[] ending : new String[][] {{"failed", "fetch_failed"}, {"unsupported", "render_required"}, {"cancelled", "cancelled"}}) {
            api.ok(FakeApi.run("run_1", ending[0], ending[1]));
            RunFailedException e = assertThrows(RunFailedException.class, () -> api.client().scrape("ats.board", INPUT));
            assertEquals(RunStatus.fromValue(ending[0]), e.status());
            assertEquals(ending[1], e.reason());
            assertEquals("run_1", e.runId());
            assertEquals("run_1", e.run().id());
            assertTrue(e.getMessage().contains(ending[0]) && e.getMessage().contains(ending[1]), e.getMessage());
        }
    }

    @Test
    void scrapeCanBeToldNotToThrow() {
        api.ok(FakeApi.run("run_1", "unsupported", "render_required"));
        ScrapeResult result = api.client().scrape("ats.board", INPUT, ScrapeOptions.builder().throwOnFailure(false).build());
        assertEquals(RunStatus.UNSUPPORTED, result.run().status());
        assertEquals("render_required", result.run().reason());
    }

    @Test
    void scrapeGivesUpWhenTheRunDoesNotEndInTime() {
        api.reply(202, FakeApi.run("run_1", "queued", null));
        for (int i = 0; i < 50; i++) {
            api.ok(FakeApi.run("run_1", "running", null));
        }
        ScrapeOptions options = ScrapeOptions.builder().timeout(Duration.ofMillis(80)).pollInterval(Duration.ofMillis(20)).build();
        RunTimeoutException e = assertThrows(RunTimeoutException.class, () -> api.client().scrape("ats.board", INPUT, options));
        assertEquals("run_1", e.runId());
        assertEquals("wait=0.08", api.received().get(0).query);
    }
}
