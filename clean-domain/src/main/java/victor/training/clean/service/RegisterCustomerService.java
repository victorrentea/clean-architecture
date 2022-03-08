package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.Customer;
import victor.training.clean.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterCustomerService {
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
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic
   }
}
