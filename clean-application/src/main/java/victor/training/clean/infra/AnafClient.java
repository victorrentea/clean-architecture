package victor.training.clean.infra;

import org.springframework.stereotype.Component;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.AnafResult;

@Component
public class AnafClient {
  public AnafResult query(String legalEntityCode) {
    throw new RuntimeException("Not implemented yet");
  }
}
