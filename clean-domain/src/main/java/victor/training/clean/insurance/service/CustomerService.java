package victor.training.clean.insurance.service;

import victor.training.clean.insurance.entity.Customer;

public interface CustomerService {
   Customer findById(long customerId);
}
