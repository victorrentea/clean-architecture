package victor.training.clean.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.common.event.CustomerRegisteredEvent;

@Service
@RequiredArgsConstructor
public class AuditService {
//   private final CustomerClient customerClient;
   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void auditCustomerRegistered(CustomerRegisteredEvent event) {
      System.out.println(event.getCustomerId());
//      CustomerVO myCustomer = customerClient.retrieveById(event.getCustomerId());
   }
}
