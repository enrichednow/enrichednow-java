package com.enrichednow.errors;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The API answered with an error: an HTTP status of 4xx or 5xx and a body of
 * {@code {"error": {"code", "message", "details"}}}.
 *
 * <p>Match on {@link #code()}, a stable {@code snake_case} code ({@code unauthorized}, {@code forbidden},
 * {@code not_found}, {@code invalid_input}, {@code quota_exceeded}, {@code too_many_runs}, {@code rate_limited},
 * {@code internal}, {@code data_unavailable}). The message is a sentence for a person and its wording can change.
 *
 * <p>Each HTTP status has a subclass: {@link AuthenticationException} (401), {@link QuotaExceededException} (402; its subclass
 * {@link LimitReachedException} for {@code limit_reached}),
 * {@link PermissionDeniedException} (403), {@link NotFoundException} (404), {@link ValidationException} (422),
 * {@link RateLimitException} (429) and {@link ServerException} (5xx).
 */
public class ApiException extends EnrichedNowException {
    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String code;
    private final String apiMessage;
    private final transient Map<String, Object> details;
    private final String requestId;

    /**
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null} when the body was not an API error
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     */
    public ApiException(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId) {
        super(describe(statusCode, code, apiMessage, requestId));
        this.statusCode = statusCode;
        this.code = code;
        this.apiMessage = apiMessage;
        this.details = details == null ? Collections.emptyMap() : Collections.unmodifiableMap(new LinkedHashMap<>(details));
        this.requestId = requestId;
    }

    /**
     * The exception for an error answer, of the subclass that matches its HTTP status.
     *
     * @param statusCode the HTTP status
     * @param code       the API's error code, or {@code null}
     * @param apiMessage the API's message, or {@code null}
     * @param details    the API's {@code details} object, or {@code null}
     * @param requestId  the request id the answer carried, or {@code null}
     * @param retryAfter the wait the {@code Retry-After} header named, or {@code null}
     * @return the exception to throw
     */
    public static ApiException of(int statusCode, String code, String apiMessage, Map<String, Object> details, String requestId,
                                  Duration retryAfter) {
        switch (statusCode) {
            case 401:
                return new AuthenticationException(statusCode, code, apiMessage, details, requestId);
            case 402:
                return "limit_reached".equals(code)
                        ? new LimitReachedException(statusCode, code, apiMessage, details, requestId)
                        : new QuotaExceededException(statusCode, code, apiMessage, details, requestId);
            case 403:
                return new PermissionDeniedException(statusCode, code, apiMessage, details, requestId);
            case 404:
                return new NotFoundException(statusCode, code, apiMessage, details, requestId);
            case 400:
            case 422:
                return new ValidationException(statusCode, code, apiMessage, details, requestId);
            case 429:
                return new RateLimitException(statusCode, code, apiMessage, details, requestId, retryAfter);
            default:
                if (statusCode >= 500) {
                    return new ServerException(statusCode, code, apiMessage, details, requestId);
                }
                return new ApiException(statusCode, code, apiMessage, details, requestId);
        }
    }

    private static String describe(int statusCode, String code, String apiMessage, String requestId) {
        StringBuilder text = new StringBuilder();
        text.append(code != null ? code : "http_" + statusCode).append(" (HTTP ").append(statusCode).append(')');
        if (apiMessage != null && !apiMessage.isEmpty()) {
            text.append(": ").append(apiMessage);
        }
        if (requestId != null) {
            text.append(" [request ").append(requestId).append(']');
        }
        return text.toString();
    }

    /** @return the HTTP status of the answer */
    public int statusCode() {
        return statusCode;
    }

    /** @return the API's stable error code, or {@code null} when the answer was not in the API's error format */
    public String code() {
        return code;
    }

    /** @return the API's own sentence about the error, or {@code null}; for a person, not for parsing */
    public String apiMessage() {
        return apiMessage;
    }

    /**
     * What the API added to the error. Empty for most codes; {@code fields} for {@code invalid_input},
     * {@code used_units} and {@code allowance_units} for {@code quota_exceeded} (with {@code topup_units} and
     * {@code units_needed} on starting a run), {@code active_runs} and
     * {@code max_concurrent_runs} for {@code too_many_runs}, and {@code reason} for some {@code forbidden} answers.
     *
     * @return the details, never {@code null}
     */
    public Map<String, Object> details() {
        return details == null ? Collections.emptyMap() : details;
    }

    /**
     * For a {@code forbidden} answer that says why, the reason code, e.g. {@code email_not_verified}.
     *
     * @return {@code details.reason}, or {@code null}
     */
    public String reason() {
        Object reason = details().get("reason");
        return reason instanceof String ? (String) reason : null;
    }

    /** @return the request id of the answer when the API sent one, otherwise {@code null} */
    public String requestId() {
        return requestId;
    }

    /**
     * The problems an {@code invalid_input} answer lists, one per field.
     *
     * @return the field errors; empty when the error names none
     */
    public List<FieldError> fieldErrors() {
        Object fields = details().get("fields");
        if (!(fields instanceof List)) {
            return Collections.emptyList();
        }
        List<FieldError> out = new ArrayList<>();
        for (Object item : (List<?>) fields) {
            if (item instanceof Map) {
                Map<?, ?> field = (Map<?, ?>) item;
                Object path = field.get("path");
                Object message = field.get("message");
                out.add(new FieldError(path == null ? "" : String.valueOf(path), message == null ? "" : String.valueOf(message)));
            }
        }
        return Collections.unmodifiableList(out);
    }

    Long detailNumber(String key) {
        Object value = details().get(key);
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    /** One problem of an {@code invalid_input} error. */
    public static final class FieldError {
        private final String path;
        private final String message;

        /**
         * @param path    where the problem is
         * @param message what is wrong there
         */
        public FieldError(String path, String message) {
            this.path = path;
            this.message = message;
        }

        /** @return where the problem is, as it was sent: {@code input.board_url}, {@code wait}, {@code limit} */
        public String path() {
            return path;
        }

        /** @return what is wrong there */
        public String message() {
            return message;
        }

        @Override
        public String toString() {
            return path + ": " + message;
        }
    }
}
