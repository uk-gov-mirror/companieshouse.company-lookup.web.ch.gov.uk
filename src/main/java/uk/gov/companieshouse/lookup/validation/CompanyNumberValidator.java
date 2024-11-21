package uk.gov.companieshouse.lookup.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
public class CompanyNumberValidator implements ConstraintValidator<CompanyNumberValidation, String> {

    private static final Pattern COMPANY_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9]+$");

    private final MessageSource messageSource;

    private final HttpServletRequest request;

    @Autowired
    public CompanyNumberValidator(MessageSource messageSource, HttpServletRequest request) {
        this.messageSource = messageSource;
        this.request = request;
    }

    @Override
    public boolean isValid(String companyNumber, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        Locale locale = RequestContextUtils.getLocale(request);

        if (companyNumber == null || companyNumber.equals("")) {
            String errorMessage = messageSource.getMessage("company.error.number.empty", null, locale);
            context.buildConstraintViolationWithTemplate(
                errorMessage)
                .addConstraintViolation();
            return false;
        }

        if (companyNumber.length() != 8) {
            String errorMessage = messageSource.getMessage("company.error.number.length", null, locale);
            context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation();
            return false;
        }

        if(COMPANY_NUMBER_PATTERN.matcher(companyNumber).matches()){
            return true;
        }
        else{
            String errorMessage = messageSource.getMessage("company.error.number.invalid", null, locale);
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();
            return false;
        }
    }
}