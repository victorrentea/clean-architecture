package victor.training.clean.domain.service;

import victor.training.clean.domain.model.AnafResult;

public interface FiscalDetailsProvider {
  AnafResult query(String legalEntityCode);
}
