package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Fixture;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.PlanLimits;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RunStatus;
import com.enrichednow.models.SavedFormula;
import com.enrichednow.models.SeenReset;
import com.enrichednow.models.Snapshot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Saved formulas, against the recorded answers of the real routes. */
class SavedFormulasTest {
    private static final String SAVED = "sf_000000000000000000000002";

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    @Test
    void aFormulaIsSavedWithItsScheduleAndOnlyWhatIsGiven() {
        SavedFormula saved = api.answer("saved_create").client().savedFormulas().create(SavedFormulaCreate.builder(FormulasTest.PARAMS)
                .schedule(Schedule.weekdays("07:30", "Europe/London"))
                .notify(Notify.DIGEST)
                .build());
        FakeApi.fixture("saved_create").assertSent(api.only());
        assertNull(api.only().header("Idempotency-Key"));
        assertEquals(SAVED, saved.id());
        assertEquals("job-order-hunter", saved.formulaId());
        assertEquals("1.1.0", saved.version());
        assertEquals(1, saved.major());
        assertEquals("Java Developer, London, on weekdays", saved.name());
        assertTrue(saved.nameAuto());
        assertEquals(List.of("Java Developer"), saved.params().get("job_titles"));
        assertEquals(3000, saved.unitCap());
        assertEquals(200, saved.rowCap());
        assertNull(saved.monthlyUnitCap());
        assertEquals("active", saved.status());
        assertTrue(saved.isActive());
        assertFalse(saved.isPaused());
        assertNull(saved.pausedReason());
        assertEquals("key_000000000000000000000001", saved.runsWithKey());
        assertEquals("digest", saved.notifyMode());
        assertEquals("weekdays", saved.schedule().cadence());
        assertEquals("07:30", saved.schedule().localTime());
        assertEquals("Europe/London", saved.schedule().timezone());
        assertNull(saved.schedule().dayOfWeek());
        assertNull(saved.schedule().dayOfMonth());
        assertEquals(Instant.parse("2026-11-25T09:04:29Z"), saved.schedule().nextFireAt());
        assertEquals(0, saved.consecutiveFailures());
        assertEquals(0, saved.emptyRuns());
        assertTrue(saved.notes().isEmpty());
        assertEquals(35, saved.estimate().expectedUnits());
        assertNull(saved.lastRun());
        assertEquals(0, saved.units30d());
        assertEquals(Instant.parse("2026-11-25T09:02:53.250Z"), saved.createdAt());
        assertNull(saved.archivedAt());
    }

    @Test
    void savingIsNotSentAgainAfterAServerError() throws Exception {
        api.error(500, "internal", "x");
        assertThrows(com.enrichednow.errors.ServerException.class,
                () -> api.client().savedFormulas().create("job-order-hunter", FormulasTest.paramsAsMap()));
        assertEquals(1, api.count());
        JsonNode body = JSON.readTree(api.only().body);
        assertEquals(List.of("formula_id", "params"), fieldNames(body));
        assertEquals("job-order-hunter", body.path("formula_id").asText());
        assertEquals(JSON.valueToTree(FormulasTest.paramsAsMap()), body.path("params"));
    }

    private static final ObjectMapper JSON = new ObjectMapper();

    private static List<String> fieldNames(JsonNode json) {
        List<String> names = new ArrayList<>();
        json.fieldNames().forEachRemaining(names::add);
        return names;
    }

    @Test
    void schedulesAreWrittenAsTheApiTakesThem() {
        assertEquals(Map.of("cadence", "daily", "local_time", "07:30", "timezone", "Europe/London"),
                Schedule.daily("07:30", "Europe/London").toMap());
        assertEquals(Map.of("cadence", "weekly", "local_time", "06:00", "timezone", "UTC", "day_of_week", 0),
                Schedule.weekly(DayOfWeek.MONDAY, "06:00", "UTC").toMap());
        assertEquals(6, Schedule.weekly(DayOfWeek.SUNDAY, "06:00", "UTC").dayOfWeek());
        assertEquals(Map.of("cadence", "monthly", "local_time", "09:00", "timezone", "Europe/Berlin", "day_of_month", 31),
                Schedule.monthly(31, "09:00", "Europe/Berlin").toMap());
        assertEquals("hourly", Schedule.hourly("00:15", "UTC").cadence());
        assertThrows(IllegalArgumentException.class, () -> Schedule.daily("7:30", "UTC"));
        assertThrows(IllegalArgumentException.class, () -> Schedule.daily("24:00", "UTC"));
        assertThrows(IllegalArgumentException.class, () -> Schedule.daily("07:30", " "));
        assertThrows(IllegalArgumentException.class, () -> Schedule.monthly(0, "07:30", "UTC"));
    }

