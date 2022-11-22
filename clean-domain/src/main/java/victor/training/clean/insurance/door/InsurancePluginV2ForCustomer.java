//package victor.training.clean.insurance.door;
//
//import org.springframework.stereotype.Component;
//import victor.training.clean.customer.door.QuotationServiceForCustomer;
//import victor.training.clean.insurance.domain.service.QuotationService;
//
//@Component
//public class InsurancePluginV2ForCustomer implements QuotationServiceForCustomer {
//  private final QuotationService  quotationService;
//
//  public InsurancePluginV2ForCustomer(QuotationService quotationService) {
//    this.quotationService = quotationService;
//  }
//
//  @Override
//  public void quoteCustomer(Long customerId) {
//    quotationService.quoteCustomer(customerId);
//  }
//}
