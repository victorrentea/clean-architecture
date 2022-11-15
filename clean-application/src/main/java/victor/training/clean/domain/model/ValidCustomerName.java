package victor.training.clean.domain.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.repo.CustomerRepo;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Constraint(validatedBy = MyValidatorDontDoIt.class)
@NotNull@NotBlank
@Size(min = 3)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomerName {
//    @Component
//    public class MyValidatorDontDoIt implements ConstraintValidator {
//        @Autowired
//        private CustomerRepo customerRepo;
//
//        @Override
//        public void initialize(Annotation constraintAnnotation) {
//            ConstraintValidator.super.initialize(constraintAnnotation);
//        }
//
//        @Override
//        public boolean isValid(Object value, ConstraintValidatorContext context) {
//            return false;
//        }
//    }
}
