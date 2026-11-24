package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.errors.ApiException;
import com.enrichednow.errors.AttestationRequiredException;
import com.enrichednow.errors.ConflictException;
import com.enrichednow.errors.FormulaUnavailableException;
import com.enrichednow.errors.LimitReachedException;
import com.enrichednow.errors.NotFoundException;
import com.enrichednow.errors.PermissionDeniedException;
import com.enrichednow.errors.PlanLimitReachedException;
import com.enrichednow.errors.QuotaExceededException;
import com.enrichednow.errors.RequirementMissingException;
import com.enrichednow.errors.SavedFormulaArchivedException;
import com.enrichednow.errors.ValidationException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Every error code of the formula routes, from the recorded answers, as its own exception. */
class FormulaErrorsTest {
    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    @Test
    void attestationRequiredSaysWhichTerms() {
        api.answer("error_attestation_required");
        ConflictException caught = assertThrows(ConflictException.class, () -> api.client().formulas().run(FormulasTest.PARAMS,
                FormulaRunOptions.builder().idempotencyKey("c0ffee-attest").build()));
        FakeApi.fixture("error_attestation_required").assertSent(api.only());
        AttestationRequiredException e = assertInstanceOf(AttestationRequiredException.class, caught);
        assertEquals(409, e.statusCode());
        assertEquals("attestation_required", e.code());
        assertEquals("personal_data", e.kind());
        assertEquals("2026-11-24", e.textVersion());
        assertTrue(e.apiMessage().contains("accept the terms"));
        assertEquals(1, api.count());                                   // not retried
    }

    @Test
    void anUnknownFormulaIsNotFound() {
        api.answer("error_formula_not_found");
        NotFoundException e = assertThrows(NotFoundException.class, () -> api.client().formulas().get("no-such-formula"));
        FakeApi.fixture("error_formula_not_found").assertSent(api.only());
        assertEquals(404, e.statusCode());
        assertEquals("formula_not_found", e.code());
    }

    @Test
    void aFormulaThatCannotRunNowSaysWhy() {
        api.answer("error_formula_unavailable");
        FormulaUnavailableException e = assertThrows(FormulaUnavailableException.class, () -> api.client().formulas()
                .run("retired-formula", Map.of("terms", List.of("java")), FormulaRunOptions.builder().idempotencyKey("c0ffee-retired").build()));
        FakeApi.fixture("error_formula_unavailable").assertSent(api.only());
        assertEquals("formula_unavailable", e.code());
        assertEquals("deprecated", e.formulaStatus());
        assertNull(e.formulaId());
        assertNull(e.major());
        assertInstanceOf(ConflictException.class, e);

        api.reply(409, FakeApi.errorBody("formula_unavailable", "No longer offered.", "{\"formula_id\":\"old-one\",\"major\":1}"));
        FormulaUnavailableException saved = assertThrows(FormulaUnavailableException.class, () -> api.client().savedFormulas().run("sf_1"));
        assertEquals("old-one", saved.formulaId());
        assertEquals(1L, saved.major());
    }

    @Test
    void aMissingRequirementNamesWhatIsMissing() {
        api.answer("error_requirement_missing");
        RequirementMissingException e = assertThrows(RequirementMissingException.class, () -> api.client().formulas()
                .run("data-formula", Map.of("terms", List.of("java")), FormulaRunOptions.builder().idempotencyKey("c0ffee-data").build()));
        FakeApi.fixture("error_requirement_missing").assertSent(api.only());
        assertEquals(List.of("product:data"), e.missing());
        assertThrows(UnsupportedOperationException.class, () -> e.missing().clear());
    }

    @Test
    void aUnitCapTooLowSaysTheLeastItTakes() {
        api.answer("error_unit_cap_too_low");
        ValidationException e = assertThrows(ValidationException.class, () -> api.client().formulas().run(FormulasTest.PARAMS,
                FormulaRunOptions.builder().unitCap(1).idempotencyKey("c0ffee-cap").build()));
        FakeApi.fixture("error_unit_cap_too_low").assertSent(api.only());
        assertEquals(422, e.statusCode());
        assertEquals("unit_cap_too_low", e.code());
        assertEquals(20L, e.minUnitCap());
        assertTrue(e.fieldErrors().isEmpty());
    }

    @Test
    void aSavedFormulasMonthlyCapIsALimitReached() {
        api.answer("error_limit_reached");
        QuotaExceededException caught = assertThrows(QuotaExceededException.class, () -> api.client().savedFormulas()
                .run("sf_000000000000000000000001", SavedFormulaRunOptions.builder().idempotencyKey("c0ffee-saved-cap").build()));
        FakeApi.fixture("error_limit_reached").assertSent(api.only());
        LimitReachedException e = assertInstanceOf(LimitReachedException.class, caught);
        assertEquals("saved_formula", e.scope());
        assertEquals("sf_000000000000000000000001", e.savedFormulaId());
        assertNull(e.keyId());
        assertEquals(3L, e.limitUnits());
        assertEquals(4L, e.usedUnits());
        assertEquals(10L, e.unitsNeeded());
        assertEquals(1, api.count());

        api.reply(402, FakeApi.errorBody("limit_reached", "The price changed.", "{\"scope\":\"formula_run\",\"units_needed\":7}"));
        LimitReachedException price = assertThrows(LimitReachedException.class, () -> api.client().formulas().run(FormulasTest.PARAMS));
        assertEquals("formula_run", price.scope());
        assertNull(price.savedFormulaId());
        assertEquals(7L, price.unitsNeeded());
    }

