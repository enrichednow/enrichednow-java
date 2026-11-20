package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Received;
import com.enrichednow.errors.EnrichedNowException;
import com.enrichednow.models.Scraper;
import com.enrichednow.models.ScraperDetail;
import com.enrichednow.models.Usage;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ClientTest {
    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    @Test
    void aMissingKeyIsRefusedWithWordsThatSayWhatToDo() {
        EnrichedNowException e = assertThrows(EnrichedNowException.class,
                () -> EnrichedNowClient.builder().environment(name -> null).build());
        assertTrue(e.getMessage().contains("ENRICHEDNOW_API_KEY"), e.getMessage());
        assertThrows(EnrichedNowException.class, () -> EnrichedNowClient.builder().apiKey("  ").environment(name -> null).build());
    }

    @Test
    void theKeyFallsBackToTheEnvironment() {
        api.ok("{\"status\":\"ok\"}");
        EnrichedNowClient client = EnrichedNowClient.builder().baseUrl(api.url())
                .environment(name -> name.equals("ENRICHEDNOW_API_KEY") ? "en_live_from_env" : null).build();
        assertTrue(client.health().isOk());
        assertEquals("Bearer en_live_from_env", api.only().header("Authorization"));
    }

    @Test
    void anExplicitKeyWinsOverTheEnvironment() {
        api.ok("{\"status\":\"ok\"}");
        EnrichedNowClient.builder().baseUrl(api.url()).apiKey("en_live_explicit").environment(name -> "en_live_from_env").build().health();
        assertEquals("Bearer en_live_explicit", api.only().header("Authorization"));
    }

    @Test
    void everyRequestCarriesTheKeyAndSaysWhoIsCalling() {
        api.ok("{\"status\":\"ok\"}");
        assertEquals("ok", api.client().health().status());
        Received request = api.only();
        assertEquals("GET", request.method);
        assertEquals("/v1/health", request.path);
        assertNull(request.query);
        assertEquals("Bearer " + FakeApi.KEY, request.header("Authorization"));
        assertEquals("enrichednow-java/0.3.0", request.header("User-Agent"));
        assertEquals("application/json", request.header("Accept"));
    }

    @Test
    void aBaseUrlWithATrailingSlashAndACustomHttpClientWork() {
        api.ok("{\"status\":\"ok\"}");
        HttpClient http = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        EnrichedNowClient client = EnrichedNowClient.builder().apiKey(FakeApi.KEY).baseUrl(api.url() + "/").httpClient(http)
                .timeout(Duration.ofSeconds(5)).maxRetries(0).build();
        assertTrue(client.health().isOk());
        assertEquals("/v1/health", api.only().path);
    }

    @Test
    void badBuilderValuesAreRefused() {
        assertThrows(IllegalArgumentException.class, () -> EnrichedNowClient.builder().baseUrl("api.enrichednow.com"));
        assertThrows(IllegalArgumentException.class, () -> EnrichedNowClient.builder().maxRetries(-1));
        assertThrows(IllegalArgumentException.class, () -> EnrichedNowClient.builder().timeout(Duration.ZERO));
    }

    @Test
    void theCatalogueIsListed() {
        api.ok("{\"scrapers\":[{\"id\":\"ats.board\",\"version\":3,\"title\":\"ATS board\",\"summary\":\"Reads a board.\",\"mode\":\"job\","
                + "\"billing_unit\":\"request\",\"data_class\":\"jobs\",\"availability\":\"public\",\"later\":true},"
                + "{\"id\":\"linkedin.jobs_search\",\"version\":1,\"title\":\"LinkedIn\",\"summary\":\"s\",\"mode\":\"job\","
                + "\"billing_unit\":\"record\",\"units_per_request\":null,\"units_per_record\":4,\"data_class\":\"jobs\",\"availability\":\"public\"}]}");
        List<Scraper> scrapers = api.client().scrapers().list();
        assertEquals("GET /v1/scrapers", api.only().toString());
        assertEquals(2, scrapers.size());
        assertEquals("ats.board", scrapers.get(0).id());
        assertEquals(3, scrapers.get(0).version());
        assertEquals("request", scrapers.get(0).billingUnit());
        assertEquals("record", scrapers.get(1).billingUnit());
        assertEquals("public", scrapers.get(1).availability());
        assertEquals(4, scrapers.get(1).unitsPerRecord());
        assertEquals(4, scrapers.get(1).unitCost());
        assertEquals(1, scrapers.get(0).unitCost());              // an API from before costs differed: 1
    }

    @Test
    void oneScraperComesWithItsSchemas() {
        api.ok("{\"id\":\"ats.board\",\"version\":3,\"title\":\"ATS board\",\"summary\":\"Reads a board.\",\"mode\":\"job\","
                + "\"billing_unit\":\"request\",\"data_class\":\"jobs\",\"availability\":\"public\","
                + "\"input_schema\":{\"type\":\"object\",\"required\":[\"board_url\"]},\"record_schema\":{\"type\":\"object\"}}");
        ScraperDetail scraper = api.client().scrapers().get("ats.board");
        assertEquals("GET /v1/scrapers/ats.board", api.only().toString());
        assertEquals("ATS board", scraper.title());
        assertEquals(List.of("board_url"), scraper.inputSchema().get("required"));
        assertEquals(Map.of("type", "object"), scraper.recordSchema());
    }

    @Test
    void anIdIsEncodedIntoThePathAndAnEmptyOneIsRefused() {
        api.ok(FakeApi.run("a/b c", "ok", null));
        api.client().runs().get("a/b c");
        assertEquals("/v1/runs/a%2Fb%20c", api.only().path);
        assertThrows(IllegalArgumentException.class, () -> api.client().runs().get(""));
        assertThrows(IllegalArgumentException.class, () -> api.client().scrapers().get(null));
    }

    @Test
    void usageWithItsDefaults() {
        api.ok("{\"period\":{\"from\":\"2026-11-01\",\"to\":\"2026-11-02\"},\"total_units\":7,\"allowance_units\":1000,"
                + "\"rows\":[{\"key\":\"2026-11-01\",\"units\":7,\"runs\":5},{\"key\":\"2026-11-02\",\"units\":0,\"runs\":0}]}");
        Usage usage = api.client().usage().get();
        assertEquals("GET /v1/usage", api.only().toString());
        assertEquals(LocalDate.of(2026, 11, 1), usage.period().from());
        assertEquals(LocalDate.of(2026, 11, 2), usage.period().to());
        assertEquals(7, usage.totalUnits());
        assertEquals(1000, usage.allowanceUnits());
        assertEquals(2, usage.rows().size());
        assertEquals("2026-11-01", usage.rows().get(0).key());
        assertEquals(7, usage.rows().get(0).units());
        assertEquals(5, usage.rows().get(0).runs());
    }

    @Test
    void usageWithEveryParameter() {
        api.ok("{\"period\":{\"from\":\"2026-10-01\",\"to\":\"2026-10-31\"},\"total_units\":40,\"allowance_units\":500,"
                + "\"rows\":[{\"key\":\"data.postings.list\",\"units\":40,\"runs\":2}]}");
        Usage usage = api.client().usage().get(UsageParams.builder().from(LocalDate.of(2026, 10, 1)).to(LocalDate.of(2026, 10, 31))
                .groupBy(UsageParams.GroupBy.SCRAPER).product(UsageParams.Product.DATA).build());
        assertEquals("from=2026-10-01&to=2026-10-31&group_by=scraper&product=data", api.only().query);
        assertEquals("data.postings.list", usage.rows().get(0).key());
        assertFalse(usage.rows().isEmpty());
    }
}
