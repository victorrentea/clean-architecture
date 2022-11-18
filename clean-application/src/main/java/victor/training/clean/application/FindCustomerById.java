package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.repo.CustomerRepo;

@UseCase
@RequiredArgsConstructor
public class FindCustomerById {
  private final CustomerRepo customerRepo;

  //-  stupid too small class = overengineering
  @GetMapping("{id}")
  public CustomerDto findById(@PathVariable long customerId) {
    return new CustomerDto(customerRepo.findById(customerId).orElseThrow());
  }
}
