package victor.training.clean.crm.domain.service;

import victor.training.clean.crm.domain.model.LegalEntity;

public interface LegalEntityProvider {
  LegalEntity query(String legalEntityCode);
}
