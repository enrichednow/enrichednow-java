package com.enrichednow.errors;

/**
 * The request took longer than the client's timeout. A kind of {@link ConnectionException}.
 */
public class ApiTimeoutException extends ConnectionException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message what went wrong
     * @param cause   the underlying exception
     */
    public ApiTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
