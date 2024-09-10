package uk.gov.companieshouse.lookup.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyNumberValidatorTest {

    private CompanyNumberValidator validator;

    @Mock
    private MessageSource messageSource;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeEach
    void setUp() {
        this.validator = new CompanyNumberValidator(messageSource, request);
    }

    @Test
    void testCompanyNumberIsBlank() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(
                constraintViolationBuilder);
        // Arrange
        when(RequestContextUtils.getLocale(request)).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage("company.error.number.empty", null, Locale.ENGLISH))
                .thenReturn("Company number cannot be empty");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);

        // Act
        boolean actual = validator.isValid(null, context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // Assert
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("Company number cannot be empty"));
        assertFalse(actual);
    }

    @Test
    void testCompanyNumberSizeMoreThanEight() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(
                constraintViolationBuilder);
        // Arrange
        when(RequestContextUtils.getLocale(request)).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage("company.error.number.length", null, Locale.ENGLISH))
                .thenReturn("Company number must be 8 characters long");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);

        // Act
        boolean actual = validator.isValid("123456789", context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // Assert
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("Company number must be 8 characters long"));
        assertFalse(actual);
    }

    @Test
    void testCompanyNumberSizeLessThanEight() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(
                constraintViolationBuilder);
        // Arrange
        when(RequestContextUtils.getLocale(request)).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage("company.error.number.length", null, Locale.ENGLISH))
                .thenReturn("Company number must be 8 characters long");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);

        // Act
        boolean actual = validator.isValid("123456", context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // Assert
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("Company number must be 8 characters long"));
        assertFalse(actual);
    }

    @Test
    void testCompanyNumberPattern() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(
                constraintViolationBuilder);
        // Arrange
        when(RequestContextUtils.getLocale(request)).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage("company.error.number.invalid", null, Locale.ENGLISH))
                .thenReturn("Invalid company number format");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);

        // Act
        boolean actual = validator.isValid("ac123456", context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // Assert
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("Invalid company number format"));
        assertFalse(actual);
    }

    @Test
    void testWithValidCompanyNumber() {
        // Arrange
        when(RequestContextUtils.getLocale(request)).thenReturn(Locale.ENGLISH);

        // Act
        boolean actual = validator.isValid("01777777", context);

        // Assert
        assertTrue(actual);
    }
}
