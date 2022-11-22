package victor.training.clean.customer.door;

import victor.training.clean.customer.domain.model.Customer;

// Option A: customer provides an interface that insurance implements
public interface QuotationServiceForCustomer {
  void quoteCustomer(Long customerId);
  // if this method is part of the domain service, what objects should it take/return ?

  // if the quotation domain service implements this interface,
  // my domain model is leaked (exposed) to other modules
}
