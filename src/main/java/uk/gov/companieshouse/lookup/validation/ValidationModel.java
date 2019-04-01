package uk.gov.companieshouse.lookup.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use the {@code ValidationModel} annotation to indicate a model class whose fields should be
 * scanned for field-level validation mappings annotated with {@code ValidationMapping}.
 *
 * @see ValidationMapping
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationModel {

}
