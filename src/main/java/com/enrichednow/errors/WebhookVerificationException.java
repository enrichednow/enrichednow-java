package com.enrichednow.errors;

/**
 * A webhook request that is not one this API signed with the secret, recently, or whose body is not an event. Answer
 * it {@code 400} and do nothing with it.
 */
public class WebhookVerificationException extends EnrichedNowException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message what did not verify
     */
    public WebhookVerificationException(String message) {
        super(message);
    }

    /**
     * @param message what did not verify
     * @param cause   why
     */
    public WebhookVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
