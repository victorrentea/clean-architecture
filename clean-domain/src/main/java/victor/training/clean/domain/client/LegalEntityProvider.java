package victor.training.clean.domain.client;

import victor.training.clean.domain.model.LegalEntity;

public interface LegalEntityProvider {
  LegalEntity query(String legalEntityCode);
}
