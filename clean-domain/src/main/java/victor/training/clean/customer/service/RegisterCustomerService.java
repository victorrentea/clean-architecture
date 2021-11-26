package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.common.events.RequoteCustomerEvent;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class RegisterCustomerService {
   private final CustomerRepo customerRepo;
   private final ApplicationEventPublisher eventPublisher;

   public void register(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      eventPublisher.publishEvent(new RequoteCustomerEvent(customer.getId()));

//      quotationService.requoteCustomer(customer.getId());
      // Heavy business logic
   }
}
