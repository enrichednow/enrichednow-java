package com.enrichednow;

import com.enrichednow.errors.WebhookVerificationException;
import com.enrichednow.models.WebhookEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Verifying a webhook request that arrives. Needs no client and makes no request.
 *
 * <pre>{@code
 * byte[] body = request.getInputStream().readAllBytes();          // the raw bytes, before any JSON parsing
 * WebhookEvent event = Webhooks.parse(secret, request.getHeader(Webhooks.TIMESTAMP_HEADER), body,
 *         request.getHeader(Webhooks.SIGNATURE_HEADER));
 * }</pre>
 */
public final class Webhooks {
    /** The header that carries the signature. */
    public static final String SIGNATURE_HEADER = "X-Webhook-Signature";
    /** The header that carries the time the request was signed, in seconds since 1970. */
    public static final String TIMESTAMP_HEADER = "X-Webhook-Timestamp";
    /** How far from now a request's timestamp may be, either way. */
    public static final Duration DEFAULT_TOLERANCE = Duration.ofSeconds(300);

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    private Webhooks() {
    }

    /**
     * Whether a webhook request is one this API signed with {@code secret}, recently.
     *
     * <p>The signature is {@code v1=} and the HMAC-SHA256, in hex, of the timestamp, a dot, and the body exactly as it
     * arrived. False when it does not match (compared in constant time), when anything is missing or malformed, and
     * when the timestamp is more than 300 seconds from now, so a recorded request cannot be played back later.
     *
     * @param secret    the endpoint's signing secret ({@code whsec_…}); for a run's own {@code webhook_url}, the run secret
     * @param timestamp the {@code X-Webhook-Timestamp} header
     * @param body      the request body, as the bytes that arrived
     * @param signature the {@code X-Webhook-Signature} header
     * @return whether it verifies
     */
    public static boolean verify(String secret, String timestamp, byte[] body, String signature) {
        return verify(secret, timestamp, body, signature, DEFAULT_TOLERANCE);
    }

    /**
     * {@link #verify(String, String, byte[], String)}, with a tolerance of your choosing.
     *
     * @param secret    the signing secret
     * @param timestamp the {@code X-Webhook-Timestamp} header
     * @param body      the request body, as the bytes that arrived
     * @param signature the {@code X-Webhook-Signature} header
     * @param tolerance how far from now the timestamp may be
     * @return whether it verifies
     */
    public static boolean verify(String secret, String timestamp, byte[] body, String signature, Duration tolerance) {
        if (secret == null || secret.isEmpty() || signature == null || body == null || timestamp == null
                || !timestamp.matches("[0-9]{1,16}")) {
            return false;
        }
        long sent = Long.parseLong(timestamp);
        long allowed = (tolerance == null ? DEFAULT_TOLERANCE : tolerance).getSeconds();
        if (Math.abs(Instant.now().getEpochSecond() - sent) > allowed) {
            return false;
        }
        byte[] digest;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((timestamp + ".").getBytes(StandardCharsets.UTF_8));
            digest = mac.doFinal(body);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
        char[] hex = new char[digest.length * 2];
        for (int i = 0; i < digest.length; i++) {
            hex[i * 2] = HEX[(digest[i] >> 4) & 0xf];
            hex[i * 2 + 1] = HEX[digest[i] & 0xf];
        }
        byte[] expected = ("v1=" + new String(hex)).getBytes(StandardCharsets.US_ASCII);
        return MessageDigest.isEqual(expected, signature.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Verifies a webhook request as {@link #verify(String, String, byte[], String)} does, and returns the event it
     * carries.
     *
     * @param secret    the signing secret
     * @param timestamp the {@code X-Webhook-Timestamp} header
     * @param body      the request body, as the bytes that arrived
     * @param signature the {@code X-Webhook-Signature} header
     * @return the event
     * @throws WebhookVerificationException when the request does not verify, or what was signed is not an event:
     *                                      answer {@code 400} and do nothing with it
     */
    public static WebhookEvent parse(String secret, String timestamp, byte[] body, String signature) {
        if (!verify(secret, timestamp, body, signature)) {
            throw new WebhookVerificationException("The webhook request did not verify: the signature does not match this secret, "
                    + "timestamp and body, or the timestamp is too far from now.");
        }
        try {
            WebhookEvent event = MAPPER.readValue(body, WebhookEvent.class);
            if (event == null || event.type() == null) {
                throw new WebhookVerificationException("The webhook request verified, but its body is not an event.");
            }
            return event;
        } catch (IOException e) {
            throw new WebhookVerificationException("The webhook request verified, but its body is not an event.", e);
        }
    }
}
