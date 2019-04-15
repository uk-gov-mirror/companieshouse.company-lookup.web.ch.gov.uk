package uk.gov.companieshouse.lookup.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

@Constraint(validatedBy = CompanyNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyNumberValidation {

    public abstract String message() default "";

    public abstract Class<?>[] groups() default {};

    public abstract Class<?>[] payload() default {};
}
