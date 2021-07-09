package victor.training.clean.infra;

import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.insurance.entity.CustomerVO;
import victor.training.clean.insurance.service.ExternalCustomerProvider;

public class CustomerRestClient implements ExternalCustomerProvider {
   @Override
   public CustomerVO findCustomerById(long id) {
//      RestTemplate
//      FeignClient.get()
      CustomerDto dto = null;// TODO;
      return new CustomerVO(dto.id, dto.name);
   }
}
