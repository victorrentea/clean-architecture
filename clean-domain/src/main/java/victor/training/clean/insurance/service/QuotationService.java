package victor.training.clean.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.api.CustomerApi;
import victor.training.clean.customer.api.dto.CustomerVO;
import victor.training.clean.customer.api.event.CustomerRegistrationCreatedEvent;
import victor.training.clean.insurance.entity.InsurancePolicy;
import victor.training.clean.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerApi customerApi;
   @EventListener
   public void onCustomerRegisteredAgain(CustomerRegistrationCreatedEvent event) {

   }
   @EventListener
   public void onCustomerRegistered(CustomerRegistrationCreatedEvent event) {
      log.debug("Event " + event.getCustomerId());
//      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
//      policy.setCustomer(customer);
      policy.setCustomerId(event.getCustomerId());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }


   public void sendPaymentReminder(InsurancePolicy policy) {
      CustomerVO customer= customerApi.getCustomer(policy.getCustomerId());
      String customerEmail = customer.getEmail();
      // one idea to send an event to the customer asking for the customer details
      // they will reply with a message with the details



//      publisEvent (customerId, Text for email)  to customer microservice > sends my email to it.www
   }
}
