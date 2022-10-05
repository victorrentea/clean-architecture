package victor.training.clean.domain.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.domain.insurance.api.InsuranceApi;
import victor.training.clean.domain.insurance.api.dto.QuotationRequest;
import victor.training.clean.domain.insurance.service.QuotationService;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterCustomerService {
    private final CustomerRepo customerRepo;
    private final InsuranceApi insuranceApi;
    ;
    public void registerCustomer(Customer customer) {
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic

        // TODO Where can I move this little logic? (... operating on the state of a single entity)
        int discountPercentage = customer.getDiscountPercentage();
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
        QuotationRequest quotationRequest = new QuotationRequest(customer.getId(), customer.getName());
        insuranceApi.requoteCustomer(quotationRequest);

    }

}
