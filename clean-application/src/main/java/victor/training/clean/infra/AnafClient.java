package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.service.IAnafClient;

@Component
public class AnafClient implements IAnafClient {
  @Override
  public AnafResult query(String legalEntityCode) {
    // Imagine Dragons here
    // Adapter that calls the ANAF endpoint and maps their result into a Domain Model object
    // Omited for brevity of the example
    return new AnafResult("CompanyName", true);
  }
}
