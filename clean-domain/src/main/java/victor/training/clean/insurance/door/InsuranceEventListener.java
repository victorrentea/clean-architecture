package victor.training.clean.insurance.door;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.door.event.CustomerRegisteredEvent;
import victor.training.clean.insurance.domain.service.QuotationService;

@Component
public class InsuranceEventListener {
  private final QuotationService quotationService;

  public InsuranceEventListener(QuotationService quotationService) {
    this.quotationService = quotationService;
  }

  @EventListener // @Subscribe
//  @Async // fire-and-forget on a different thread + 5 weird bugs / year
  public void onCustomerRegisteredEvent(CustomerRegisteredEvent event) {
    quotationService.quoteCustomer(event.getCustomerId());
    // IMPORTANT NOTE: the events are dispatched via memory instantaneous, in the same thread, thus in the same transaction.
  }
}
