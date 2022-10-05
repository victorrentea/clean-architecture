package victor.training.clean.domain.customer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.api.dto.CustomerDto;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class CustomerApi {
    private final CustomerRepo customerRepo;
    public CustomerDto getCustomerById(long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();
        return new CustomerDto(customer.getId(), customer.getName());
    }
}
