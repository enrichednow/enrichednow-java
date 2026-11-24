package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Fixture;
import com.enrichednow.formulas.JobOrderHunterParams;
import com.enrichednow.formulas.TalentSourcingParams;
import com.enrichednow.models.Formula;
import com.enrichednow.models.FormulaDetails;
import com.enrichednow.models.FormulaEstimate;
import com.enrichednow.models.FormulaRun;
import com.enrichednow.models.FormulaRunStep;
import com.enrichednow.models.RunStatus;
import com.enrichednow.models.Standing;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** The catalogue of formulas, estimates and starting a run, against the recorded answers of the real routes. */
class FormulasTest {
    /** The parameters every recorded run was started with. */
    static final JobOrderHunterParams PARAMS = JobOrderHunterParams.builder()
            .jobTitles("Java Developer")
            .locations("London")
            .postedWithinDays(2)
            .titleExcludeKeywords("intern")
            .excludeCompanies("Blocked By Us Inc")
            .companySize(11, 5000)
            .build();

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    static Map<String, Object> paramsAsMap() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("job_titles", List.of("Java Developer"));
        params.put("locations", List.of("London"));
        params.put("posted_within_days", 2);
        params.put("title_exclude_keywords", List.of("intern"));
        params.put("exclude_companies", List.of("Blocked By Us Inc"));
        params.put("company_size", Map.of("min", 11, "max", 5000));
        return params;
    }

    // --- the catalogue ------------------------------------------------------------------------------------------------

    @Test
    void theCatalogueIsListed() {
        List<Formula> formulas = api.answer("formulas_list").client().formulas().list();
        FakeApi.fixture("formulas_list").assertSent(api.only());
        assertEquals(3, formulas.size());
        Formula hunter = formulas.get(1);
        assertEquals("job-order-hunter", hunter.id());
        assertEquals("1.1.0", hunter.version());
        assertEquals("Job Order Hunter", hunter.title());
        assertTrue(hunter.summary().startsWith("Companies newly hiring"));
        assertTrue(hunter.useWhen().startsWith("Use when a recruiter"));
        assertEquals("recruiting", hunter.category());
        assertEquals(List.of("recruiter", "research_analyst", "staffing_agency"), hunter.personas());
        assertEquals("beta", hunter.status());
        assertTrue(hunter.available());
        assertTrue(hunter.needsProvider());
        assertEquals("weekdays", hunter.recommendedCadence());
        assertEquals(List.of("daily", "weekdays", "weekly"), hunter.schedules());
        assertTrue(hunter.needsAttestation());
        assertTrue(hunter.health().isOk());
        assertTrue(hunter.health().degradedSources().isEmpty());
        assertNull(hunter.health().successRate7d());
        assertNull(hunter.health().medianSeconds7d());
        assertEquals(3, hunter.basics().size());
        assertEquals("job_titles", hunter.basics().get(0).name());
        assertEquals("list of strings", hunter.basics().get(0).type());
        assertTrue(hunter.basics().get(0).required());
        assertFalse(hunter.basics().get(2).required());
        assertFalse(formulas.get(0).available());
        assertFalse(formulas.get(0).needsAttestation());
    }

    @Test
    void theCatalogueIsFiltered() {
        List<Formula> formulas = api.answer("formulas_list_filtered").client().formulas()
                .list(FormulaListParams.builder().query("hiring").category("recruiting").build());
        FakeApi.fixture("formulas_list_filtered").assertSent(api.only());
        assertEquals(1, formulas.size());
        assertEquals("job-order-hunter", formulas.get(0).id());

        api.ok("{\"formulas\":[]}");
        api.client().formulas().list(FormulaListParams.builder().persona("recruiter").status("live").build());
        assertEquals("persona=recruiter&status=live", api.last().query);
    }

    @Test
    void oneFormulaComesWithEverythingToRunIt() {
        FormulaDetails formula = api.answer("formulas_get").client().formulas().get("job-order-hunter");
        FakeApi.fixture("formulas_get").assertSent(api.only());
        assertEquals("job-order-hunter", formula.id());
        assertEquals("Job Order Hunter", formula.title());
        assertTrue(formula.description().startsWith("Finds the companies"));
        assertEquals("object", formula.parameters().get("type"));
        assertEquals(List.of("job_titles", "locations"), formula.parameters().get("required"));
        assertFalse(formula.presets().isEmpty());
        assertEquals("it-contract-uk", formula.presets().get(0).id());
        assertEquals("IT contract desk (UK)", formula.presets().get(0).label());
        assertEquals(List.of("London"), formula.presets().get(0).params().get("locations"));
        assertEquals("scrape", formula.steps().get(0).kind());
        assertEquals("Find", formula.steps().get(0).label());
        assertEquals("company_name", formula.columns().get(0).key());
        assertEquals("Company", formula.columns().get(0).label());
        assertEquals("string", formula.columns().get(0).type());
        assertTrue(formula.columns().get(0).defaultVisible());
        assertEquals("company_domain", formula.companyView().key());
        assertEquals("One row per company", formula.companyView().label());
        assertTrue(formula.companyView().lists().contains("job_title"));
        assertTrue(formula.requirements().scrapers().contains("careers.company"));
        assertEquals(List.of("people_search", "contact_find", "email_verify"), formula.requirements().capabilities());
        assertTrue(formula.requirements().capabilitiesConnected().isEmpty());
        assertEquals(List.of("scrapers"), formula.requirements().products());
        assertTrue(formula.requirements().missing().isEmpty());
        assertTrue(formula.requirements().disabledSources().isEmpty());
        assertEquals(List.of("google.jobs"), formula.requirements().notOffered());
        assertEquals(200L, formula.limits().get("default_rows"));
        assertEquals(20L, formula.limits().get("min_unit_cap"));
        assertFalse(formula.sampleRows().isEmpty());
        assertNotNull(formula.sampleRows().get(0).get("company_name"));
        assertFalse(formula.sampleIsFixture());
        assertEquals(2, formula.changelog().size());
        assertTrue(formula.health().isOk());
    }

    // --- estimating ---------------------------------------------------------------------------------------------------

    @Test
    void anEstimateSpendsNothingAndSaysWhatARunWouldCost() {
        FormulaEstimate estimate = api.answer("formulas_estimate").client().formulas()
                .estimate(PARAMS, FormulaRunOptions.builder().unitCap(2000).build());
        FakeApi.fixture("formulas_estimate").assertSent(api.only());
        assertNull(api.only().header("Idempotency-Key"));
        assertTrue(estimate.valid());
        assertTrue(estimate.errors().isEmpty());
        assertEquals(1, estimate.estimate().minUnits());
        assertEquals(63, estimate.estimate().expectedUnits());
        assertEquals(1425, estimate.estimate().maxUnits());
        assertEquals(1, estimate.estimate().expectedRows());
        assertEquals("2 searches across 2 sources, about 1 row", estimate.estimate().basis());
        assertTrue(estimate.missing().isEmpty());
        assertEquals(List.of("google.jobs"), estimate.notOffered());
    }

    @Test
    void parametersThatCannotRunAreAnAnswerNotAnError() {
        FormulaEstimate estimate = api.answer("formulas_estimate_invalid").client().formulas()
                .estimate("job-order-hunter", Map.of("locations", List.of("London")));
        FakeApi.fixture("formulas_estimate_invalid").assertSent(api.only());
        assertFalse(estimate.valid());
        assertNull(estimate.estimate());
        assertEquals(1, estimate.errors().size());
        assertEquals("params.job_titles", estimate.errors().get(0).path());
        assertEquals("This is required.", estimate.errors().get(0).message());
        assertEquals("params.job_titles: This is required.", estimate.errors().get(0).toString());
    }

    @Test
    void anEstimateOfAPreviewSaysSoAndIsRetriedAfterAServerError() {
        api.error(500, "internal", "x").answer("formulas_estimate");
        api.client().formulas().estimate("job-order-hunter", null, FormulaRunOptions.builder().preview(true).rowCap(10).build());
        assertEquals(2, api.count());
        assertEquals("{\"params\":{},\"row_cap\":10,\"mode\":\"preview\"}", api.last().body);
    }

    // --- running ------------------------------------------------------------------------------------------------------

    @Test
    void aRunIsStartedWithItsParamsAndItsOwnIdempotencyKey() {
        FormulaRun run = api.answer("formulas_run_queued").client().formulas().run(PARAMS,
                FormulaRunOptions.builder().unitCap(2000).idempotencyKey("c0ffee-run-1").build());
        FakeApi.fixture("formulas_run_queued").assertSent(api.only());
        assertEquals("frun_000000000000000000000002", run.id());
        assertEquals("job-order-hunter", run.formulaId());
        assertEquals("1.1.0", run.version());
        assertNull(run.savedFormulaId());
        assertEquals(RunStatus.QUEUED, run.status());
        assertEquals("queued", run.rawStatus());
        assertFalse(run.isFinished());
        assertFalse(run.isSuccessful());
        assertNull(run.reason());
        assertTrue(run.reasons().isEmpty());
        assertEquals("full", run.mode());
        assertFalse(run.isPreview());
        assertEquals("api", run.trigger());
        assertEquals(200, run.params().get("max_results"));
        assertEquals(63, run.estimate().expectedUnits());
        assertEquals(2000, run.unitCap());
        assertEquals(200, run.rowCap());
        assertEquals(0, run.unitsSpent());
        assertEquals(0, run.rowsTotal());
        assertFalse(run.includeSeen());
        assertNull(run.scheduledFor());
        assertTrue(run.steps().isEmpty());
        assertEquals(Instant.parse("2026-11-25T09:00:02.250Z"), run.createdAt());
        assertNull(run.startedAt());
        assertNull(run.finishedAt());
        Standing standing = api.client().standing().orElse(null);
        assertNull(standing);                                    // a new client has seen nothing yet
    }

    @Test
    void aPreviewThatEndedWithinTheWaitIsReadWhole() {
        EnrichedNowClient client = api.client();
        api.answer("formulas_run");
        FormulaRun run = client.formulas().run("job-order-hunter", paramsAsMap(), FormulaRunOptions.builder().preview(true)
                .wait(Duration.ofSeconds(60)).idempotencyKey("c0ffee-run-2").build());
        FakeApi.fixture("formulas_run").assertSent(api.only());
        assertEquals(RunStatus.PARTIAL, run.status());
        assertTrue(run.isFinished());
        assertTrue(run.isSuccessful());
        assertTrue(run.isPreview());
        assertEquals("contacts_not_connected", run.reason());
        assertEquals(2, run.reasons().size());
        assertEquals("source_not_offered", run.reasons().get(1).code());
        assertEquals("search_google_jobs", run.reasons().get(1).values().get("step"));
        assertEquals(50, run.unitCap());
        assertEquals(10, run.rowCap());
        assertEquals(17, run.unitsSpent());
        assertEquals(4, run.rowsTotal());
        FormulaRunStep linkedSearch = run.steps().stream().filter(s -> s.id().equals("search_linkedin")).findFirst().orElseThrow();
        assertEquals("scrape", linkedSearch.kind());
        assertEquals("done", linkedSearch.status());
        assertTrue(linkedSearch.recordsOut() > 0);
        assertEquals(1, linkedSearch.calls());
        assertNotNull(linkedSearch.startedAt());
        FormulaRunStep skipped = run.steps().get(0);
        assertEquals("skipped", skipped.status());
        assertNull(skipped.startedAt());
        assertNotNull(skipped.finishedAt());
        assertTrue(skipped.errors().isEmpty());
        assertEquals(959L, client.standing().orElseThrow().unitsRemaining());
    }

    @Test
    void eachRunGetsItsOwnKeyAndARetryKeepsIt() {
        api.answer("formulas_run_queued").answer("formulas_run_queued");
        api.client().formulas().run(PARAMS);
        api.client().formulas().run(PARAMS);
        String first = api.received().get(0).header("Idempotency-Key");
        assertEquals(36, first.length());
        assertFalse(first.equals(api.received().get(1).header("Idempotency-Key")));

        api.error(503, "internal", "x").answer("formulas_run_queued");
        api.client().formulas().run(PARAMS);
        List<FakeApi.Received> all = api.received();
        assertEquals(all.get(2).header("Idempotency-Key"), all.get(3).header("Idempotency-Key"));
    }

    @Test
    void aRunSendsNoDefaults() {
        api.answer("formulas_run_queued");
        api.client().formulas().run("job-order-hunter", null);
        assertEquals("{\"params\":{}}", api.only().body);
        assertNull(api.only().query);
    }

    // --- typed parameters ---------------------------------------------------------------------------------------------

    @Test
    void typedParametersAreSentAsTheirMap() {
        api.answer("formulas_run_queued").answer("formulas_run_queued");
        api.client().formulas().run("job-order-hunter", PARAMS, FormulaRunOptions.builder().unitCap(2000).idempotencyKey("c0ffee-run-1").build());
        FakeApi.fixture("formulas_run_queued").assertSent(api.received().get(0));
        api.client().formulas().run("job-order-hunter", paramsAsMap(), FormulaRunOptions.builder().unitCap(2000).idempotencyKey("c0ffee-run-1").build());
        FakeApi.fixture("formulas_run_queued").assertSent(api.received().get(1));
    }

    @Test
    void typedParametersOfAnotherFormulaAreRefusedBeforeSending() {
        TalentSourcingParams other = TalentSourcingParams.builder().jobTitle("Nurse").location("Texas").build();
        IllegalArgumentException run = assertThrows(IllegalArgumentException.class, () -> api.client().formulas().run("job-order-hunter", other));
        assertTrue(run.getMessage().contains("talent-sourcing") && run.getMessage().contains("job-order-hunter"), run.getMessage());
        assertThrows(IllegalArgumentException.class, () -> api.client().formulas().estimate("job-order-hunter", other));
        assertThrows(IllegalArgumentException.class, () -> api.client().runFormula("job-order-hunter", other));
        assertThrows(IllegalArgumentException.class, () -> SavedFormulaCreate.builder("job-order-hunter", other));
        assertThrows(IllegalArgumentException.class, () -> api.client().async().formulas().run("job-order-hunter", other, null));
        assertEquals(0, api.count());
    }

    @Test
    void badOptionsAreRefused() {
        assertThrows(IllegalArgumentException.class, () -> FormulaRunOptions.builder().unitCap(0));
        assertThrows(IllegalArgumentException.class, () -> FormulaRunOptions.builder().rowCap(-1));
        assertThrows(IllegalArgumentException.class, () -> FormulaRunOptions.waitUpTo(Duration.ofSeconds(301)));
        assertThrows(IllegalArgumentException.class, () -> FormulaRunOptions.builder().idempotencyKey("has space"));
        assertThrows(IllegalArgumentException.class, () -> api.client().formulas().get(""));
        assertTrue(FormulaRunOptions.preview().isPreview());
        assertEquals(Duration.ofMinutes(10), FormulaRunOptions.none().timeout());
        assertTrue(FormulaRunOptions.none().throwOnFailure());
    }

    @Test
    void aFormulaIdIsEncodedIntoThePath() {
        api.answer("formulas_get");
        api.client().formulas().get("a b/c");
        assertEquals("/v1/formulas/a%20b%2Fc", api.only().path);
        Fixture fixture = FakeApi.fixture("formulas_get");
        assertEquals("job-order-hunter", fixture.body().path("id").asText());
    }
}
