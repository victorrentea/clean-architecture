package victor.training.clean.insurance.service;

import victor.training.clean.insurance.entity.CustomerVO;

public interface ExternalCustomerProvider {
   CustomerVO findCustomerById(long id);
}
