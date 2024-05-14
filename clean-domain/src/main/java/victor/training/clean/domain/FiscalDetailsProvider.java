package victor.training.clean.domain;

import victor.training.clean.domain.model.AnafResult;

public interface FiscalDetailsProvider {
  AnafResult query(String legalEntityCode);
}