    @Test
    void savedFormulasAreListedAcrossPages() {
        api.ok("{\"saved_formulas\":[" + FakeApi.fixture("saved_get").body() + "],\"next_cursor\":\"c2\"}").answer("saved_list");
        List<SavedFormula> all = api.client().savedFormulas().list(SavedFormulaListParams.builder().limit(10).build()).toList();
        assertEquals(2, all.size());
        assertEquals(Map.of("limit", "10"), Fixture.queryOf(api.received().get(0)));
        assertEquals(Map.of("limit", "10", "cursor", "c2"), Fixture.queryOf(api.received().get(1)));

        api.answer("saved_list");
        Page<SavedFormula> page = api.client().savedFormulas().listPage(SavedFormulaListParams.builder().limit(10).build());
        FakeApi.fixture("saved_list").assertSent(api.last());
        assertEquals(SAVED, page.items().get(0).id());
        assertFalse(page.hasNextPage());

        api.answer("saved_list");
        api.client().savedFormulas().listPage(SavedFormulaListParams.builder().formulaId("job-order-hunter")
                .status(SavedFormulaListParams.Status.ARCHIVED).build());
        assertEquals("formula_id=job-order-hunter&status=archived", api.last().query);
    }

    @Test
    void oneSavedFormulaIsRead() {
        SavedFormula saved = api.answer("saved_get").client().savedFormulas().get(SAVED);
        FakeApi.fixture("saved_get").assertSent(api.only());
        assertEquals("Java Developer, London, on weekdays", saved.name());
    }

    @Test
    void aChangeSendsOnlyWhatItSets() {
        SavedFormula saved = api.answer("saved_update").client().savedFormulas().update(SAVED,
                SavedFormulaChange.builder().name("Java in London, weekdays").monthlyUnitCap(20000).build());
        FakeApi.fixture("saved_update").assertSent(api.only());
        assertEquals("Java in London, weekdays", saved.name());
        assertFalse(saved.nameAuto());
        assertEquals(20000L, saved.monthlyUnitCap());
    }

    @Test
    void aChangeCanRemoveTheScheduleTheCapAndTheName() throws Exception {
        api.answer("saved_update").answer("saved_update").answer("saved_update");
        EnrichedNowClient client = api.client();
        client.savedFormulas().update(SAVED, SavedFormulaChange.builder().removeSchedule().removeMonthlyUnitCap().autoName().build());
        assertEquals("{\"schedule\":null,\"monthly_unit_cap\":null,\"name\":null}", api.last().body);
        assertEquals("PATCH /v1/saved-formulas/" + SAVED, api.last().toString());

        client.savedFormulas().update(SAVED, SavedFormulaChange.builder().params(FormulasTest.PARAMS).resetSeen(true)
                .schedule(Schedule.daily("06:00", "UTC")).unitCap(100).rowCap(50).notify(Notify.NONE).build());
        JsonNode body = JSON.readTree(api.last().body);
        assertEquals(List.of("params", "reset_seen", "schedule", "unit_cap", "row_cap", "notify"), fieldNames(body));
        assertEquals(JSON.valueToTree(FormulasTest.paramsAsMap()), body.path("params"));
        assertEquals(JSON.readTree("{\"cadence\":\"daily\",\"local_time\":\"06:00\",\"timezone\":\"UTC\"}"), body.path("schedule"));
        assertTrue(body.path("reset_seen").asBoolean());
        assertEquals(100, body.path("unit_cap").asInt());
        assertEquals(50, body.path("row_cap").asInt());
        assertEquals("none", body.path("notify").asText());

        client.savedFormulas().update(SAVED, SavedFormulaChange.builder().name("x").schedule(Schedule.daily("06:00", "UTC"))
                .removeSchedule().build());
        assertEquals("{\"name\":\"x\",\"schedule\":null}", api.last().body);   // the last word on a field wins
    }

