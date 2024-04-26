package victor.training.clean.vsa;

import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

  @Builder
  @VisibleForTesting // tests can acces package-private classes, but not other clases
  // in src/main
  record GetCustomerByIdResponse(
      Long id,
      String name,
      String email,
      Long siteId,
      String creationDateStr,
      boolean gold,
      String goldMemberRemovalReason) {
  }

//  interface Mapt

  @GetMapping("customer/{id}/vsa")
  public GetCustomerByIdResponse findById(@PathVariable long id) {
      Customer customer = customerRepo.findById(id).orElseThrow();
      return GetCustomerByIdResponse.builder()
              .id(customer.getId())
              .name(customer.getName())
              .email(customer.getEmail())
              .siteId(customer.getCountry().getId())
              .creationDateStr(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
              .build();
  }
}
