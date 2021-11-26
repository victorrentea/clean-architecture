package victor.training.clean.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.common.events.RequoteCustomerEvent;
import victor.training.clean.insurance.entity.Customer;
import victor.training.clean.insurance.entity.InsurancePolicy;
import victor.training.clean.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerService customerAdapter;

   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   @Transactional
   public void requoteCustomer(RequoteCustomerEvent event) {
      Customer customer =  customerAdapter.findById(event.getCustomerId());
      log.debug("Requoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomer(customer);
      policy.setValue(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }
}
