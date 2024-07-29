package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidationImpl implements ConstraintValidator<ReleaseDateValidation, LocalDate> {
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(minDate);
    }
}