    @Test
    void anEmptyChangeIsRefusedBeforeSending() {
        assertThrows(IllegalArgumentException.class, () -> api.client().savedFormulas().update(SAVED, SavedFormulaChange.builder().build()));
        assertThrows(IllegalArgumentException.class, () -> api.client().savedFormulas().update(SAVED, null));
        assertThrows(NullPointerException.class, () -> SavedFormulaChange.builder().name(null));
        assertThrows(NullPointerException.class, () -> SavedFormulaChange.builder().schedule(null));
        assertEquals(0, api.count());
    }

    @Test
    void aChangeIsSafeToSendAgain() {
        api.error(502, "internal", "x").answer("saved_update");
        api.client().savedFormulas().update(SAVED, SavedFormulaChange.builder().name("Java in London, weekdays").monthlyUnitCap(20000).build());
        assertEquals(2, api.count());
    }

    @Test
    void aSavedFormulaIsArchived() {
        SavedFormula saved = api.answer("saved_archive").client().savedFormulas().archive(SAVED);
        FakeApi.fixture("saved_archive").assertSent(api.only());
        assertTrue(saved.isArchived());
        assertEquals(Instant.parse("2026-11-25T09:04:25.250Z"), saved.archivedAt());
        assertNull(saved.schedule().nextFireAt());
    }

    @Test
    void aSavedFormulaIsDuplicated() {
        SavedFormula copy = api.answer("saved_duplicate").client().savedFormulas().duplicate(SAVED,
                DuplicateOptions.builder().name("Java in London (copy)").copySeen(true).build());
        FakeApi.fixture("saved_duplicate").assertSent(api.only());
        assertEquals("sf_000000000000000000000003", copy.id());
        assertNull(copy.schedule());

        api.answer("saved_duplicate");
        api.client().savedFormulas().duplicate(SAVED);
        assertEquals("{}", api.last().body);
        api.answer("saved_duplicate");
        api.client().savedFormulas().duplicate(SAVED, DuplicateOptions.builder().schedule(Schedule.weekly(DayOfWeek.FRIDAY, "17:00", "UTC")).build());
        assertEquals("{\"schedule\":{\"cadence\":\"weekly\",\"local_time\":\"17:00\",\"timezone\":\"UTC\",\"day_of_week\":4}}", api.last().body);
    }

    @Test
    void aSavedFormulaIsRunNowWithAnIdempotencyKey() {
        FormulaRun run = api.answer("saved_run").client().savedFormulas().run(SAVED,
                SavedFormulaRunOptions.builder().wait(Duration.ofSeconds(60)).idempotencyKey("c0ffee-saved-1").build());
        FakeApi.fixture("saved_run").assertSent(api.only());
        assertEquals(SAVED, run.savedFormulaId());
        assertEquals(RunStatus.PARTIAL, run.status());

        api.answer("saved_run");
        api.client().savedFormulas().run(SAVED, SavedFormulaRunOptions.builder().preview(true).includeSeen(true).build());
        assertEquals("{\"mode\":\"preview\",\"include_seen\":true}", api.last().body);
        assertEquals(36, api.last().header("Idempotency-Key").length());
        assertNull(api.last().query);

        api.error(500, "internal", "x").answer("saved_run");
        api.client().savedFormulas().run(SAVED);
        List<FakeApi.Received> all = api.received();
        assertEquals("{}", all.get(all.size() - 1).body);
        assertEquals(all.get(all.size() - 2).header("Idempotency-Key"), all.get(all.size() - 1).header("Idempotency-Key"));
    }

    @Test
    void itsScheduleIsPausedAndResumed() {
        api.answer("saved_pause").answer("saved_resume");
        SavedFormula paused = api.client().savedFormulas().pause(SAVED);
        FakeApi.fixture("saved_pause").assertSent(api.received().get(0));
        assertTrue(paused.isPaused());
        assertEquals("manual", paused.pausedReason());
        assertNull(paused.schedule().nextFireAt());
        assertEquals("frun_000000000000000000000004", paused.lastRun().id());
        assertEquals(RunStatus.PARTIAL, paused.lastRun().status());
        assertEquals("partial", paused.lastRun().rawStatus());
        assertEquals("contacts_not_connected", paused.lastRun().reason());
        assertEquals(4, paused.lastRun().rowsNew());
        assertEquals(Instant.parse("2026-11-25T09:02:56.250Z"), paused.lastRun().createdAt());
        assertEquals(Instant.parse("2026-11-25T09:04:20.250Z"), paused.lastRun().finishedAt());
        assertEquals(17, paused.units30d());

        SavedFormula resumed = api.client().savedFormulas().resume(SAVED);
        FakeApi.fixture("saved_resume").assertSent(api.received().get(1));
        assertTrue(resumed.isActive());
        assertEquals(Instant.parse("2026-11-25T09:04:29Z"), resumed.schedule().nextFireAt());
    }

