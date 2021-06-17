package victor.training.clean.quotation.service;

import victor.training.clean.quotation.entity.CustomerVO;

public interface ExternalCustomerProvider {
   CustomerVO getCustomerById(long id);
}
