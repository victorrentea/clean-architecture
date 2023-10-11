package victor.training.clean.infra;

import victor.training.clean.domain.model.AnafResult;

public interface FiscalAuthority {
  AnafResult query(String legalEntityCode);
}
