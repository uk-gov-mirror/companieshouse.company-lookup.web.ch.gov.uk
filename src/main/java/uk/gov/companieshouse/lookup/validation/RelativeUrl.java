package uk.gov.companieshouse.lookup.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RelativeUrlValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RelativeUrl {
    public abstract String message() default "";

    public abstract Class<?>[] groups() default {};

    public abstract Class<?>[] payload() default {};
}
