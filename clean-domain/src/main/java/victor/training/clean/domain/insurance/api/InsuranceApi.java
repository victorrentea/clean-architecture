package victor.training.clean.domain.insurance.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.insurance.api.dto.QuotationRequest;
import victor.training.clean.domain.insurance.service.QuotationService;

@RequiredArgsConstructor
@Service
public class InsuranceApi {
    private final QuotationService quotationService;
    public void requoteCustomer(QuotationRequest quotationRequest) {
        quotationService.quoteCustomer(quotationRequest.getCustomerId(), quotationRequest.getCustomerName());
    }
}
