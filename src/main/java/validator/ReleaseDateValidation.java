package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ReleaseDateValidationImpl.class)
public @interface ReleaseDateValidation {
    String message() default "дата не может быть раньше 28 декабря 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
