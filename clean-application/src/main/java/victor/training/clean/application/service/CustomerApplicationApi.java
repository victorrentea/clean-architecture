package victor.training.clean.application.service;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.dto.CustomerDto;

public interface CustomerApplicationApi {
  @Operation(description = "Register Customer with validation and business rules and external API call and email notification and more business logic (imagine) ")
  @PostMapping("customers")
  void register(@RequestBody @Validated CustomerDto dto);
}
