package victor.training.clean.core.service;

import victor.training.clean.core.model.AnafResult;

public interface AnafQueryPort {
  AnafResult query(String legalEntityCode);
}
