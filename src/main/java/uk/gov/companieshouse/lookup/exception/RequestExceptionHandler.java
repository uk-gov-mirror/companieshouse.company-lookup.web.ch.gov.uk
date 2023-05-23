package uk.gov.companieshouse.lookup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.lookup.Application;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RequestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(Application.APPLICATION_NAME_SPACE);

    @ExceptionHandler({InvalidRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(HttpServletRequest request, Exception ex) {
        LOG.errorRequest(request, ex.getMessage(), ex);
        return "error";
    }
}
