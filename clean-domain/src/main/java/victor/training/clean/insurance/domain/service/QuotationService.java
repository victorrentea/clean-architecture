package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.domain.door.CustomerDoor;
import victor.training.clean.insurance.domain.door.dto.QuotationRequestKnob;
import victor.training.clean.insurance.domain.model.InsurancePolicy;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final CustomerDoor customerDoor;
   private final InsurancePolicyRepo insurancePolicyRepo;

   public void quoteCustomer(QuotationRequestKnob dto) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + dto.getCustomerId());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(dto.getCustomerId());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();


      String customerName = "customerDoor.get....";
      System.out.println("Insurange Policy for " + customerName);
   }
}
