package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Fixture;
import com.enrichednow.FakeApi.Received;
import com.enrichednow.errors.FormulaRunFailedException;
import com.enrichednow.errors.RunFailedException;
import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import com.enrichednow.models.RunStatus;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Formula runs, their rows and exports, waiting, and {@code runFormula}, against the recorded answers. */
class FormulaRunsTest {
    private static final String RUN = "frun_000000000000000000000002";
    private static final WaitOptions FAST = WaitOptions.builder().pollInterval(Duration.ofMillis(5)).build();

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    private static Map<String, String> query(String... pairs) {
        Map<String, String> query = new java.util.TreeMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            query.put(pairs[i], pairs[i + 1]);
        }
        return query;
    }

    // --- reading, listing, cancelling ---------------------------------------------------------------------------------

    @Test
    void oneRunIsReadAsItIsNow() {
        FormulaRun run = api.answer("formula_runs_get_running").client().formulaRuns().get(RUN);
        FakeApi.fixture("formula_runs_get_running").assertSent(api.only());
        assertEquals(RunStatus.RUNNING, run.status());
        assertFalse(run.isFinished());
        assertEquals(1, run.steps().size());
        assertEquals("running", run.steps().get(0).status());
        assertNull(run.steps().get(0).finishedAt());
    }

    @Test
    void oneRunIsReadWhenItHasEnded() {
        FormulaRun run = api.answer("formula_runs_get").client().formulaRuns().get(RUN, Duration.ofSeconds(60));
        FakeApi.fixture("formula_runs_get").assertSent(api.only());
        assertEquals(RunStatus.PARTIAL, run.status());
        assertEquals(24, run.unitsSpent());
        assertEquals(4, run.rowsTotal());
        assertEquals(Instant.parse("2026-11-25T09:01:25.250Z"), run.finishedAt());
        assertTrue(run.steps().size() > 10);
        assertThrows(IllegalArgumentException.class, () -> api.client().formulaRuns().get(RUN, Duration.ofSeconds(301)));
    }

    @Test
    void fieldsAndStatusesThisSdkDoesNotKnowDoNotBreakReading() {
        api.reply(FakeApi.fixture("formula_runs_get").replyWith(body -> {
            body.put("status", "paused").put("a_field_added_later", 1);
            ((com.fasterxml.jackson.databind.node.ObjectNode) body.path("steps").get(0)).put("cost_basis", "x");
            body.putArray("reasons").addObject().put("code", "new_reason").put("detail", 3);
        }));
        FormulaRun run = api.client().formulaRuns().get(RUN);
        assertEquals(RunStatus.UNKNOWN, run.status());
        assertEquals("paused", run.rawStatus());
        assertFalse(run.isFinished());
        assertEquals("new_reason", run.reasons().get(0).code());
        assertEquals(3, run.reasons().get(0).values().get("detail"));
        assertEquals("search_ats", run.steps().get(0).id());
    }

    @Test
    void runsAreListedAPageAtATime() {
        Fixture fixture = FakeApi.fixture("formula_runs_list");
        api.answer("formula_runs_list").ok("{\"runs\":[],\"next_cursor\":null}");
        Page<FormulaRun> first = api.client().formulaRuns().listPage(FormulaRunListParams.builder().limit(1).build());
        fixture.assertSent(api.only());
        assertEquals(1, first.items().size());
        assertEquals("frun_000000000000000000000003", first.items().get(0).id());
        assertTrue(first.items().get(0).steps().isEmpty());
        assertEquals(fixture.body().path("next_cursor").asText(), first.nextCursor());
        Page<FormulaRun> second = first.nextPage();
        assertEquals(query("limit", "1", "cursor", first.nextCursor()), Fixture.queryOf(api.last()));
        assertFalse(second.hasNextPage());
    }

    @Test
    void runsAreListedAcrossPagesWithTheSameFilters() {
        Fixture fixture = FakeApi.fixture("formula_runs_list");
        api.answer("formula_runs_list").ok("{\"runs\":[],\"next_cursor\":null}");
        List<FormulaRun> runs = api.client().formulaRuns().list(FormulaRunListParams.builder().formulaId("job-order-hunter")
                .savedFormulaId("sf_1").status(RunStatus.PARTIAL).build()).toList();
        assertEquals(1, runs.size());
        assertEquals(query("formula_id", "job-order-hunter", "saved_formula_id", "sf_1", "status", "partial"),
                Fixture.queryOf(api.received().get(0)));
        assertEquals(fixture.body().path("next_cursor").asText(), Fixture.queryOf(api.received().get(1)).get("cursor"));
        assertEquals("sf_1", Fixture.queryOf(api.received().get(1)).get("saved_formula_id"));
        assertThrows(IllegalArgumentException.class, () -> FormulaRunListParams.builder().status(RunStatus.UNKNOWN));
    }

    @Test
    void aRunIsAskedToStop() {
        api.error(500, "internal", "x").answer("formula_runs_cancel");
        FormulaRun run = api.client().formulaRuns().cancel("frun_000000000000000000000001");
        assertEquals(2, api.count());                           // safe to send again
        FakeApi.fixture("formula_runs_cancel").assertSent(api.last());
        assertEquals(RunStatus.RUNNING, run.status());        // asked to stop; waitFor shows how it ended
    }

    // --- rows ---------------------------------------------------------------------------------------------------------

    @Test
    void rowsArePagedThroughAutomatically() {
        api.answer("formula_runs_rows").answer("formula_runs_rows_page2");
        List<String> companies = new ArrayList<>();
        for (FormulaRow row : api.client().formulaRuns().rows(RUN, FormulaRowListParams.limit(2))) {
            companies.add((String) row.get("company_name"));
        }
        FakeApi.fixture("formula_runs_rows").assertSent(api.received().get(0));
        FakeApi.fixture("formula_runs_rows_page2").assertSent(api.received().get(1));
        assertEquals(List.of("Corvid", "Corvid", "Northwind Ltd", "Quiet Agencies Ltd"), companies);
    }

    @Test
    void aRowIsReadWhole() {
        Page<FormulaRow> page = api.answer("formula_runs_rows").client().formulaRuns().rowsPage(RUN, FormulaRowListParams.limit(2));
        FormulaRow row = page.items().get(0);
        assertEquals("frow_000000000000000000000001", row.id());
        assertEquals(RUN, row.runId());
        assertEquals("corvid-demo.com|java developer|london uk|careers@corvid-demo.com", row.rowKey());
        assertEquals("Java Developer (Remote)", row.data().get("job_title"));
        assertEquals(List.of("Java", "AWS"), row.get("technologies"));
        assertNull(row.get("contact_name"));
        assertEquals(20.0, row.score());
        assertTrue(row.evidence().get("sources") instanceof List);
        assertEquals("new", row.status());
        assertNotNull(row.firstSeenAt());
        assertEquals("Mg", page.nextCursor());
    }

    @Test
    void rowsKeepOnlyTheColumnsAskedForAndAScore() {
        List<FormulaRow> rows = api.answer("formula_runs_rows_fields").client().formulaRuns().rows(RUN, FormulaRowListParams.builder()
                .fields("company_name", "job_title", "contact_email").minScore(10).build()).toList();
        FakeApi.fixture("formula_runs_rows_fields").assertSent(api.only());
        assertEquals(3, rows.size());
        assertEquals(3, rows.get(0).data().size());
    }

    @Test
    void rowsCanBeReadOnePerCompany() {
        List<FormulaRow> rows = api.answer("formula_runs_rows_company").client().formulaRuns()
                .rows(RUN, FormulaRowListParams.builder().view(FormulaRowListParams.View.COMPANY).build()).toList();
        FakeApi.fixture("formula_runs_rows_company").assertSent(api.only());
        assertEquals("company:corvid-demo.com", rows.get(0).rowKey());
        assertEquals(2, ((List<?>) rows.get(0).get("job_title")).size());
        assertEquals(2, rows.get(0).get("rows"));
    }

    @Test
    void sinceAndOrderAreRefusedForARunsRows() {
        assertThrows(IllegalArgumentException.class, () -> api.client().formulaRuns()
                .rowsPage(RUN, FormulaRowListParams.builder().since(Instant.now()).build()));
        assertThrows(IllegalArgumentException.class, () -> api.client().formulaRuns()
                .rowsPage(RUN, FormulaRowListParams.builder().order(FormulaRowListParams.Order.DESC).build()));
        assertThrows(IllegalArgumentException.class, () -> FormulaRowListParams.builder().fields(List.of()));
        assertEquals(0, api.count());
    }

    // --- exports ------------------------------------------------------------------------------------------------------

    @Test
    void anExportIsOnePageWithTheNextCursorInAHeader() {
        RecordsExport page = api.answer("formula_runs_rows_csv").client().formulaRuns()
                .exportPage(RUN, RecordFormat.CSV, FormulaRowListParams.limit(2));
        FakeApi.fixture("formula_runs_rows_csv").assertSent(api.only());
        assertEquals("text/csv", api.only().header("Accept"));
        assertEquals(FakeApi.fixture("formula_runs_rows_csv").text(), page.text());
        assertEquals("Mg", page.nextCursor());
        assertFalse(page.expired());
    }

    @Test
    void exportAllWritesOneCsvFileWithTheHeaderOnce() {
        api.answer("formula_runs_rows_csv").answer("formula_runs_rows_csv_page2");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        api.client().formulaRuns().exportAll(RUN, RecordFormat.CSV, FormulaRowListParams.limit(2), out);
        FakeApi.fixture("formula_runs_rows_csv").assertSent(api.received().get(0));
        FakeApi.fixture("formula_runs_rows_csv_page2").assertSent(api.received().get(1));
        String first = FakeApi.fixture("formula_runs_rows_csv").text();
        String second = FakeApi.fixture("formula_runs_rows_csv_page2").text();
        String expected = first + second.substring(second.indexOf('\n') + 1);
        String written = out.toString(StandardCharsets.UTF_8);
        assertEquals(expected, written);
        assertEquals(1, written.split("company_name,company_domain", -1).length - 1);
        assertEquals(5, written.split("\r\n").length);                   // the header and four rows
    }

    @Test
    void exportAllWritesNdjsonAsItCame() {
        api.answer("formula_runs_rows_ndjson");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        api.client().formulaRuns().exportAll(RUN, RecordFormat.NDJSON, out);
        FakeApi.fixture("formula_runs_rows_ndjson").assertSent(api.only(), query("format", "ndjson", "limit", "1000"));
        assertEquals("application/x-ndjson", api.only().header("Accept"));
        assertEquals(FakeApi.fixture("formula_runs_rows_ndjson").text(), out.toString(StandardCharsets.UTF_8));

        api.answer("formula_runs_rows_ndjson");
        RecordsExport page = api.client().formulaRuns().exportPage(RUN, RecordFormat.NDJSON, null);
        FakeApi.fixture("formula_runs_rows_ndjson").assertSent(api.last());
        assertNull(page.nextCursor());
        assertEquals(4, page.text().split("\n").length);
    }

    // --- waiting ------------------------------------------------------------------------------------------------------

    @Test
    void waitForHoldsEachLookUntilTheRunHasEnded() {
        api.answer("formula_runs_get_running").answer("formula_runs_get");
        FormulaRun run = api.client().formulaRuns().waitFor(RUN, FAST);
        assertEquals(RunStatus.PARTIAL, run.status());
        List<Received> looks = api.received();
        assertEquals(2, looks.size());
        FakeApi.fixture("formula_runs_get_running").assertSent(looks.get(0), query("wait", "60"));
        FakeApi.fixture("formula_runs_get").assertSent(looks.get(1));
    }

    @Test
    void waitForReturnsARunThatHasEndedWhateverItsStatus() {
        api.reply(FakeApi.fixture("formula_runs_get").replyWith(body -> body.put("status", "failed").put("reason", "source_failed:jobs")));
        FormulaRun run = api.client().formulaRuns().waitFor(RUN);
        assertEquals(RunStatus.FAILED, run.status());
        assertEquals("source_failed:jobs", run.reason());
        assertEquals(1, api.count());
    }

    @Test
    void anAnswerThatComesBackEarlyIsFollowedByAPause() {
        api.answer("formula_runs_get_running").answer("formula_runs_get_running").answer("formula_runs_get");
        long started = System.nanoTime();
        api.client().formulaRuns().waitFor(RUN, WaitOptions.builder().pollInterval(Duration.ofMillis(150)).build());
        long took = Duration.ofNanos(System.nanoTime() - started).toMillis();
        assertEquals(3, api.count());
        assertTrue(took >= 280, "took " + took + " ms: a server that does not hold the request is not asked in a tight loop");
    }

    @Test
    void waitForGivesUpWithTheRunAsItWas() {
        for (int i = 0; i < 50; i++) {
            api.answer("formula_runs_get_running");
        }
        WaitOptions brief = WaitOptions.builder().timeout(Duration.ofMillis(100)).pollInterval(Duration.ofMillis(20)).build();
        RunTimeoutException e = assertThrows(RunTimeoutException.class, () -> api.client().formulaRuns().waitFor(RUN, brief));
        assertEquals(RUN, e.runId());
        assertEquals(RunStatus.RUNNING, e.formulaRun().status());
        assertNull(e.run());
        assertTrue(e.getMessage().startsWith("Formula run " + RUN), e.getMessage());
        assertTrue(api.count() >= 2 && api.count() <= 8, "looked " + api.count() + " times");
        String wait = Fixture.queryOf(api.received().get(0)).get("wait");
        assertTrue(Double.parseDouble(wait) <= 0.1, "wait=" + wait);               // never past the timeout
    }

    // --- runFormula ---------------------------------------------------------------------------------------------------

    @Test
    void runFormulaStartsWaitsAndReadsTheRows() {
        api.answer("formulas_run_queued").answer("formula_runs_get").answer("formula_runs_rows").answer("formula_runs_rows_page2");
        FormulaResult result = api.client().runFormula(FormulasTest.PARAMS,
                FormulaRunOptions.builder().unitCap(2000).idempotencyKey("c0ffee-run-1").pollInterval(Duration.ofMillis(5)).build());
        List<Received> sent = api.received();
        FakeApi.fixture("formulas_run_queued").assertSent(sent.get(0), query("wait", "60"));
        FakeApi.fixture("formula_runs_get").assertSent(sent.get(1));
        assertEquals(RunStatus.PARTIAL, result.run().status());
        assertEquals(RUN, result.run().id());

        List<String> companies = new ArrayList<>();
        for (FormulaRow row : result.rows(FormulaRowListParams.limit(2))) {
            companies.add((String) row.get("company_name"));
        }
        assertEquals(4, companies.size());
        FakeApi.fixture("formula_runs_rows").assertSent(api.received().get(2));
        FakeApi.fixture("formula_runs_rows_page2").assertSent(api.received().get(3));

        api.answer("formula_runs_rows_csv").answer("formula_runs_rows_csv_page2");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        result.exportAll(RecordFormat.CSV, FormulaRowListParams.limit(2), out);
        assertEquals(5, out.toString(StandardCharsets.UTF_8).split("\r\n").length);
    }

    @Test
    void runFormulaReturnsAPreviewThatEndedWithinTheFirstWait() {
        api.answer("formulas_run");
        FormulaResult result = api.client().runFormula("job-order-hunter", FormulasTest.paramsAsMap(),
                FormulaRunOptions.builder().preview(true).idempotencyKey("c0ffee-run-2").build());
        FakeApi.fixture("formulas_run").assertSent(api.only());
        assertEquals(RunStatus.PARTIAL, result.run().status());

        api.reply(FakeApi.fixture("formula_runs_rows").replyWith(body -> body.putNull("next_cursor")));
        List<FormulaRow> rows = new ArrayList<>();
        for (FormulaRow row : result) {                                         // iterating the result walks its rows
            rows.add(row);
        }
        assertEquals(2, rows.size());
        assertEquals("GET /v1/formula-runs/frun_000000000000000000000003/rows?limit=1000", api.last().toString());

        api.answer("formula_runs_rows_ndjson");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        result.exportAll(RecordFormat.NDJSON, out);
        assertEquals("format=ndjson&limit=1000", api.last().query);
    }

    @Test
    void runFormulaThrowsWhenTheRunFailed() {
        for (String status : new String[] {"failed", "unsupported", "cancelled"}) {
            api.reply(FakeApi.fixture("formulas_run").replyWith(body -> body.put("status", status).put("reason", "source_failed:jobs")));
            FormulaRunFailedException e = assertThrows(FormulaRunFailedException.class,
                    () -> api.client().runFormula(FormulasTest.PARAMS, FormulaRunOptions.preview()));
            assertEquals(RunStatus.fromValue(status), e.status());
            assertEquals("source_failed:jobs", e.reason());
            assertEquals("frun_000000000000000000000003", e.runId());
            assertEquals("job-order-hunter", e.formulaId());
            assertSame(e.formulaRun(), e.formulaRun());
            assertEquals(status, e.formulaRun().rawStatus());
            assertNull(e.run());
            assertTrue(e instanceof RunFailedException);
            assertTrue(e.getMessage().contains(status) && e.getMessage().contains("source_failed:jobs"), e.getMessage());
        }
    }

    @Test
    void runFormulaCanBeToldNotToThrow() {
        api.reply(FakeApi.fixture("formulas_run").replyWith(body -> body.put("status", "failed").put("reason", "source_failed:jobs")));
        FormulaResult result = api.client().runFormula(FormulasTest.PARAMS, FormulaRunOptions.builder().throwOnFailure(false).build());
        assertEquals(RunStatus.FAILED, result.run().status());
    }

    @Test
    void runFormulaGivesUpWhenTheRunDoesNotEndInTime() {
        api.answer("formulas_run_queued");
        for (int i = 0; i < 50; i++) {
            api.answer("formula_runs_get_running");
        }
        FormulaRunOptions brief = FormulaRunOptions.builder().timeout(Duration.ofMillis(120)).pollInterval(Duration.ofMillis(20)).build();
        RunTimeoutException e = assertThrows(RunTimeoutException.class, () -> api.client().runFormula(FormulasTest.PARAMS, brief));
        assertEquals(RUN, e.runId());
        assertEquals("0.12", Fixture.queryOf(api.received().get(0)).get("wait"));      // the first wait is never past the timeout
    }

    @Test
    void runFormulaHoldsItsFirstRequestForTheWaitGiven() {
        api.answer("formulas_run");
        api.client().runFormula(FormulasTest.PARAMS, FormulaRunOptions.builder().wait(Duration.ofSeconds(5)).build());
        assertEquals("wait=5", api.only().query);
    }
}
