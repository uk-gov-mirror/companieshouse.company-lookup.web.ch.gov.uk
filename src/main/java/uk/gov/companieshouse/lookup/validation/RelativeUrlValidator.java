package uk.gov.companieshouse.lookup.validation;

import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.lookup.Application;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URI;
import java.net.URISyntaxException;

public class RelativeUrlValidator implements ConstraintValidator<RelativeUrl, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.APPLICATION_NAME_SPACE);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if(value == null || value.isEmpty() || new URI(value).isAbsolute()) {
                return false;
            }else{
                return true;
            }
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
