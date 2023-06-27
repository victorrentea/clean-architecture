package victor.training.clean.domain.service;

import victor.training.clean.domain.model.customer.AnafResult;

public interface AnafClientInterface {
  AnafResult query(String legalEntityCode);
}
