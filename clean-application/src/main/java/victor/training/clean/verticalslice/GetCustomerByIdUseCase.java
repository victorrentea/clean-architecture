package victor.training.clean.verticalslice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
//@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @Builder
  @Value
  @AllArgsConstructor
  static class GetCustomerByIdResponse {
    Long id;
    String name;
    String email;
    Long siteId;
    String creationDateStr;
    boolean gold;
    String goldMemberRemovalReason;
  }

  @GetMapping("customer/{id}")
  public GetCustomerByIdResponse findById(@PathVariable long id) {
      Customer customer = customerRepo.findById(id).orElseThrow();
      return GetCustomerByIdResponse.builder()
              .id(customer.getId())
              .name(customer.getName())
              .email(customer.getEmail())
              .siteId(customer.getSite().getId())
              .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
              .build();
  }
}
