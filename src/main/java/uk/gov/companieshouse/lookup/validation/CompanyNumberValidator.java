package uk.gov.companieshouse.lookup.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompanyNumberValidator implements ConstraintValidator<CompanyNumberValidation, String> {


    public boolean isValid(String companyNumber, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (companyNumber == null || companyNumber.equals("")) {
            context.buildConstraintViolationWithTemplate(
                "{company.number.NotEmpty.message}")
                .addConstraintViolation();
            return false;
        }

        if (companyNumber.length() < 8) {
            context.buildConstraintViolationWithTemplate("{company.number.Size.message}")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}