package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    public void registerCustomer(Customer customer) {
       // Heavy business logic
       // Heavy business logic
       // Heavy business logic
       // TODO Where can I move this little logic? (... operating on the state of a single entity)
       int discountPercentage = 3;
       if (customer.isGoldMember()) {
          discountPercentage += 1;
       }
       System.out.println("Biz Logic with discount " + discountPercentage);
       // Heavy business logic
       // Heavy business logic
       customerRepo.save(customer);
       // Heavy business logic
    }
}
