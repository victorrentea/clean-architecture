package victor.training.clean.facade.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.ValidCustomerName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Builder
@Value
public class CustomerDto {
   Long id;
   @Schema(description = "Name of the customer")
//   @NotNull
//   @NotBlank
//   @Size(min = 3)
   @ValidCustomerName
   String name;
//   @ValidCustomerEmail
   String email;
   Long siteId;
   String creationDateStr;

}
