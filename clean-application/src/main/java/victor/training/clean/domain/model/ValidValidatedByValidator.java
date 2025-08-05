package victor.training.clean.domain.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class ValidValidatedByValidator implements ConstraintValidator<ValidValidatedBy, Customer> {
  @Override
  // separate file guarding the constraint
  public boolean isValid(Customer customer, ConstraintValidatorContext context) {
    if (customer == null) return true;
    Customer.Status status = customer.getStatus();
    if ((status == Customer.Status.VALIDATED || status == Customer.Status.ACTIVE || status == Customer.Status.DELETED)
        && customer.getValidatedBy() == null) {
      return false;
    }
    return true;
  }
}