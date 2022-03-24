package victor.training.clean.customer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.api.dto.CustomerVO;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@Component
@RequiredArgsConstructor
public class CustomerApi {
   private final CustomerRepo customerRepo;
   public CustomerVO getCustomer(Long customerId) {
      Customer customer = customerRepo.findById(customerId).get();
      return new CustomerVO(customer.getId(), customer.getName(), customer.getEmail());
   }
}
