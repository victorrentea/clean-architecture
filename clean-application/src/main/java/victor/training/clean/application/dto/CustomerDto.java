package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Site;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto folosit pt WRITE
  @Schema(description = "Name of the customer")
  @Size(min = 5)
  @NotNull
  String name;
  String email;
  Long siteId;
  boolean gold;
  String goldMemberRemovalComment;


  public Customer toEntity() {
    Customer customer = new Customer();
    customer.setEmail(this.email);
    customer.setName(this.name);
    customer.setCreationDate(LocalDate.now());
    customer.setSite(new Site().setId(getSiteId()));
    return customer;
  }
}
