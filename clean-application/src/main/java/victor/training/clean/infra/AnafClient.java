package victor.training.clean.infra;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.customer.AnafResult;
import victor.training.clean.domain.service.AnafClientInterface;

@Component
public class AnafClient implements AnafClientInterface {
  @Override
  @Timed // 90 sec SOAP
  public AnafResult query(String legalEntityCode) {
    // Imagine Dragons here
    // Adapter that calls the ANAF endpoint and maps their result into a Domain Model object
    // Omited for brevity of the example
    return new AnafResult("CompanyName", true);
  }
}
