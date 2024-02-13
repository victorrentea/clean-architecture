package victor.training.clean.infra;

import victor.training.clean.domain.model.AnafResult;

public interface FiscalDetailsProvider {
  AnafResult query(String legalEntityCode);
}
