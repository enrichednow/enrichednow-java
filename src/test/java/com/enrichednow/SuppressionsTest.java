package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Fixture;
import com.enrichednow.errors.NotFoundException;
import com.enrichednow.models.Attestation;
import com.enrichednow.models.RowFeedback;
import com.enrichednow.models.Suppression;
import com.enrichednow.models.SuppressionUpload;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** The suppression list, the attestations and row feedback, against the recorded answers of the real routes. */
class SuppressionsTest {
    private static final String CSV = FakeApi.fixture("suppressions_upload").request.path("text").asText();

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    // --- suppressions -------------------------------------------------------------------------------------------------

    @Test
    void entriesAreAdded() {
        int added = api.answer("suppressions_add").client().suppressions().add(List.of(
                SuppressionEntry.domain("https://www.globex.example/"),
                SuppressionEntry.email("Ana@Initech.example"),
                SuppressionEntry.companyName("Umbrella, Inc.")));
        FakeApi.fixture("suppressions_add").assertSent(api.only());
        assertEquals(3, added);
    }

    @Test
    void addingIsSafeToSendAgainAndSaysWhereEntriesCameFrom() {
        api.error(500, "internal", "x").answer("suppressions_add");
        api.client().suppressions().add(List.of(SuppressionEntry.of(SuppressionKind.DOMAIN, "globex.example")), "upload");
        assertEquals(2, api.count());
        assertEquals("{\"entries\":[{\"kind\":\"domain\",\"value\":\"globex.example\"}],\"source\":\"upload\"}", api.last().body);

        api.answer("suppressions_add");
        api.client().suppressions().add(SuppressionEntry.email("a@b.example"));
        assertEquals("{\"entries\":[{\"kind\":\"email\",\"value\":\"a@b.example\"}]}", api.last().body);
    }

    @Test
    void anEmptyOrTooLongListIsRefusedBeforeSending() {
        assertThrows(IllegalArgumentException.class, () -> api.client().suppressions().add(Collections.emptyList()));
        List<SuppressionEntry> many = new ArrayList<>(Collections.nCopies(10_001, SuppressionEntry.domain("a.example")));
        assertThrows(IllegalArgumentException.class, () -> api.client().suppressions().add(many));
        assertEquals(0, api.count());
    }

    @Test
    void theListIsReadAcrossPages() {
        Fixture fixture = FakeApi.fixture("suppressions_list");
        api.answer("suppressions_list").ok("{\"suppressions\":[],\"next_cursor\":null}");
        List<Suppression> all = api.client().suppressions().list(SuppressionListParams.builder().limit(2).build()).toList();
        fixture.assertSent(api.received().get(0));
        assertEquals(Map.of("limit", "2", "cursor", fixture.body().path("next_cursor").asText()), Fixture.queryOf(api.received().get(1)));
        assertEquals(2, all.size());
        assertEquals("company_name", all.get(0).kind());
        assertEquals("umbrella", all.get(0).value());
        assertEquals("manual", all.get(0).source());
        assertEquals(Instant.parse("2026-11-25T09:04:28.250Z"), all.get(0).createdAt());

        api.ok("{\"suppressions\":[],\"next_cursor\":null}");
        api.client().suppressions().listPage(SuppressionListParams.builder().kind(SuppressionKind.EMAIL).cursor("c9").build());
        assertEquals("kind=email&cursor=c9", api.last().query);
    }

    @Test
    void aCsvFileIsUploadedAsItIs() {
        SuppressionUpload upload = api.answer("suppressions_upload").client().suppressions().upload(CSV);
        FakeApi.fixture("suppressions_upload").assertSent(api.only());
        assertEquals("text/csv", api.only().header("Content-Type"));
        assertEquals(3, upload.added());
        assertEquals(0, upload.already());
        assertEquals(1, upload.invalid().size());
        assertEquals(5, upload.invalid().get(0).index());
        assertTrue(upload.invalid().get(0).message().contains("not an address"));
    }

