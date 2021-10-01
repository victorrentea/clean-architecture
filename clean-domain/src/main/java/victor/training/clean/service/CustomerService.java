package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.Customer;

@Service
@RequiredArgsConstructor
public class CustomerService {
//   private final CustomerHistoryRepo repo;
   public void registerCustomer(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
//		CustomerHistory cust = cHrepo.
      int discountPercentage = customer.getDiscountPercentage(/*customerHistory*/);
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
   }
}
