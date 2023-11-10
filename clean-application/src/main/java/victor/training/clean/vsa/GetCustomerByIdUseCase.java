package victor.training.clean.vsa;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.entity.Customer;
import victor.training.clean.application.repo.CustomerRepo;

import java.time.format.DateTimeFormatter;

//class AltUseCase2 {
//  public void method() {
// tipa sonar si IntelliJ la linia urmatoare
//    var r = new GetCustomerByIdUseCase.GetCustomerByIdResponse(
//        null, null,null,null,null,false,null);
//  }
//}

@RequiredArgsConstructor
@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @Builder
  @VisibleForTesting //+ package protection
  // Sonar/IntelliJ tipa daca folosesti clasa asta in alta clasa din src/main
      // NU vreau ca sa pasez de colo colo obiecte de API prin codul meu.
      // ci doar obiecte interne (domain)
  // Dar o lasa sa fie folosita din src/test
      // vreau sa o pot instantia
  record GetCustomerByIdResponse(
      Long id,
      String name,
      String email,
      Long siteId,
      String creationDateStr,
      boolean gold,
      String goldMemberRemovalReason) { // JSON
  }

  @GetMapping("customer/{id}")
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
