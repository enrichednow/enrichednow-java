package com.enrichednow.errors;

/**
 * The base of every exception this SDK throws. Unchecked.
 *
 * <p>Catch this to handle anything that can go wrong with a call; catch a subclass to handle one kind:
 * {@link ApiException} for an error answer from the API, {@link ConnectionException} when no answer arrived,
 * {@link RunFailedException} when a run ended without a result.
 */
public class EnrichedNowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message what went wrong
     */
    public EnrichedNowException(String message) {
        super(message);
    }

    /**
     * @param message what went wrong
     * @param cause   the underlying exception
     */
    public EnrichedNowException(String message, Throwable cause) {
        super(message, cause);
    }
}
