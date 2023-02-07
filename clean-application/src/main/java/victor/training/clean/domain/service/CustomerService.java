package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.application.CustomerApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RequiredArgsConstructor
@Service // ZEN TATA! Pace armonie, clean code
public class CustomerService {
  private final CustomerRepo customerRepo;

  // GU-NOI! cod degeaba rezultat din "Strict Layers Arch"
//  public Customer findById(long id) {
//    return customerRepo.findById(id).orElseThrow();
//  }

  public void register(Customer customer) {
    // o metoda cu 10 ifuri pline de reguli de business
    // care sade acum in Application Service
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Customer email is already registered");
      // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
    customerRepo.save(customer);
    // Heavy business logic
  }
}
