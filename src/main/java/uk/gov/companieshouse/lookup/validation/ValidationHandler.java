package uk.gov.companieshouse.lookup.validation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class ValidationHandler {

    private static Object[] getValidationArgs(Map<String, String> errorArgs) {

        if (errorArgs == null) {
            return new Object[]{};
        }

        if (errorArgs.get("lower") != null && errorArgs.get("upper") != null) {
            ArrayList<String> args = new ArrayList<>();
            args.add(errorArgs.get("lower"));
            args.add(errorArgs.get("upper"));
            return args.toArray();
        } else {
            return errorArgs.values().toArray();
        }
    }

    public void bindValidationErrors(BindingResult bindingResult, List<ValidationError> errors) {
        errors.sort(Comparator.comparing(ValidationError::getFieldPath)
            .thenComparing(ValidationError::getMessageKey));

        errors.forEach(error ->
            bindingResult.rejectValue(error.getFieldPath(),
                error.getMessageKey(),
                getValidationArgs(error.getMessageArguments()),
                null)
        );
    }
}
