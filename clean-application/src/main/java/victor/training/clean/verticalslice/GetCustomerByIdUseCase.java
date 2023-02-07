package victor.training.clean.verticalslice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.application.dto.CustomerView;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
//@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @GetMapping("customer/{id}")
  public CustomerView findById(@PathVariable long id) {
    return new CustomerView(customerRepo.findById(id).orElseThrow());
  }
}
