package victor.training.clean.application.usecase;

import victor.training.clean.application.entity.AnafResult;

public interface LegalEntityProvider {
  public AnafResult query(String legalEntityCode);
}
