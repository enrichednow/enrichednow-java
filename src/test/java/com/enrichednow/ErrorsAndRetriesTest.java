package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.FakeApi.Reply;
import com.enrichednow.errors.ApiException;
import com.enrichednow.errors.ApiTimeoutException;
import com.enrichednow.errors.AuthenticationException;
import com.enrichednow.errors.ConnectionException;
import com.enrichednow.errors.EnrichedNowException;
import com.enrichednow.errors.NotFoundException;
import com.enrichednow.errors.PermissionDeniedException;
import com.enrichednow.errors.QuotaExceededException;
import com.enrichednow.errors.RateLimitException;
import com.enrichednow.errors.ServerException;
import com.enrichednow.errors.ValidationException;
import com.enrichednow.models.Run;
import java.net.ServerSocket;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ErrorsAndRetriesTest {
    private static final Map<String, Object> INPUT = Map.of("domain", "acme.com");

    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    // --- every error answer becomes its own exception ------------------------------------------------------------------

    @Test
    void unauthorizedIsAnAuthenticationException() {
        api.error(401, "unauthorized", "This API key is not valid.");
        AuthenticationException e = assertThrows(AuthenticationException.class, () -> api.client().runs().get("run_1"));
        assertEquals(401, e.statusCode());
        assertEquals("unauthorized", e.code());
        assertEquals("This API key is not valid.", e.apiMessage());
        assertTrue(e.details().isEmpty());
        assertNull(e.requestId());
        assertEquals("unauthorized (HTTP 401): This API key is not valid.", e.getMessage());
        assertInstanceOf(EnrichedNowException.class, e);
        assertEquals(1, api.count());
    }

    @Test
    void quotaExceededSaysWhereTheAccountStands() {
        api.reply(402, FakeApi.errorBody("quota_exceeded", "This month's units are used up.", "{\"used_units\":1000,\"allowance_units\":1000}"));
        QuotaExceededException e = assertThrows(QuotaExceededException.class, () -> api.client().scrapers().run("careers.company", INPUT));
        assertEquals("quota_exceeded", e.code());
        assertEquals(1000L, e.usedUnits());
        assertEquals(1000L, e.allowanceUnits());
        assertEquals(1, api.count());
    }

    @Test
    void forbiddenIsAPermissionDeniedExceptionWithItsReason() {
        api.reply(403, FakeApi.errorBody("forbidden", "Confirm your email address first.", "{\"reason\":\"email_not_verified\"}"));
        PermissionDeniedException e = assertThrows(PermissionDeniedException.class, () -> api.client().data().postings().list().toList());
        assertEquals("forbidden", e.code());
        assertEquals("email_not_verified", e.reason());
    }

    @Test
    void notFoundIsANotFoundException() {
        api.error(404, "not_found", "There is no run 'run_x'.");
        NotFoundException e = assertThrows(NotFoundException.class, () -> api.client().runs().get("run_x"));
        assertEquals(404, e.statusCode());
        assertNull(e.reason());
    }

    @Test
    void invalidInputListsEveryFieldThatIsWrong() {
        api.reply(422, FakeApi.errorBody("invalid_input", "The input is not valid.",
                "{\"fields\":[{\"path\":\"input.board_url\",\"message\":\"Field required\"},{\"path\":\"input.board\",\"message\":\"Extra inputs are not permitted\"}]}"));
        ValidationException e = assertThrows(ValidationException.class, () -> api.client().scrapers().run("ats.board", Map.of("board", "x")));
        assertEquals(422, e.statusCode());
        assertEquals("invalid_input", e.code());
        assertEquals(2, e.fieldErrors().size());
        assertEquals("input.board_url", e.fieldErrors().get(0).path());
        assertEquals("Field required", e.fieldErrors().get(0).message());
        assertEquals("input.board: Extra inputs are not permitted", e.fieldErrors().get(1).toString());
        assertEquals(1, api.count());
    }

    @Test
    void tooManyRunsIsARateLimitExceptionThatSaysSo() {
        api.reply(429, FakeApi.errorBody("too_many_runs", "Too many runs.", "{\"active_runs\":5,\"max_concurrent_runs\":5}"));
        RateLimitException e = assertThrows(RateLimitException.class,
                () -> api.builder().maxRetries(0).build().scrapers().run("careers.company", INPUT));
        assertTrue(e.isTooManyRuns());
        assertEquals(5L, e.activeRuns());
        assertEquals(5L, e.maxConcurrentRuns());
        assertNull(e.retryAfter());
    }

    @Test
    void rateLimitedCarriesRetryAfterAndARequestIdWhenThereIsOne() {
        api.reply(new Reply(429, FakeApi.errorBody("rate_limited", "Too many attempts.", "{}")).header("Retry-After", "37")
                .header("X-Request-Id", "req_123"));
        RateLimitException e = assertThrows(RateLimitException.class, () -> api.builder().maxRetries(0).build().runs().get("run_1"));
        assertFalse(e.isTooManyRuns());
        assertEquals(Duration.ofSeconds(37), e.retryAfter());
        assertEquals("req_123", e.requestId());
        assertTrue(e.getMessage().endsWith("[request req_123]"), e.getMessage());
    }

    @Test
    void serverErrorsAreServerExceptions() {
        api.error(500, "internal", "Something went wrong on our side.");
        ServerException internal = assertThrows(ServerException.class, () -> api.builder().maxRetries(0).build().usage().get());
        assertEquals("internal", internal.code());

        api.error(503, "data_unavailable", "The data API is not available at the moment.");
        ServerException data = assertThrows(ServerException.class,
                () -> api.builder().maxRetries(0).build().data().companies().get("acme.com"));
        assertEquals(503, data.statusCode());
        assertEquals("data_unavailable", data.code());
    }

    @Test
    void anAnswerThatIsNotTheApisOwnStillBecomesAnException() {
        api.reply(502, "<html>Bad Gateway</html>");
        ServerException e = assertThrows(ServerException.class, () -> api.builder().maxRetries(0).build().runs().get("run_1"));
        assertEquals(502, e.statusCode());
        assertNull(e.code());
        assertNull(e.apiMessage());
        assertEquals("http_502 (HTTP 502)", e.getMessage());

        api.reply(418, "");
        ApiException teapot = assertThrows(ApiException.class, () -> api.client().runs().get("run_1"));
        assertSame(ApiException.class, teapot.getClass());
    }

    @Test
    void aSuccessfulAnswerThatCannotBeReadIsAnSdkException() {
        api.ok("not json");
        EnrichedNowException e = assertThrows(EnrichedNowException.class, () -> api.client().runs().get("run_1"));
        assertFalse(e instanceof ApiException);
        assertEquals(1, api.count());
    }

    // --- retries ------------------------------------------------------------------------------------------------------

    @Test
    void aReadIsTriedAgainAfterAServerError() {
        api.error(500, "internal", "x").error(503, "data_unavailable", "x").ok(FakeApi.run("run_1", "ok", null));
        Run run = api.client().runs().get("run_1");
        assertEquals("run_1", run.id());
        assertEquals(3, api.count());
    }

    @Test
    void retriesStopAfterMaxRetries() {
        api.error(500, "internal", "1").error(500, "internal", "2").error(500, "internal", "3").ok(FakeApi.run("run_1", "ok", null));
        ServerException e = assertThrows(ServerException.class, () -> api.client().runs().get("run_1"));
        assertEquals("3", e.apiMessage());
        assertEquals(3, api.count());                       // the first try and the default 2 retries
    }

    @Test
    void maxRetriesIsConfigurableAndZeroTurnsRetriesOff() {
        api.error(500, "internal", "x");
        assertThrows(ServerException.class, () -> api.builder().maxRetries(0).build().runs().get("run_1"));
        assertEquals(1, api.count());

        for (int i = 0; i < 4; i++) {
            api.error(500, "internal", "x");
        }
        api.ok(FakeApi.run("run_1", "ok", null));
        assertEquals("run_1", api.builder().maxRetries(4).build().runs().get("run_1").id());
        assertEquals(6, api.count());
    }

    @Test
    void errorsThatWillNotChangeAreNotRetried() {
        int[] statuses = {401, 402, 403, 404, 422};
        for (int status : statuses) {
            api.error(status, "x", "x");
            assertThrows(ApiException.class, () -> api.client().runs().get("run_1"));
        }
        assertEquals(statuses.length, api.count());
    }

    @Test
    void startingARunIsNotRepeatedAfterAServerError() {
        api.error(500, "internal", "x").reply(202, FakeApi.run("run_1", "queued", null));
        assertThrows(ServerException.class, () -> api.client().scrapers().run("careers.company", INPUT));
        assertEquals(1, api.count());
    }

    @Test
    void startingARunIsNotRepeatedAfterATimeout() {
        api.reply(new Reply(202, FakeApi.run("run_1", "queued", null)).delay(1500)).reply(202, FakeApi.run("run_2", "queued", null));
        EnrichedNowClient client = api.builder().timeout(Duration.ofMillis(200)).build();
        ApiTimeoutException e = assertThrows(ApiTimeoutException.class, () -> client.scrapers().run("careers.company", INPUT));
        assertTrue(e.getMessage().contains("check before sending it again"), e.getMessage());
        assertEquals(1, api.count());
    }

    @Test
    void startingARunIsTriedAgainAfterA429BecauseNoRunWasCreated() {
        api.reply(429, FakeApi.errorBody("too_many_runs", "Too many runs.", "{\"active_runs\":5,\"max_concurrent_runs\":5}"))
                .reply(202, FakeApi.run("run_1", "queued", null));
        assertEquals("run_1", api.client().scrapers().run("careers.company", INPUT).id());
        assertEquals(2, api.count());
    }

    @Test
    void cancellingIsSafeToRepeatAndIsRetried() {
        api.error(500, "internal", "x").ok(FakeApi.run("run_1", "cancelled", "cancelled"));
        assertEquals("cancelled", api.client().runs().cancel("run_1").reason());
        assertEquals(2, api.count());
    }

    @Test
    void retryAfterIsHonoured() {
        api.reply(new Reply(429, FakeApi.errorBody("rate_limited", "Too many attempts.", "{}")).header("Retry-After", "1"))
                .ok(FakeApi.run("run_1", "ok", null));
        long started = System.nanoTime();
        api.client().runs().get("run_1");                    // the client's own backoff here is 1 to 5 ms
        long waited = Duration.ofNanos(System.nanoTime() - started).toMillis();
        assertTrue(waited >= 950, "waited " + waited + " ms");
        assertEquals(2, api.count());
    }

    @Test
    void retryAfterIsReadAsSecondsOrAsADate() {
        assertEquals(Duration.ofSeconds(20), Transport.parseRetryAfter("20"));
        assertEquals(Duration.ofMillis(1500), Transport.parseRetryAfter(" 1.5 "));
        assertEquals(Duration.ZERO, Transport.parseRetryAfter("Wed, 21 Oct 2015 07:28:00 GMT"));
        assertNull(Transport.parseRetryAfter("soon"));
        assertNull(Transport.parseRetryAfter("-3"));
    }

    @Test
    void backoffGrowsExponentiallyWithJitterAndIsCapped() {
        Transport transport = new Transport(HttpClient.newHttpClient(), api.url(), FakeApi.KEY, Duration.ofSeconds(5), 5,
                Duration.ofMillis(100), Duration.ofMillis(250));
        ApiRequest<Run> read = new Requests(transport).run("run_1");
        ServerException failure = new ServerException(500, "internal", "x", null, null);
        long[][] bounds = {{50, 100}, {100, 200}, {125, 250}, {125, 250}};
        for (int attempt = 0; attempt < bounds.length; attempt++) {
            for (int i = 0; i < 200; i++) {
                long delay = transport.retryDelay(read, attempt, failure).toMillis();
                assertTrue(delay >= bounds[attempt][0] && delay <= bounds[attempt][1], "attempt " + attempt + ": " + delay);
            }
        }
        assertNull(transport.retryDelay(read, 5, failure));

        RateLimitException wait = new RateLimitException(429, "rate_limited", "x", null, null, Duration.ofHours(1));
        assertEquals(Transport.MAX_RETRY_AFTER, transport.retryDelay(read, 0, wait));
    }

    @Test
    void aReadIsTriedAgainAfterATimeout() {
        api.reply(new Reply(200, FakeApi.run("run_1", "ok", null)).delay(1500)).ok(FakeApi.run("run_1", "ok", null));
        EnrichedNowClient client = api.builder().timeout(Duration.ofMillis(200)).build();
        assertEquals("run_1", client.runs().get("run_1").id());
        assertEquals(2, api.count());
    }

    @Test
    void aTimeoutThatKeepsHappeningIsAnApiTimeoutException() {
        api.reply(new Reply(200, FakeApi.run("run_1", "ok", null)).delay(1500));
        EnrichedNowClient client = api.builder().timeout(Duration.ofMillis(150)).maxRetries(0).build();
        ApiTimeoutException e = assertThrows(ApiTimeoutException.class, () -> client.runs().get("run_1"));
        assertInstanceOf(ConnectionException.class, e);
        assertTrue(e.getMessage().contains("GET /v1/runs/run_1"), e.getMessage());
    }

    @Test
    void aRequestThatWaitsForARunIsAllowedTheWaitOnTopOfTheTimeout() {
        api.reply(new Reply(200, FakeApi.run("run_1", "ok", null)).delay(600));
        EnrichedNowClient client = api.builder().timeout(Duration.ofMillis(200)).build();
        Run run = client.scrapers().run("careers.company", INPUT, RunOptions.waitUpTo(Duration.ofSeconds(1)));
        assertTrue(run.isFinished());
    }

    @Test
    void aConnectionThatCannotBeMadeIsAConnectionExceptionAfterTheRetries() throws Exception {
        int closedPort;
        try (ServerSocket socket = new ServerSocket(0)) {
            closedPort = socket.getLocalPort();
        }
        EnrichedNowClient client = EnrichedNowClient.builder().apiKey(FakeApi.KEY).baseUrl("http://127.0.0.1:" + closedPort)
                .initialRetryDelay(Duration.ofMillis(1)).build();
        ConnectionException get = assertThrows(ConnectionException.class, () -> client.runs().get("run_1"));
        assertFalse(get instanceof ApiTimeoutException);
        assertThrows(ConnectionException.class, () -> client.scrapers().run("careers.company", INPUT));
    }
}
