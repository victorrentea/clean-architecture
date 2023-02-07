package victor.training.clean.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.application.dto.CustomerView;

public interface CustomerApplicationServiceApiCuMetadate {
  @GetMapping("{id}")
  CustomerView findById(@PathVariable long id);
}
