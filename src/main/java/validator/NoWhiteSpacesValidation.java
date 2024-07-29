package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NoWhiteSpaceValidation.class)
public @interface NoWhiteSpacesValidation {
    String message() default "поле содержит пробелы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
