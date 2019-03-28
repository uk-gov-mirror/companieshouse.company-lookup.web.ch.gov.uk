package uk.gov.companieshouse.lookup.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use the {@code ValidationMapping} annotation to associate an API error
 * JSON path with a model object field for the purposes of validation.
 *
 * @see ValidationModel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationMapping {

    String value();
}
