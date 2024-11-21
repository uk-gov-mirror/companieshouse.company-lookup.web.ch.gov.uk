package uk.gov.companieshouse.lookup.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidationErrorTest {

    ValidationError validator = new ValidationError();

    @BeforeEach
    void setUp() {
        this.validator = new ValidationError();
    }

    @Test
    void testGetMessageArguments() {
        Map<String, String> expected = new HashMap<>();
        expected.put("Key 1", "Value 1");
        expected.put("Key 2", "Value 2");

        validator.setMessageArguments(expected);

        Map<String, String> result = validator.getMessageArguments();

        assertEquals(expected, result);
    }

    @Test
    void testGetFieldPath() {
        String expected = "field path";
        validator.setFieldPath(expected);
        String result = validator.getFieldPath();
        assertEquals(expected, result);
    }

    @Test
    void testGetMessageKey() {
        String expected = "message";
        validator.setMessageKey(expected);
        String result = validator.getMessageKey();
        assertEquals(expected, result);
    }
}
