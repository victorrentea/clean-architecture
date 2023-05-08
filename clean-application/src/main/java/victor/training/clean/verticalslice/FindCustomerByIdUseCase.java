package victor.training.clean.verticalslice;

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
public class FindCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @Value
  @Builder
  public static class FindCustomerByIdUseCaseResponse {
    Long id;
    String name;
    String email;
    Long siteId;
    String creationDateStr;
  }

  @GetMapping("customer/{id}")
  public FindCustomerByIdUseCaseResponse findById(@PathVariable long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    return FindCustomerByIdUseCaseResponse.builder()
            .id(customer.getId())
            .name(customer.getName())
            .email(customer.getEmail())
            .siteId(customer.getSite().getId())
            .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .build();
  }
} // PROBLEMA: clasa asta poate creste mare
