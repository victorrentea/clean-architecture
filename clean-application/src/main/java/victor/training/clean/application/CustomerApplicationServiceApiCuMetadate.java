package victor.training.clean.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.application.dto.CustomerDto;

public interface CustomerApplicationServiceApiCuMetadate {
  @GetMapping("{id}")
  CustomerDto findById(@PathVariable long id);
}
