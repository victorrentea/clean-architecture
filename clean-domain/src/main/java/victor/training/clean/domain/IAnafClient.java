package victor.training.clean.domain;

import victor.training.clean.domain.model.AnafResult;

public interface IAnafClient {
  AnafResult query(String legalEntityCode);
}
