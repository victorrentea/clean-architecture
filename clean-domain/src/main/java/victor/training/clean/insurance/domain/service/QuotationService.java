package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.crm.domain.door.CRMDoor;
import victor.training.clean.crm.domain.door.events.CustomerNameChangedEvent;
import victor.training.clean.insurance.domain.entity.InsurancePolicy;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CRMDoor crmDoor;

   public void quoteCustomer(Long customerId, String customerName) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setCustomerName(customerName);
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }
// dubious practice as fetching the customerKnob on the fly is super fast in a modulith.!±!
//   @EventListener
//   public void onCustNameChanged(CustomerNameChangedEvent event) {
//      // todo find policiy update name;
//   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
//      String customerName = crmDoor.getCustomerById(policy.getCustomerId()).getName();
      System.out.println("Insurange Policy for " + policy.getCustomerName());
   }
}
