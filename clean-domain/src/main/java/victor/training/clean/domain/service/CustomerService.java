package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepo customerRepo;

  public void register(Customer customer) {
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();

    // enum {REGULAR(3), GOLD(4), BUSINESS(5), PLATINUM(5) }
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
    customerRepo.save(customer);
    // Heavy business logic
  }

  //  public Optional<Customer> findById(long customerId) {
  //    return customerRepo.findById(customerId); // code smell : Middle Man
  //  }
}
