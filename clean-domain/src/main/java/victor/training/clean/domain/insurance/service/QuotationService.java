package victor.training.clean.domain.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.domain.customer.api.CustomerApi;
import victor.training.clean.domain.customer.api.dto.CustomerInfantDto;
import victor.training.clean.domain.customer.api.event.CustomerRegisteredEvent;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.insurance.model.InsurancePolicy;
import victor.training.clean.domain.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerApi customerApi;

//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   @EventListener
   @Transactional
   public void quoteCustomer(CustomerRegisteredEvent event) {
      String name = customerApi.fetchCustomerById(event.getCustomerId()).getName();
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): "
                + name);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(event.getCustomerId());
//      policy.setCustomerName(event.getName());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(Long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      CustomerInfantDto customer = customerApi.fetchCustomerById(policy.getCustomerId());
      System.out.println(customer.getName());

   }
}

// INSERT INTO MESSAGES_TO_SEND(....)values(... TO_SEND)
// @Scheduled ... SELELCT FOR UPDATE WHEERE = TO_SEND... later:> UPDATE SET =SENT