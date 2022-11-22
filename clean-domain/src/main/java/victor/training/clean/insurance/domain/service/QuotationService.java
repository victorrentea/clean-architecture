package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.door.QuotationServiceForCustomer;
import victor.training.clean.customer.door.CustomerDoor;
import victor.training.clean.customer.door.knob.CustomerKnob;
import victor.training.clean.insurance.domain.model.InsurancePolicy;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService  {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerDoor customerDoor;

   public void quoteCustomer(Long customerId, String customerName) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setCustomerName(customerName);
//      policy.setCustomerName(customerDoor.getCustomer(policy.getCustomerId()).getName());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      // can I avoid this call?
//      CustomerKnob customer = customerDoor.getCustomer(policy.getCustomerId());

      String customerName = policy.getCustomerName();
      System.out.println("Insurange Policy for " + customerName);
   }
}
