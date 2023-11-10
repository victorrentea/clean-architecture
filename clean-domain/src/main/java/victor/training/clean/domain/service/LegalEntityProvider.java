package victor.training.clean.domain.service;

import victor.training.clean.domain.model.AnafResult;

public interface LegalEntityProvider {
  public AnafResult query(String legalEntityCode);
}
