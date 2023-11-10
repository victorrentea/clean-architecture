package victor.training.clean.out.anaf;

import org.springframework.stereotype.Component;
import victor.training.clean.application.entity.AnafResult;
import victor.training.clean.application.port.LegalEntityPort;

@Component
public class AnafClient implements LegalEntityPort {
  public AnafResult query(String legalEntityCode) {
    // Imagine Dragons here
    // Adapter that calls the ANAF endpoint and maps their result into a Domain Model object
    // Omited for brevity of the example
    return new AnafResult("CompanyName", true);
  }
}
