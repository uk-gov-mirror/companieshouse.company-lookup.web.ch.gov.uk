package uk.gov.companieshouse.lookup.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RelativeUrlValidatorTest {

    private RelativeUrlValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        this.validator = new RelativeUrlValidator();
    }

    @Test
    void testForwardUrlIsInvalidIfNull() {
        //when
        boolean actual = validator.isValid(null, context);

        //then
        assertFalse(actual);
    }

    @Test
    void testForwardUrlIsInvalidIfBlank() {
        //when
        boolean actual = validator.isValid("", context);

        //then
        assertFalse(actual);
    }

    @Test
    void testForwardUrlIsInvalidIfMalformed() {
        //when
        boolean actual = validator.isValid("^.^", context);

        //then
        assertFalse(actual);
    }

    @Test
    void testForwardUrlIsInvalidIfAbsoluteUrl() {
        //when
        boolean actual = validator.isValid("http://0.0.0.0", context);

        //then
        assertFalse(actual);
    }

    @Test
    void testForwardUrlIsValidIfRelativeUrl() {
        //when
        boolean actual = validator.isValid("/company/", context);

        //then
        assertTrue(actual);
    }
}
