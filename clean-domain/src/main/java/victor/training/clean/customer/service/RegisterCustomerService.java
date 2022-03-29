package victor.training.clean.customer.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.api.events.CustomerRegisteredEvent;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Service
public class RegisterCustomerService {
   private final CustomerRepo customerRepo;
   private final ApplicationEventPublisher eventPublisher;

   public RegisterCustomerService(CustomerRepo customerRepo, ApplicationEventPublisher eventPublisher) {
      this.customerRepo = customerRepo;
      this.eventPublisher = eventPublisher;
   }

   public void register(Customer customer) {

      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic

      customerRepo.save(customer);
      // Heavy business logic
//      quotationService.quoteCustomer(customer);
      eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId()));
   }
}