    @Test
    void itsDeliveredRowsCanBeForgotten() {
        SeenReset reset = api.answer("saved_seen_reset").client().savedFormulas().resetSeen(SAVED);
        FakeApi.fixture("saved_seen_reset").assertSent(api.only());
        assertEquals(4, reset.cleared());
        assertEquals(SAVED, reset.savedFormula().id());
    }

    @Test
    void itsNewRowsArePolledWithSince() {
        Fixture fixture = FakeApi.fixture("saved_rows");
        api.answer("saved_rows").ok("{\"rows\":[],\"next_cursor\":null}");
        FormulaRowListParams params = FormulaRowListParams.builder().since(Instant.parse("2026-11-25T09:02:55.250Z")).limit(2).build();
        List<FormulaRow> rows = api.client().savedFormulas().rows(SAVED, params).toList();
        fixture.assertSent(api.received().get(0));
        assertEquals(2, rows.size());
        assertEquals("frun_000000000000000000000004", rows.get(0).runId());
        Map<String, String> second = new TreeMap<>(fixture.query());
        second.put("cursor", fixture.body().path("next_cursor").asText());
        assertEquals(second, Fixture.queryOf(api.received().get(1)));

        api.ok("{\"rows\":[],\"next_cursor\":null}");
        api.client().savedFormulas().rowsPage(SAVED, FormulaRowListParams.builder().order(FormulaRowListParams.Order.DESC)
                .since(Instant.parse("2026-11-25T09:00:00Z")).minScore(2.5).build());
        assertEquals(Map.of("order", "desc", "since", "2026-11-25T09:00:00+00:00", "min_score", "2.5"), Fixture.queryOf(api.last()));
        assertThrows(IllegalArgumentException.class, () -> api.client().savedFormulas().rowsPage(SAVED,
                FormulaRowListParams.builder().view(FormulaRowListParams.View.COMPANY).build()));
    }

    @Test
    void itsRowsAreExported() {
        api.answer("saved_rows_csv");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        api.client().savedFormulas().exportAll(SAVED, RecordFormat.CSV,
                FormulaRowListParams.builder().fields("company_name", "job_title", "job_url").build(), out);
        Fixture fixture = FakeApi.fixture("saved_rows_csv");
        Map<String, String> withPageSize = new TreeMap<>(fixture.query());
        withPageSize.put("limit", "1000");
        fixture.assertSent(api.only(), withPageSize);
        assertEquals(fixture.text(), out.toString(StandardCharsets.UTF_8));

        api.answer("saved_rows_csv");
        String page = api.client().savedFormulas().exportPage(SAVED, RecordFormat.CSV,
                FormulaRowListParams.builder().fields(List.of("company_name", "job_title", "job_url")).build()).text();
        fixture.assertSent(api.last());
        assertTrue(page.startsWith("company_name,job_title,job_url\r\n"));
    }

    @Test
    void whatItObservedIsReadAsSnapshots() {
        List<Snapshot> snapshots = api.answer("saved_snapshots").client().savedFormulas()
                .snapshots("sf_000000000000000000000001", "https://board.example/1", null);
        FakeApi.fixture("saved_snapshots").assertSent(api.only());
        assertEquals(2, snapshots.size());
        Snapshot latest = snapshots.get(0);
        assertEquals("job", latest.entity());
        assertEquals("https://board.example/1", latest.entityKey());
        assertEquals("Senior Java Developer", latest.fields().get("job.title"));
        assertEquals("frun_000000000000000000000005", latest.runId());
        assertEquals(Instant.parse("2026-11-25T09:04:27.250Z"), latest.observedAt());

        api.answer("saved_snapshots");
        api.client().savedFormulas().snapshots(SAVED);
        assertNull(api.last().query);
    }

    @Test
    void whatThePlanAllowsIsRead() {
        PlanLimits limits = api.answer("saved_limits").client().savedFormulas().limits();
        FakeApi.fixture("saved_limits").assertSent(api.only());
        assertEquals(3, limits.schedules());
        assertEquals(1, limits.schedulesActive());
        assertEquals(1440, limits.minIntervalMinutes());
        assertEquals(90, limits.rowRetentionDays());
        assertEquals(3, limits.destinations());
    }
}
