package uk.gov.companieshouse.lookup.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use the {@code ValidationParentMapping} annotation to associate an API error with a model object
 * field where the API error path relates to a parent of that field (e.g. current or previous
 * period).
 *
 * @see ValidationModel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationParentMapping {

    String value();
}
