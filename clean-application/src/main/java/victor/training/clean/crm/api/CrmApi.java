package victor.training.clean.crm.api;

import org.springframework.stereotype.Component;
import victor.training.clean.crm.api.knob.CustomerKnob;

@Component
public class CrmApi {


  public CustomerKnob getCustomerById(long customerId) {
    throw new RuntimeException("Method not implemented");
  }
}
