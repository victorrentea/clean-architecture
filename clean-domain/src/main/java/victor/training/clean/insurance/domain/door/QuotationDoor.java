package victor.training.clean.insurance.domain.door;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.insurance.domain.door.dto.QuotationRequestKnob;
import victor.training.clean.insurance.domain.service.QuotationService;

@Component
@RequiredArgsConstructor
public class QuotationDoor {
    private final QuotationService quotationService;

    public void quoteCustomer(QuotationRequestKnob knob) throws RuntimeException {
        //        quotationService.quoteCustomer(new DomainObject);
        quotationService.quoteCustomer(knob);
    }
}
