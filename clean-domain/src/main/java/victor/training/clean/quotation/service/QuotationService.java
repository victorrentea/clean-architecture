package victor.training.clean.quotation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.common.events.CustomerRegisteredEvent;
import victor.training.clean.quotation.entity.InsurancePolicy;
import victor.training.clean.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerClient customerClient;

   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   // @ServiceActivator  Messafe Listener in 1y
   public void requoteCustomer(CustomerRegisteredEvent event) {
      log.debug("Requoting customer (~230 total lines of code): " /*+ customer.getName()*/);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(event.getCustomerId());

      customerClient.getCustomer(event.getCustomerId());

//      policy.setCustomerName(event.getCustomerName());
      policy.setValue(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }


   public void laterFlow() {
      InsurancePolicy policy;
//      p.getCustomerName;

   }
}
