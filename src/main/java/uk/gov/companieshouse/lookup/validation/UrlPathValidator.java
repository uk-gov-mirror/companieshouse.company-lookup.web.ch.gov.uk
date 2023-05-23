package uk.gov.companieshouse.lookup.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UrlPathValidator implements ConstraintValidator<RelativeUrl, String> {

    private static final Pattern URL_PATTERN = Pattern.compile("^(?![^:/?#]+:)(?!//[^/?#]*).*$"); //based on https://tools.ietf.org/html/rfc3986#page-51

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && URL_PATTERN.matcher(value).matches();
    }
}
