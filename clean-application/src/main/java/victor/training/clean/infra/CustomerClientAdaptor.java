package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.facade.CustomerFacade;
import victor.training.clean.quotation.entity.Customer;
import victor.training.clean.quotation.service.ICustomerClient;

@Service
@RequiredArgsConstructor
public class CustomerClientAdaptor implements ICustomerClient {
   private final CustomerFacade customerFacade;
   @Override
   public Customer retrieveById(Long customerId) {
//      return customerFacade.findById(customerId).conver/......;
      return null;
   }
}
