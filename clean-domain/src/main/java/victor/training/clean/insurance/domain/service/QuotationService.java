package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.door.CustomerDoor;
import victor.training.clean.customer.door.events.CustomerRegisteredEvent;
import victor.training.clean.insurance.domain.model.InsurancePolicy;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerDoor customerDoor;

@EventListener // in-memory syncrhronuous in-transaction event.
   public void onCustomerRegisteredEvent(CustomerRegisteredEvent event) {
      quoteCustomer(event.getCustomerId(), event.getCustomerName());
   // running in the same tx as the CustomerService
   }

   public void quoteCustomer(Long customerId, String customerName) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setCustomerName(customerName);
      // to fill data on my side
      // option1: notification event + call back to source
//      customerDoor.getCustomerById(customerId).getName()
      // option2: fat events

      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
//      CustomerKnob customer = customerDoor.getCustomerById(policy.getCustomerId());
//      String customerName = customer.getName();
      String customerName = policy.getCustomerName();
      System.out.println("Insurange Policy for " + customerName);
   }
}
