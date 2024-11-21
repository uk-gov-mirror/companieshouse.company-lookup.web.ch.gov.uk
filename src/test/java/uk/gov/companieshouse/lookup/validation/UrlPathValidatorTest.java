package uk.gov.companieshouse.lookup.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlPathValidatorTest {

    private UrlPathValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        this.validator = new UrlPathValidator();
    }

    @Test
    void testForwardUrlIsInvalidIfNull() {
        //when
        boolean actual = validator.isValid(null, context);

        //then
        assertFalse(actual);
    }

    @Test
    void testForwardUrlIsInvalidIfMissingProtocol() {
        //when
        boolean actual = validator.isValid("example.com:8080", context);

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
    void testForwardUrlIsInvalidIfProtocolRelativeUrl() {
        //when
        boolean actual = validator.isValid("//example.com", context);

        //then
        assertFalse(actual);
    }

    @Test
    void testForwardUrlIsValidIfRelativeUrlWithLeadingSlash() {
        //when
        boolean actual = validator.isValid("/company/", context);

        //then
        assertTrue(actual);
    }

    @Test
    void testForwardUrlIsValidIfRelativeUrlWithoutLeadingSlash() {
        //when
        boolean actual = validator.isValid("company", context);

        //then
        assertTrue(actual);
    }

    @Test
    void testForwardUrlIsValidIfRelativeUrlWithPathParams() {
        //when
        boolean actual = validator.isValid("/company/{company}/details", context);

        //then
        assertTrue(actual);
    }

    @Test
    void testForwardUrlIsValidIfSlash() {
        //when
        boolean actual = validator.isValid("/", context);

        //then
        assertTrue(actual);
    }

    @Test
    void testForwardUrlIsValidIfEmpty() {
        //when
        boolean actual = validator.isValid("", context);

        //then
        assertTrue(actual);
    }
}
