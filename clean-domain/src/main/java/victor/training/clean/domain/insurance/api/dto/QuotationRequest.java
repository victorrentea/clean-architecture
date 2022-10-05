package victor.training.clean.domain.insurance.api.dto;

import lombok.Value;

@Value
public class QuotationRequest {
    long customerId;
    String customerName;
}
