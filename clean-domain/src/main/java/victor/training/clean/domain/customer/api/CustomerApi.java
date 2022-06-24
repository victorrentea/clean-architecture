package victor.training.clean.domain.customer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.customer.api.dto.CustomerInternalDto;
import victor.training.clean.domain.customer.entity.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;

@RequiredArgsConstructor
@Component
public class CustomerApi {
    private final CustomerRepo customerRepo;

    public CustomerInternalDto retrieveById(Long customerId) {
        Customer c = customerRepo.findById(customerId).orElseThrow();
        return new CustomerInternalDto(c.getId(), c.getName());
    }
}
