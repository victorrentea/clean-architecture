package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.AnafResult;

@Component
public class AnafClientImpl implements victor.training.clean.domain.service.AnafClient {
  @Override
  public AnafResult query(String legalEntityCode) {
    // Imagine Dragons here
    // Adapter that calls the ANAF endpoint and maps their result into a Domain Model object
    // Omited for brevity of the example
    return new AnafResult("CompanyName", true);
  }
}
