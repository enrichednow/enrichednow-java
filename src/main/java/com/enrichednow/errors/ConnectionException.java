package com.enrichednow.errors;

/**
 * No answer arrived: the connection could not be made, broke, or the answer could not be read. Safe requests are
 * retried automatically before this is thrown.
 *
 * <p>When this is thrown from starting a run, the run may or may not exist: look at {@code client.runs().list()}
 * before starting it again.
 */
public class ConnectionException extends EnrichedNowException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message what went wrong
     * @param cause   the underlying exception
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
