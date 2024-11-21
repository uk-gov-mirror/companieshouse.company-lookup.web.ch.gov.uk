package uk.gov.companieshouse.lookup.validation;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Company number validation.
 */
@Constraint(validatedBy = CompanyNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyNumberValidation {

    /**
     * Message string.
     *
     * @return the string
     */
    public abstract String message() default "";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    public abstract Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    public abstract Class<?>[] payload() default {};
}
