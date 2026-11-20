package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Received;
import com.enrichednow.FakeApi.Reply;
import com.enrichednow.errors.EnrichedNowException;
import com.enrichednow.errors.QuotaExceededException;
import com.enrichednow.errors.ServerException;
import com.enrichednow.errors.WebhookVerificationException;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.RecordsExport;
import com.enrichednow.models.RunStatus;
import com.enrichednow.models.Standing;
import com.enrichednow.models.WebhookDelivery;
import com.enrichednow.models.WebhookEndpoint;
import com.enrichednow.models.WebhookEndpointList;
import com.enrichednow.models.WebhookEvent;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** What the API has beyond the first release: webhooks, record formats, the standing, unmetered, longer waits. */
class ParityTest {
    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    private static Reply standing(Reply reply, String remaining) {
        return reply.header("X-Units-Limit", "1000").header("X-Units-Used", "120").header("X-Units-Remaining", remaining)
                .header("X-Units-Topup", "50").header("X-Units-Reset", "2026-12-01T00:00:00Z")
                .header("X-RateLimit-Limit", "2").header("X-RateLimit-Remaining", "1");
    }

    private static Reply csv(String body, String cursor) {
        Reply reply = new Reply(200, body).header("Content-Type", "text/csv; charset=utf-8");
        return cursor == null ? reply : reply.header("X-Next-Cursor", cursor);
    }

    // --- the standing -------------------------------------------------------------------------------------------------

    @Test
    void theStandingOfTheLastAnswerThatCarriedItIsKeptAnErrorIncluded() {
        EnrichedNowClient client = api.client();
        api.ok("{\"scrapers\":[]}");
        client.scrapers().list();
        assertTrue(client.standing().isEmpty());
        api.reply(standing(new Reply(200, FakeApi.run("run_1", "ok", null)), "880"));
        client.runs().get("run_1");
        Standing seen = client.standing().orElseThrow();
        assertEquals(1000L, seen.unitsLimit());
        assertEquals(880L, seen.unitsRemaining());
        assertEquals(930L, seen.unitsAvailable());
        assertEquals(2L, seen.runsLimit());
        assertEquals(1L, seen.runsRemaining());
        assertEquals(Instant.parse("2026-12-01T00:00:00Z"), seen.unitsReset());
        assertFalse(seen.unmetered());
        api.reply(standing(new Reply(402, FakeApi.errorBody("quota_exceeded", "No.", "{}")), "0"));
        assertThrows(QuotaExceededException.class, () -> client.scrapers().run("ats.board", Map.of()));
        assertEquals(50L, client.standing().orElseThrow().unitsAvailable());
    }

    @Test
    void usageStandingAsksForItAndUsageSaysUnmetered() {
        api.reply(standing(new Reply(200, "{\"runs\":[],\"next_cursor\":null}"), "880").header("X-Units-Unmetered", "true"));
        Standing standing = api.client().usage().standing();
        assertTrue(standing.unmetered());
        assertEquals("GET /v1/runs?limit=1", api.only().toString());
        api.ok("{\"period\":{\"from\":\"2026-11-01\",\"to\":\"2026-11-19\"},\"total_units\":3,\"allowance_units\":1000,\"unmetered\":true,\"rows\":[]}");
        assertTrue(api.client().usage().get().unmetered());
    }

    // --- waiting and listing ------------------------------------------------------------------------------------------

    @Test
    void aWaitOfUpTo300SecondsAndEveryRunFilter() {
        EnrichedNowClient client = api.client();
        api.ok(FakeApi.run("run_1", "ok", null));
        client.runs().get("run_1", Duration.ofSeconds(120));
        assertEquals("wait=120", api.last().query);
        api.ok(FakeApi.run("run_1", "ok", null));
        client.scrapers().run("ats.board", Map.of(), RunOptions.builder().wait(Duration.ofSeconds(300)).webhookUrl("https://example.com/hook").build());
        assertEquals("wait=300", api.last().query);
        assertTrue(api.last().body.contains("\"webhook_url\":\"https://example.com/hook\""), api.last().body);
        assertThrows(IllegalArgumentException.class, () -> RunOptions.waitUpTo(Duration.ofSeconds(301)));
        assertThrows(IllegalArgumentException.class, () -> client.runs().get("run_1", Duration.ofSeconds(-1)));
        api.ok("{\"runs\":[],\"next_cursor\":null}");
        client.runs().listPage(RunListParams.builder().apiKeyId("key_1").from(Instant.parse("2026-11-01T00:00:00Z"))
                .to(Instant.parse("2026-11-19T00:00:00Z")).status(RunStatus.OK).build());
        assertEquals("status=ok&api_key_id=key_1&from=2026-11-01T00%3A00%3A00Z&to=2026-11-19T00%3A00%3A00Z", api.last().query);
    }

