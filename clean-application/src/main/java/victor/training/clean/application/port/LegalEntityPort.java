package victor.training.clean.application.port;

import victor.training.clean.application.entity.AnafResult;

public interface LegalEntityPort {
  public AnafResult query(String legalEntityCode);
}
