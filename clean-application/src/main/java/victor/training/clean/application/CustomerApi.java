package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.application.dto.CustomerDto;

public interface CustomerApi {
  @GetMapping("{id}")
  @Operation(description = "Customer find")
  CustomerDto findById(@PathVariable long id);
}
