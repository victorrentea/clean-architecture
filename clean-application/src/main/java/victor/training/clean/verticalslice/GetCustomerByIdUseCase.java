package victor.training.clean.verticalslice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
//@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @GetMapping("customer/{id}")
  public CustomerDto findById(@PathVariable long id) {
      Customer customer = customerRepo.findById(id).orElseThrow();
//      return new CustomerDto(customer);
      return CustomerDto.builder()
              .id(customer.getId())
              .name(customer.getName())
              .email(customer.getEmail())
              .siteId(customer.getSite().getId())
              .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
              .build();
  }
}