    @Test
    void aPlanLimitIsAPermissionDeniedThatSaysWhichLimit() {
        api.answer("error_plan_limit_reached");
        PermissionDeniedException caught = assertThrows(PermissionDeniedException.class, () -> api.client().savedFormulas().create(
                SavedFormulaCreate.builder("posting-watch", Map.of("terms", List.of("java")))
                        .schedule(Schedule.hourly("07:30", "Europe/London")).build()));
        FakeApi.fixture("error_plan_limit_reached").assertSent(api.only());
        PlanLimitReachedException e = assertInstanceOf(PlanLimitReachedException.class, caught);
        assertEquals(403, e.statusCode());
        assertEquals("min_interval_minutes", e.limit());
        assertEquals(1440L, e.allowed());
        assertEquals(60L, e.asked());
        assertNull(e.current());

        api.reply(403, FakeApi.errorBody("plan_limit_reached", "Too many.", "{\"limit\":\"schedules\",\"allowed\":3,\"current\":3}"));
        PlanLimitReachedException schedules = assertThrows(PlanLimitReachedException.class,
                () -> api.client().savedFormulas().resume("sf_1"));
        assertEquals("schedules", schedules.limit());
        assertEquals(3L, schedules.current());

        api.reply(403, FakeApi.errorBody("forbidden", "No.", "{}"));
        PermissionDeniedException plain = assertThrows(PermissionDeniedException.class, () -> api.client().savedFormulas().resetSeen("sf_1"));
        assertFalse(plain instanceof PlanLimitReachedException);
    }

    @Test
    void anArchivedSavedFormulaCannotBeChanged() {
        api.answer("error_saved_formula_archived");
        SavedFormulaArchivedException e = assertThrows(SavedFormulaArchivedException.class, () -> api.client().savedFormulas()
                .update("sf_000000000000000000000002", SavedFormulaChange.builder().notify(Notify.NONE).build()));
        FakeApi.fixture("error_saved_formula_archived").assertSent(api.only());
        assertEquals("saved_formula_archived", e.code());
        assertNull(e.savedFormulaId());                                 // the answer to a change does not name it
        assertInstanceOf(ConflictException.class, e);
        assertEquals(1, api.count());

        api.reply(409, FakeApi.errorBody("saved_formula_archived", "Archived.", "{\"saved_formula_id\":\"sf_9\"}"));
        SavedFormulaArchivedException run = assertThrows(SavedFormulaArchivedException.class, () -> api.client().savedFormulas().run("sf_9"));
        assertEquals("sf_9", run.savedFormulaId());
    }

    @Test
    void anIdempotencyConflictIsAPlainConflict() {
        api.reply(409, FakeApi.errorBody("idempotency_conflict", "Used.", "{\"run_id\":\"frun_1\"}"));
        ApiException e = assertThrows(ApiException.class, () -> api.client().formulas().run(FormulasTest.PARAMS,
                FormulaRunOptions.builder().idempotencyKey("import-1").build()));
        assertSame(ConflictException.class, e.getClass());
        assertEquals("idempotency_conflict", e.code());
        assertEquals(1, api.count());
    }

    @Test
    void theMappingIsByStatusAndCode() {
        assertSame(ConflictException.class, ApiException.of(409, null, null, null, null, null).getClass());
        assertSame(AttestationRequiredException.class, ApiException.of(409, "attestation_required", null, null, null, null).getClass());
        assertSame(FormulaUnavailableException.class, ApiException.of(409, "formula_unavailable", null, null, null, null).getClass());
        assertSame(RequirementMissingException.class, ApiException.of(409, "requirement_missing", null, null, null, null).getClass());
        assertSame(SavedFormulaArchivedException.class, ApiException.of(409, "saved_formula_archived", null, null, null, null).getClass());
        assertSame(PlanLimitReachedException.class, ApiException.of(403, "plan_limit_reached", null, null, null, null).getClass());
        assertSame(NotFoundException.class, ApiException.of(404, "formula_not_found", null, null, null, null).getClass());
        assertSame(ValidationException.class, ApiException.of(422, "unit_cap_too_low", null, null, null, null).getClass());
        assertTrue(((RequirementMissingException) ApiException.of(409, "requirement_missing", null, null, null, null)).missing().isEmpty());
    }
}
