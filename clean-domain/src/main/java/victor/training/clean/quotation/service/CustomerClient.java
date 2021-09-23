package victor.training.clean.quotation.service;

import victor.training.clean.customer.entity.Customer;
import victor.training.clean.quotation.entity.CustomerVO;

public interface CustomerClient {
   CustomerVO getCustomer(long customerId);
}
