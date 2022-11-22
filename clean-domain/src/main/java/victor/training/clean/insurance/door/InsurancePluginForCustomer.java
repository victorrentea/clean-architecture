package victor.training.clean.insurance.door;

import org.springframework.stereotype.Component;
import victor.training.clean.customer.door.QuotationServiceForCustomer;
import victor.training.clean.insurance.domain.service.QuotationService;

@Component
public class InsurancePluginForCustomer implements QuotationServiceForCustomer {
  private final QuotationService  quotationService;

  public InsurancePluginForCustomer(QuotationService quotationService) {
    this.quotationService = quotationService;
  }

  @Override
  public void quoteCustomer(Long customerId) {
    quotationService.quoteCustomer(customerId);
  }
}
