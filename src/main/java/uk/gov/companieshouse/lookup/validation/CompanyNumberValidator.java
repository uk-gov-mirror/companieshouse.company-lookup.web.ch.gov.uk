package uk.gov.companieshouse.lookup.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompanyNumberValidator implements ConstraintValidator<CompanyNumberValidation, String> {

    private static final Pattern COMPANY_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9]+$");

    public boolean isValid(String companyNumber, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (companyNumber == null || companyNumber.equals("")) {
            context.buildConstraintViolationWithTemplate(
                "{company.error.number.empty}")
                .addConstraintViolation();
            return false;
        }

        if (companyNumber.length() != 8) {
            context.buildConstraintViolationWithTemplate("{company.error.number.length}")
                .addConstraintViolation();
            return false;
        }

        if(COMPANY_NUMBER_PATTERN.matcher(companyNumber).matches()){
            return true;
        }
        else{
            context.buildConstraintViolationWithTemplate("{company.error.number.invalid}")
                    .addConstraintViolation();
            return false;
        }
    }
}