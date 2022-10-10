package victor.training.clean.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.customer.api.CustomerInternalApi;
import victor.training.clean.customer.api.dto.CustomerInternalDto;
import victor.training.clean.customer.api.event.CustomerRegisteredEvent;
import victor.training.clean.customer.model.Customer;
import victor.training.clean.insurance.model.InsurancePolicy;
import victor.training.clean.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerInternalApi customerInternalApi;

//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   @EventListener
   public void onCustomerRegistered(CustomerRegisteredEvent event) {
      quoteCustomer(event.getCustomerId());
   }
   public void quoteCustomer(long customerId) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
//      eventPublisher
   }
   private final ApplicationEventPublisher eventPublisher;

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();

      // between this and @ManyToOne, pick @ManyToOne
//      Customer theSacredEntityOfTheOthers = customerServiceFromCustomerModule.findById(policy.getCustomerId());

      CustomerInternalDto customerDto = customerInternalApi.findById(policy.getCustomerId());
//      String customerName = policy.getCustomer().getName();
      String customerName = customerInternalApi.findById(policy.getId()).getName();
//
      //      policy.getCustomer().setName("Oups! Take that!");
      System.out.println("Insurange Policy for " + customerName);
   }
}
