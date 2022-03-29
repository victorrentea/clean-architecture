package victor.training.clean.customer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.api.dto.CustomerDto;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Component
@RequiredArgsConstructor
public class CustomerApi { // REST API tomorrow
   private final CustomerRepo customerRepo;
   public CustomerDto getCustomerById(Long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      return new CustomerDto(customer.getName());
   }
}
