package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.errors.AttestationRequiredException;
import com.enrichednow.errors.FormulaRunFailedException;
import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RunStatus;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** The formula calls of {@code client.async()}: the same requests, answered later. */
class FormulaAsyncTest {
    private static final String RUN = "frun_000000000000000000000002";
    private static final String SAVED = "sf_000000000000000000000002";

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
    void theCatalogueEstimatesAndRuns() throws Exception {
        EnrichedNowAsyncClient async = api.client().async();
        api.answer("formulas_list");
        assertEquals(3, get(async.formulas().list()).size());
        FakeApi.fixture("formulas_list").assertSent(api.last());

        api.answer("formulas_get");
        assertEquals("Job Order Hunter", get(async.formulas().get("job-order-hunter")).title());

        api.answer("formulas_estimate");
        assertTrue(get(async.formulas().estimate(FormulasTest.PARAMS, FormulaRunOptions.builder().unitCap(2000).build())).valid());
        FakeApi.fixture("formulas_estimate").assertSent(api.last());

        api.answer("formulas_run_queued");
        FormulaRun run = get(async.formulas().run(FormulasTest.PARAMS, FormulaRunOptions.builder().unitCap(2000).idempotencyKey("c0ffee-run-1").build()));
        FakeApi.fixture("formulas_run_queued").assertSent(api.last());
        assertEquals(RunStatus.QUEUED, run.status());
    }

    @Test
    void runsAndTheirRows() throws Exception {
        EnrichedNowAsyncClient async = api.client().async();
        api.answer("formula_runs_list");
        assertEquals(1, get(async.formulaRuns().listPage(FormulaRunListParams.builder().limit(1).build())).items().size());
        FakeApi.fixture("formula_runs_list").assertSent(api.last());

        api.answer("formula_runs_get");
        assertEquals(RunStatus.PARTIAL, get(async.formulaRuns().get(RUN, Duration.ofSeconds(60))).status());
        FakeApi.fixture("formula_runs_get").assertSent(api.last());

        api.answer("formula_runs_get_running");
        assertEquals(RunStatus.RUNNING, get(async.formulaRuns().get(RUN)).status());

        api.answer("formula_runs_rows").answer("formula_runs_rows_page2");
        Page<FormulaRow> first = get(async.formulaRuns().rowsPage(RUN, FormulaRowListParams.limit(2)));
        Page<FormulaRow> second = get(first.nextPageAsync());
        FakeApi.fixture("formula_runs_rows_page2").assertSent(api.last());
        assertFalse(second.hasNextPage());

        api.answer("formula_runs_rows_csv");
        assertEquals("Mg", get(async.formulaRuns().exportPage(RUN, RecordFormat.CSV, FormulaRowListParams.limit(2))).nextCursor());
        FakeApi.fixture("formula_runs_rows_csv").assertSent(api.last());

        api.answer("formula_runs_cancel");
        assertEquals(RunStatus.RUNNING, get(async.formulaRuns().cancel("frun_000000000000000000000001")).status());
        FakeApi.fixture("formula_runs_cancel").assertSent(api.last());
    }

    @Test
    void waitForHoldsEachLookAndGivesUp() throws Exception {
        api.answer("formula_runs_get_running").answer("formula_runs_get");
        FormulaRun run = get(api.client().async().formulaRuns().waitFor(RUN, WaitOptions.builder().pollInterval(Duration.ofMillis(5)).build()));
        assertEquals(RunStatus.PARTIAL, run.status());
        FakeApi.fixture("formula_runs_get").assertSent(api.received().get(0), Map.of("wait", "60"));
        FakeApi.fixture("formula_runs_get").assertSent(api.received().get(1));

        for (int i = 0; i < 50; i++) {
            api.answer("formula_runs_get_running");
        }
        WaitOptions brief = WaitOptions.builder().timeout(Duration.ofMillis(80)).pollInterval(Duration.ofMillis(20)).build();
        RunTimeoutException e = assertInstanceOf(RunTimeoutException.class, failure(api.client().async().formulaRuns().waitFor(RUN, brief)));
        assertEquals(RUN, e.formulaRun().id());
    }

    @Test
    void runFormulaWaitsAndReturnsTheResult() throws Exception {
        api.answer("formulas_run_queued").answer("formula_runs_get");
        FormulaResult result = get(api.client().async().runFormula(FormulasTest.PARAMS,
                FormulaRunOptions.builder().unitCap(2000).idempotencyKey("c0ffee-run-1").pollInterval(Duration.ofMillis(5)).build()));
        FakeApi.fixture("formulas_run_queued").assertSent(api.received().get(0), Map.of("wait", "60"));
        FakeApi.fixture("formula_runs_get").assertSent(api.received().get(1));
        assertEquals(RunStatus.PARTIAL, result.run().status());

        api.answer("formula_runs_rows").answer("formula_runs_rows_page2");
        assertEquals(4, result.rows(FormulaRowListParams.limit(2)).toList().size());
    }

