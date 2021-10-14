package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.event.CustomerRegisteredEvent;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Service //#respect
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;
   private final QuotationService quotationService;
   private final ApplicationEventPublisher eventPublisher;

   @Transactional
   public void register(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customer.activate();

      customerRepo.save(customer);
      // Heavy business logic

      quotationService.requoteCustomer(customer);



      eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId()));
//      messageSender.send(new CustomerRegisteredMessage(customer.getId()));
      System.out.println("M-am intors");
   }
}
