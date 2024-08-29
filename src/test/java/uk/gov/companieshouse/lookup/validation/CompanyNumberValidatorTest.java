package uk.gov.companieshouse.lookup.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyNumberValidatorTest {

    private CompanyNumberValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeEach
    void setUp() {
        this.validator = new CompanyNumberValidator();
    }

    @Test
    void testCompanyNumberIsBlank() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean actual = validator.isValid(null, context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //then
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("company.number.NotEmpty.message"));
        assertFalse(actual);
    }

    @Test
    void testCompanyNumberSizeMoreThanEight() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean actual = validator.isValid("123456789", context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //then
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("company.number.Size.message"));
        assertFalse(actual);
    }

    @Test
    void testCompanyNumberSizeLessThanEight() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean actual = validator.isValid("123456", context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //then
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("company.number.Size.message"));
        assertFalse(actual);
    }

    @Test
    void testCompanyNumberPattern() {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean actual = validator.isValid("ac123456", context);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //then
        verify(context).buildConstraintViolationWithTemplate(stringArgumentCaptor.capture());
        assertTrue(stringArgumentCaptor.getValue().contains("company.number.pattern.message"));
        assertFalse(actual);
    }

    @Test
    void testWithValidCompanyNumber() {
        //when
        boolean actual = validator.isValid("01777777", context);

        //then
        assertTrue(actual);
    }
}