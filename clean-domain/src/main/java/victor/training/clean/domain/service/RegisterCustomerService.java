package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterCustomerService {
  private final CustomerRepo customerRepo;
  // gu-noi
//  public Customer findById(long id) {
//    return customerRepo.findById(id).orElseThrow();
//  }

  public void register(Customer customer) {
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Customer email is already registered");
      // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Domain Logic using discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic

    customerRepo.save(customer);
    // Heavy business logic
    // aici customer.id != null

  }
}