    // --- formats ------------------------------------------------------------------------------------------------------

    @Test
    void exportIsOnePageAndExportAllIsTheWholeRunWithTheHeaderOnce() {
        EnrichedNowClient client = api.client();
        api.reply(csv("a,b\r\n1,2\r\n", "c2"));
        RecordsExport page = client.runs().exportPage("run_1", RecordFormat.CSV, 1, null);
        assertEquals("format=csv&limit=1", api.only().query);
        assertEquals("text/csv", api.only().header("Accept"));
        assertEquals("a,b\r\n1,2\r\n", page.text());
        assertEquals("c2", page.nextCursor());
        assertFalse(page.expired());

        api.reply(csv("a,b\r\n1,2\r\n", "c2")).reply(csv("a,b\r\n3,4\r\n", "c3")).reply(csv("a,b\r\n5,6\r\n", null));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.runs().exportAll("run_1", RecordFormat.CSV, out);
        assertEquals("a,b\r\n1,2\r\n3,4\r\n5,6\r\n", out.toString(StandardCharsets.UTF_8));
        List<Received> all = api.received();
        assertEquals("format=csv&limit=1000&cursor=c3", all.get(all.size() - 1).query);
    }

    @Test
    void anExportOfRecordsThatAreGone() {
        api.reply(new Reply(200, "").header("X-Records-Expired", "true"));
        assertTrue(api.client().runs().exportPage("run_1", RecordFormat.NDJSON, null, null).expired());
        api.reply(new Reply(200, "").header("X-Records-Expired", "true"));
        assertThrows(EnrichedNowException.class, () -> api.client().runs().exportAll("run_1", RecordFormat.NDJSON, new ByteArrayOutputStream()));
    }

    @Test
    void aFileARunProduced() {
        api.reply(new Reply(200, "PNG").header("Content-Type", "image/png"));
        assertArrayEquals("PNG".getBytes(StandardCharsets.UTF_8), api.client().runs().file("run_1", "screenshot.png"));
        assertEquals("GET /v1/runs/run_1/files/screenshot.png", api.only().toString());
    }

    // --- webhooks -----------------------------------------------------------------------------------------------------

    private static final String ENDPOINT = "{\"id\":\"whk_1\",\"url\":\"https://example.com/hook\",\"description\":\"prod\","
            + "\"events\":[\"run.succeeded\"],\"enabled\":true,\"disabled_reason\":null,\"disabled_at\":null,\"last_success_at\":null,"
            + "\"last_failure_at\":null,\"created_at\":\"2026-11-19T10:00:00Z\"";

    @Test
    void webhookEndpoints() {
        EnrichedNowClient client = api.client();
        api.ok("{\"endpoints\":[" + ENDPOINT + "}],\"run_secret_set\":true}");
        WebhookEndpointList list = client.webhooks().list();
        assertEquals("whk_1", list.endpoints().get(0).id());
        assertTrue(list.runSecretSet());
        api.reply(201, ENDPOINT + ",\"secret\":\"whsec_new\"}");
        WebhookEndpoint made = client.webhooks().create("https://example.com/hook", List.of("run.succeeded"), "prod");
        assertEquals("whsec_new", made.secret());
        assertEquals("{\"url\":\"https://example.com/hook\",\"events\":[\"run.succeeded\"],\"description\":\"prod\"}", api.last().body);
        api.ok(ENDPOINT + "}");
        client.webhooks().update("whk_1", WebhookChange.builder().enabled(false).build());
        assertEquals("PATCH /v1/webhooks/whk_1", api.last().toString());
        assertEquals("{\"enabled\":false}", api.last().body);
        assertThrows(IllegalArgumentException.class, () -> client.webhooks().update("whk_1", WebhookChange.builder().build()));
        api.ok(ENDPOINT + "}");
        client.webhooks().get("whk_1");
        api.reply(204, "");
        client.webhooks().delete("whk_1");
        assertEquals("DELETE /v1/webhooks/whk_1", api.last().toString());
        api.ok(ENDPOINT + ",\"secret\":\"whsec_rotated\"}");
        assertEquals("whsec_rotated", client.webhooks().rotateSecret("whk_1").secret());
        api.ok("{\"secret\":\"whsec_run\"}");
        assertEquals("whsec_run", client.webhooks().createRunSecret());
    }

    @Test
    void creatingAnEndpointIsNotSentAgain() {
        api.reply(500, FakeApi.errorBody("internal", "Oops.", "{}"));
        assertThrows(ServerException.class, () -> api.client().webhooks().create("https://x", WebhookEndpoint.EVENTS, null));
        assertEquals(1, api.count());
    }

