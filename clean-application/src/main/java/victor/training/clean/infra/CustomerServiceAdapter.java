package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.facade.CustomerFacade;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.insurance.entity.Customer;
import victor.training.clean.insurance.service.CustomerService;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceAdapter implements CustomerService {
   private final CustomerFacade customerFacade;

   @Override
   public Customer findById(long customerId) {
      //maine cand pleaca asta in alt sistem vei face RestTempplate.get
      CustomerDto dto = customerFacade.findById(customerId);
      // daca vrei sa schimbi tranzactia, faci rest.get(localohost:8080/customer/{id} > aloca alt thread din tomcat
      return new Customer(dto.id, dto.name);
   }
}
