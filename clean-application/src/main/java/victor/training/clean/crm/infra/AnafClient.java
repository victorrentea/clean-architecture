package victor.training.clean.crm.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.insurance.client.LegalEntityProvider;
import victor.training.clean.crm.domain.model.LegalEntity;

@Component
public class AnafClient implements LegalEntityProvider {
  @Override
  public LegalEntity query(String legalEntityCode) {
    // Imagine Dragons here
    // Adapter that calls the ANAF endpoint and maps their result into a Domain Model object
    // Omited for brevity of the example
    return new LegalEntity("CompanyName", true);
  }
}