    @Test
    void webhookDeliveries() {
        String delivery = "{\"id\":\"whd_1\",\"endpoint_id\":\"whk_1\",\"run_id\":\"run_1\",\"event_id\":\"evt_1\",\"event\":\"run.succeeded\","
                + "\"url\":\"https://x\",\"status\":\"failed\",\"attempts\":3,\"max_attempts\":8,\"next_attempt_at\":null,\"response_status\":500,"
                + "\"error\":\"HTTP 500\",\"duration_ms\":12,\"last_attempt_at\":\"2026-11-19T10:00:00Z\",\"created_at\":\"2026-11-19T10:00:00Z\","
                + "\"finished_at\":null,\"resend_of\":null}";
        api.ok("{\"deliveries\":[" + delivery + "],\"next_cursor\":\"c2\"}").ok("{\"deliveries\":[" + delivery + "],\"next_cursor\":null}");
        List<WebhookDelivery> all = new ArrayList<>();
        api.client().webhooks().deliveries(WebhookDeliveryListParams.builder().status("failed").runId("run_1").build()).forEach(all::add);
        assertEquals(2, all.size());
        assertEquals(500, all.get(0).responseStatus());
        assertEquals("run_id=run_1&status=failed", api.received().get(0).query);
        api.reply(201, delivery);
        assertEquals("evt_1", api.client().webhooks().resend("whd_1").eventId());
    }

    // --- verifying ----------------------------------------------------------------------------------------------------

    private static String sign(String secret, String timestamp, String body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        StringBuilder hex = new StringBuilder("v1=");
        for (byte b : mac.doFinal((timestamp + "." + body).getBytes(StandardCharsets.UTF_8))) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    @Test
    void aWebhookIsVerifiedAndParsed() throws Exception {
        String secret = "whsec_test";
        String body = "{\"id\":\"evt_1\",\"type\":\"run.succeeded\",\"created_at\":\"2026-11-19T10:00:00Z\",\"data\":{\"run\":"
                + FakeApi.run("run_1", "ok", null) + "}}";
        String now = String.valueOf(Instant.now().getEpochSecond());
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        WebhookEvent event = Webhooks.parse(secret, now, bytes, sign(secret, now, body));
        assertEquals("evt_1", event.id());
        assertEquals("run.succeeded", event.type());
        assertEquals("run_1", event.run().id());
        assertNull(event.run().unitsPerRequest());                // an event from before costs differed
        String rated = body.replace("\"units\":1,", "\"units\":1,\"units_per_request\":null,\"units_per_record\":3,");
        WebhookEvent ratedEvent = Webhooks.parse(secret, now, rated.getBytes(StandardCharsets.UTF_8), sign(secret, now, rated));
        assertEquals(3, ratedEvent.run().unitsPerRecord());
        assertNull(ratedEvent.run().unitsPerRequest());
        String old = String.valueOf(Instant.now().getEpochSecond() - 301);
        assertFalse(Webhooks.verify("whsec_other", now, bytes, sign(secret, now, body)));
        assertFalse(Webhooks.verify(secret, now, (body + " ").getBytes(StandardCharsets.UTF_8), sign(secret, now, body)));
        assertFalse(Webhooks.verify(secret, old, bytes, sign(secret, old, body)));
        assertTrue(Webhooks.verify(secret, old, bytes, sign(secret, old, body), Duration.ofSeconds(400)));
        assertFalse(Webhooks.verify(secret, now, bytes, sign(secret, now, body).toUpperCase()));
        assertFalse(Webhooks.verify(secret, "soon", bytes, sign(secret, "soon", body)));
        assertFalse(Webhooks.verify(secret, now, bytes, null));
        assertFalse(Webhooks.verify("", now, bytes, sign("x", now, body)));
        assertThrows(WebhookVerificationException.class, () -> Webhooks.parse(secret, now, "[1]".getBytes(StandardCharsets.UTF_8), sign(secret, now, "[1]")));
        assertThrows(WebhookVerificationException.class, () -> Webhooks.parse("whsec_other", now, bytes, sign(secret, now, body)));
    }

    // --- the asynchronous client --------------------------------------------------------------------------------------

    @Test
    void theAsyncClientHasTheSame() {
        EnrichedNowClient client = api.client();
        api.reply(standing(new Reply(200, FakeApi.run("run_1", "ok", null)), "880"));
        assertEquals("run_1", client.async().runs().get("run_1", Duration.ofSeconds(30)).join().id());
        api.reply(csv("a\r\n1\r\n", null));
        assertEquals("a\r\n1\r\n", client.async().runs().exportPage("run_1", RecordFormat.CSV, null, null).join().text());
        api.reply(standing(new Reply(200, "{\"runs\":[],\"next_cursor\":null}"), "10"));
        assertEquals(60L, client.async().usage().standing().join().unitsAvailable());
        api.ok("{\"endpoints\":[],\"run_secret_set\":false}");
        assertFalse(client.async().webhooks().list().join().runSecretSet());
    }
}
