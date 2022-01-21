package victor.training.clean.quotation.service;

import victor.training.clean.quotation.entity.Customer;

public interface ICustomerClient {
   Customer retrieveById(Long customerId);
}
