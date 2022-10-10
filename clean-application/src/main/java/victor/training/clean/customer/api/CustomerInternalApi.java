package victor.training.clean.customer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.api.dto.CustomerInternalDto;
import victor.training.clean.customer.model.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Component
@RequiredArgsConstructor
public class CustomerInternalApi {
    private final CustomerRepo customerRepo;

    public CustomerInternalDto findById(Long customerId) {
        return toInternalDto(customerRepo.findById(customerId).orElseThrow());
    }

    private CustomerInternalDto toInternalDto(Customer entity) {
        return new CustomerInternalDto(entity.getId(), entity.getName());
    }
}
