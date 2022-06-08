package victor.training.clean.domain.customer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.customer.api.dto.CustomerInfantDto;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;

// future wannabe REST API
@Component
@RequiredArgsConstructor
public class CustomerApi {
    private final CustomerRepo customerRepo;
    public CustomerInfantDto fetchCustomerById(long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();
        return new CustomerInfantDto(customer.getId(), customer.getName());

    }
}
