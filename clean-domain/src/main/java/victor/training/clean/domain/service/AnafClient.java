package victor.training.clean.domain.service;

import victor.training.clean.domain.model.AnafResult;

public interface AnafClient {
  AnafResult query(String legalEntityCode);
}
