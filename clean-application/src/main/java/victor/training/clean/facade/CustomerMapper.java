package victor.training.clean.facade;

import org.springframework.stereotype.Component;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.facade.dto.CustomerDto;
@Component
public class CustomerMapper {

   public CustomerDto getCustomerDto(Customer customer) { // customer Entity
      return new CustomerDto(customer); // OK as Dto (=garbage) are ok to depend on Entities (=sacred ground)
   }
}