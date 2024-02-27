package victor.training.clean.vsa;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RequiredArgsConstructor
@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo; // take advantage of Spring's Data repos

  @Builder
  @VisibleForTesting // for testing purposes only, fails Sonar if other prod code uses it
  record GetCustomerByIdResponse(
      Long id,
      String name,
      String email,
      Long siteId,
      boolean gold,
      int discountPercentage,
      String goldMemberRemovalReason) {
  }

  @GetMapping("customer/{id}/vsa") // ONE endpoint only
  public GetCustomerByIdResponse findById(@PathVariable long id) { // RULE one single public method
      Customer customer = customerRepo.findById(id).orElseThrow();
      return toDto(customer);
  }

  private GetCustomerByIdResponse toDto(Customer customer) {
    return GetCustomerByIdResponse.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .discountPercentage(customer.discountPercentage())
        .siteId(customer.getCountry().getId())
        .build();
  }
}
// don't exceed 150-200 lines -> after: extract