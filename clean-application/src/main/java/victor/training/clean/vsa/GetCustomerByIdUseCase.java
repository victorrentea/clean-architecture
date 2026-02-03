package victor.training.clean.vsa;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @GetMapping("customer/{id}/vsa") // 1..2 HTTP entry points / message listner
  public GetCustomerByIdResponse findById(@PathVariable long id) {
//    if // security/privacy
      Customer customer = customerRepo.findById(id).orElseThrow();
    return GetCustomerByIdResponse.builder() // mapper
              .id(customer.getId())
              .name(customer.getName())
              .email(customer.getEmail())
              .siteId(customer.getCountry().getId())
              .creationDateStr(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
              .build();
  }

  @Builder
  record GetCustomerByIdResponse( // DTO
      Long id,
      String name,
      String email,
      Long siteId,
      String creationDateStr,
      boolean gold,
      String goldMemberRemovalReason) {
  }
}
