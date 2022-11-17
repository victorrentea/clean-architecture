package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.door.CustomerDoor;
import victor.training.clean.customer.door.event.CustomerRegisteredEvent;
import victor.training.clean.customer.door.knob.CustomerKnob;
import victor.training.clean.insurance.domain.model.InsurancePolicy;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerDoor customerDoor;

   @EventListener
   public void onCustomerRegisteredEvent(CustomerRegisteredEvent event) {
      quoteCustomer(event.getCustomerId(), getClass().getName());
      // a) thin event "notification" with just the ID > followed by a call for the data through th door
      // b) fat event (Event-Carried State Transfer) with *more* data > no need for the call
   }
   public void quoteCustomer(long customerId, String customerName) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setCustomerName(customerName);
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
//      CustomerKnob customer = customerDoor.findCustomerById(policy.getCustomerId());
//      String customerName = customer.getName(); // where do i get this from ?
      String customerName = policy.getCustomerName();
      System.out.println("Insurange Policy for " + customerName);
   }
}
