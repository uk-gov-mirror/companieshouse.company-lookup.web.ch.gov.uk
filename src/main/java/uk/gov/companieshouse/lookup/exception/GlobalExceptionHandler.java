package uk.gov.companieshouse.lookup.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    //private static final Logger LOG = LoggerFactory.getLogger(Application.APPLICATION_NAME_SPACE);
    private final static Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(value = { RuntimeException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(HttpServletRequest request, Exception ex) {

        LOG.severe(ex.getMessage());
//
        return "error";
    }

    @ExceptionHandler(value = { ServiceException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServiceException(HttpServletRequest request, Exception ex) {

        LOG.severe(ex.getMessage());
        //LOG.errorRequest(request, ex.getMessage(), ex);
        return "error";
    }
}