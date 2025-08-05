package victor.training.clean.domain.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidValidatedByValidator.class)
public @interface ValidValidatedBy {
  String message() default "validatedBy must be non-null when status is VALIDATED or later";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}