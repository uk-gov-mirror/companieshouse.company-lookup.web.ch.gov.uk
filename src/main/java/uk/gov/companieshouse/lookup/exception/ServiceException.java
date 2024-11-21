package uk.gov.companieshouse.lookup.exception;

public class ServiceException extends Exception {

    /**
     * Constructs a new {@code ServiceException} with a custom message and the specified cause.
     *
     * @param message a custom message
     * @param cause the cause
     */
    public ServiceException(String message, Throwable cause) {
        super(cause);
    }
}
