package victor.training.clean.insurance.client;

import victor.training.clean.crm.domain.model.LegalEntity;

public interface LegalEntityProvider {
  LegalEntity query(String legalEntityCode);
}
