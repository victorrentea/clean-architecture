package victor.training.clean.insurance.door;

import org.springframework.stereotype.Component;
import victor.training.clean.insurance.domain.service.QuotationService;


// Option B: customer module --> InsuranceDoor
@Component
public class InsuranceDoor {
  private final QuotationService quotationService;

  public InsuranceDoor(QuotationService quotationService) {
    this.quotationService = quotationService;
  }

  public void quoteCustomer(Long customerId) {
    quotationService.quoteCustomer(customerId, "TODO");
  }
}
