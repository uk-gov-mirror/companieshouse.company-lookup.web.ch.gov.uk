package uk.gov.companieshouse.lookup.exception;

/**
 * Instances of this class should be thrown to halt request processing immediately if the request is invalid.
 */
public class InvalidRequestException extends Exception {
    public InvalidRequestException(String message) {
        super(message);
    }
}