    @Test
    void runFormulaFailsTheFutureWhenTheRunFailedOrWasRefused() {
        api.reply(FakeApi.fixture("formulas_run").replyWith(body -> body.put("status", "failed").put("reason", "source_failed:jobs")));
        FormulaRunFailedException failed = assertInstanceOf(FormulaRunFailedException.class,
                failure(api.client().async().runFormula("job-order-hunter", FormulasTest.paramsAsMap())));
        assertEquals("source_failed:jobs", failed.reason());

        api.answer("error_attestation_required");
        assertInstanceOf(AttestationRequiredException.class, failure(api.client().async().runFormula(FormulasTest.PARAMS)));
    }

    @Test
    void savedFormulas() throws Exception {
        EnrichedNowAsyncClient async = api.client().async();
        api.answer("saved_create");
        assertEquals(SAVED, get(async.savedFormulas().create(SavedFormulaCreate.builder(FormulasTest.PARAMS)
                .schedule(Schedule.weekdays("07:30", "Europe/London")).notify(Notify.DIGEST).build())).id());
        FakeApi.fixture("saved_create").assertSent(api.last());

        api.answer("saved_list");
        assertEquals(1, get(async.savedFormulas().listPage(SavedFormulaListParams.builder().limit(10).build())).items().size());
        FakeApi.fixture("saved_list").assertSent(api.last());

        api.answer("saved_get");
        get(async.savedFormulas().get(SAVED));
        FakeApi.fixture("saved_get").assertSent(api.last());

        api.answer("saved_update");
        get(async.savedFormulas().update(SAVED, SavedFormulaChange.builder().name("Java in London, weekdays").monthlyUnitCap(20000).build()));
        FakeApi.fixture("saved_update").assertSent(api.last());

        api.answer("saved_run");
        get(async.savedFormulas().run(SAVED, SavedFormulaRunOptions.builder().wait(Duration.ofSeconds(60)).idempotencyKey("c0ffee-saved-1").build()));
        FakeApi.fixture("saved_run").assertSent(api.last());

        api.answer("saved_pause");
        assertTrue(get(async.savedFormulas().pause(SAVED)).isPaused());
        api.answer("saved_resume");
        assertTrue(get(async.savedFormulas().resume(SAVED)).isActive());
        api.answer("saved_seen_reset");
        assertEquals(4, get(async.savedFormulas().resetSeen(SAVED)).cleared());
        api.answer("saved_duplicate");
        get(async.savedFormulas().duplicate(SAVED, DuplicateOptions.builder().name("Java in London (copy)").copySeen(true).build()));
        FakeApi.fixture("saved_duplicate").assertSent(api.last());
        api.answer("saved_archive");
        assertTrue(get(async.savedFormulas().archive(SAVED)).isArchived());

        api.answer("saved_rows");
        get(async.savedFormulas().rowsPage(SAVED, FormulaRowListParams.builder()
                .since(java.time.Instant.parse("2026-11-25T09:02:55.250Z")).limit(2).build()));
        FakeApi.fixture("saved_rows").assertSent(api.last());
        api.answer("saved_rows_csv");
        get(async.savedFormulas().exportPage(SAVED, RecordFormat.CSV, FormulaRowListParams.builder()
                .fields("company_name", "job_title", "job_url").build()));
        FakeApi.fixture("saved_rows_csv").assertSent(api.last());
        api.answer("saved_snapshots");
        assertEquals(2, get(async.savedFormulas().snapshots("sf_000000000000000000000001", "https://board.example/1", null)).size());
        api.answer("saved_limits");
        assertEquals(1440, get(async.savedFormulas().limits()).minIntervalMinutes());
    }

    @Test
    void suppressionsAttestationsAndFeedback() throws Exception {
        EnrichedNowAsyncClient async = api.client().async();
        api.answer("suppressions_add");
        assertEquals(3, get(async.suppressions().add(List.of(SuppressionEntry.domain("https://www.globex.example/"),
                SuppressionEntry.email("Ana@Initech.example"), SuppressionEntry.companyName("Umbrella, Inc.")), null)));
        FakeApi.fixture("suppressions_add").assertSent(api.last());

        api.answer("suppressions_list");
        assertEquals(2, get(async.suppressions().listPage(SuppressionListParams.builder().limit(2).build())).items().size());

        api.answer("suppressions_upload");
        String csv = FakeApi.fixture("suppressions_upload").request.path("text").asText();
        assertEquals(3, get(async.suppressions().upload(csv.getBytes(java.nio.charset.StandardCharsets.UTF_8), null)).added());
        FakeApi.fixture("suppressions_upload").assertSent(api.last());

        api.answer("suppressions_delete");
        get(async.suppressions().delete(SuppressionKind.DOMAIN, "globex.example"));
        FakeApi.fixture("suppressions_delete").assertSent(api.last());

        api.answer("attestations_list");
        assertTrue(get(async.attestations().list()).get(0).accepted());

        api.answer("formula_rows_feedback");
        assertEquals("flagged", get(async.formulaRows().feedback("frow_000000000000000000000001", RowVerdict.WRONG_COMPANY,
                "This is the agency, not the employer.", "corvid.example")).status());
        FakeApi.fixture("formula_rows_feedback").assertSent(api.last());
    }
}
