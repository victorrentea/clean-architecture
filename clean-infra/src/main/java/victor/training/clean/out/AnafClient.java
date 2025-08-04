package victor.training.clean.out;

import org.springframework.stereotype.Component;
import victor.training.clean.core.model.AnafResult;

@Component
public class AnafClient implements victor.training.clean.core.service.AnafQueryPort {
  @Override
  public AnafResult query(String legalEntityCode) {
    // Imagine Dragons here
    // Adapter that calls the ANAF endpoint and maps their result into a Domain Model object
    // Omited for brevity of the example
    return new AnafResult("CompanyName", true);
  }
}
