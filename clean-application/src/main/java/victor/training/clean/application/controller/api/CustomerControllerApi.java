package victor.training.clean.application.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.controller.api.dto.CustomerDto;

public interface CustomerControllerApi {
  @PostMapping("customers")
  @Operation(description = "Register Customer",
      summary = "Register a new Customer",
      tags = {"Customer"})
  void register(@RequestBody @Validated CustomerDto dto);
}
