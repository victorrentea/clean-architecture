package victor.training.clean.vsa;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.vsa.SearchCustomerUseCase.CustomerSearchCriteria;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @Builder
  record GetCustomerByIdResponse(
      Long id,
      String name,
      String email,
      Long siteId,
      String creationDateStr,
      boolean gold,
      String goldMemberRemovalReason) {
  }

//  public void method() {
//    var v = new CustomerSearchCriteria("a", "b", 1L);
//    System.out.println(v.name());
//  }

  @GetMapping("customer/{id}/vsa")
  public GetCustomerByIdResponse execute(@PathVariable long id) {
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
