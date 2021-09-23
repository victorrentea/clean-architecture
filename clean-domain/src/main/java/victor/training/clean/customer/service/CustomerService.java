package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.common.events.CustomerRegisteredEvent;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;
   private final ApplicationEventPublisher eventPublisher;

   public void registerCustomer(Customer customer) {

      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic

      eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId()));

   }

   public Customer getById(long customerId) {
      return null;
   }
}
