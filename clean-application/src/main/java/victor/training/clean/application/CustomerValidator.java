package victor.training.clean.application;

import org.springframework.stereotype.Component;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;

@Component
public class CustomerValidator {
  private final CustomerRepo customerRepo;

  public CustomerValidator(CustomerRepo customerRepo) {
    this.customerRepo = customerRepo;
  }

  void validate(Customer customer) {
    // move inside the model. HoW?
//    if (customer.getName().length() < 5) {
//      throw new IllegalArgumentException("Name too short");
//    }

    // remains here as it hits the database
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Customer email is already registered");
      // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }
  }
}