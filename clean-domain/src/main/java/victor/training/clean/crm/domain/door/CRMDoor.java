package victor.training.clean.crm.domain.door;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.crm.domain.door.knob.CustmerKnob;
import victor.training.clean.crm.domain.entity.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;

@Component
@RequiredArgsConstructor
public class CRMDoor {
  private final CustomerRepo customerRepo;


  public CustmerKnob getCustomerById(Long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustmerKnob(customer.getId(), customer.getName());
  }
  
}