    @Test
    void aCsvFileIsUploadedFromBytesAPathOrAStream(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("suppress.csv");
        Files.write(file, CSV.getBytes(StandardCharsets.UTF_8));
        api.answer("suppressions_upload").answer("suppressions_upload").answer("suppressions_upload");
        api.client().suppressions().upload(CSV.getBytes(StandardCharsets.UTF_8));
        api.client().suppressions().upload(file);
        api.client().suppressions().upload(new ByteArrayInputStream(CSV.getBytes(StandardCharsets.UTF_8)));
        for (FakeApi.Received sent : api.received()) {
            FakeApi.fixture("suppressions_upload").assertSent(sent);
        }

        api.answer("suppressions_upload");
        api.client().suppressions().upload("acme.example\nglobex.example\n", SuppressionKind.DOMAIN);
        assertEquals("kind=domain", api.last().query);
        assertEquals("acme.example\nglobex.example\n", api.last().body);
        assertThrows(IllegalArgumentException.class, () -> api.client().suppressions().upload(new byte[0]));
        assertThrows(java.io.UncheckedIOException.class, () -> api.client().suppressions().upload(dir.resolve("missing.csv")));
    }

    @Test
    void anEntryIsRemoved() {
        api.answer("suppressions_delete").client().suppressions().delete(SuppressionKind.DOMAIN, "globex.example");
        FakeApi.fixture("suppressions_delete").assertSent(api.only());

        api.error(404, "not_found", "That is not on the suppression list.");
        assertThrows(NotFoundException.class, () -> api.client().suppressions().delete(SuppressionKind.EMAIL, "a b@c.example"));
        assertEquals("/v1/suppressions/email/a%20b%40c.example", api.last().path);
    }

    // --- attestations -------------------------------------------------------------------------------------------------

    @Test
    void theTermsAreListed() {
        List<Attestation> terms = api.answer("attestations_list").client().attestations().list();
        FakeApi.fixture("attestations_list").assertSent(api.only());
        assertEquals(1, terms.size());
        Attestation personal = terms.get(0);
        assertEquals("personal_data", personal.kind());
        assertEquals("Formulas that return people", personal.title());
        assertTrue(personal.text().startsWith("Some formulas return"));
        assertEquals("2026-11-24", personal.textVersion());
        assertTrue(personal.accepted());
        assertEquals(Instant.parse("2026-11-25T09:00:01.250Z"), personal.acceptedAt());
        assertEquals("ada@example.com", personal.acceptedBy());
    }

    // --- row feedback -------------------------------------------------------------------------------------------------

    @Test
    void aRowIsSaidToHaveTheWrongCompany() {
        RowFeedback feedback = api.answer("formula_rows_feedback").client().formulaRows().feedback("frow_000000000000000000000001",
                RowVerdict.WRONG_COMPANY, "This is the agency, not the employer.", "corvid.example");
        FakeApi.fixture("formula_rows_feedback").assertSent(api.only());
        assertEquals("frow_000000000000000000000001", feedback.rowId());
        assertEquals("wrong_company", feedback.verdict());
        assertEquals("flagged", feedback.status());
        assertEquals("Corvid", feedback.correction().companyName());
        assertEquals("UK", feedback.correction().country());
        assertEquals("corvid.example", feedback.correction().domain());
    }

    @Test
    void feedbackSendsOnlyWhatIsGivenAndIsSafeToSendAgain() {
        api.error(500, "internal", "x").ok("{\"row_id\":\"frow_1\",\"verdict\":\"good\",\"status\":\"new\",\"correction\":null}");
        RowFeedback feedback = api.client().formulaRows().feedback("frow_1", RowVerdict.GOOD);
        assertEquals(2, api.count());
        assertEquals("{\"verdict\":\"good\"}", api.last().body);
        assertNull(feedback.correction());
        assertFalse(feedback.status().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> api.client().formulaRows().feedback("frow_1", null));
    }
}
