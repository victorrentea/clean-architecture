package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.common.events.CustomerRegisteredEvent;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class RegisterCustomerService {
   private final CustomerRepo customerRepo;
   private final ApplicationEventPublisher eventPublisher;

   public Long register(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic

//      quotationService.requoteCustomer(customer);
      eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId()));

return customer.getId();
   }
}
