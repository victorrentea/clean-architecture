package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.service.CustomerService;
import victor.training.clean.quotation.entity.CustomerVO;
import victor.training.clean.quotation.service.CustomerClient;

@RequiredArgsConstructor
@Component
public class CustomerClientImpl implements CustomerClient {
   private final CustomerService customerService;
   @Override
   public CustomerVO getCustomer(long customerId) {
      Customer customer =  customerService.getById(customerId);
      return new CustomerVO(customer.getName());
   }
}
