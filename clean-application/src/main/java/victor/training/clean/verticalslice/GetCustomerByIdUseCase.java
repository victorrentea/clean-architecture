package victor.training.clean.verticalslice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;

interface MyRepo extends Repository<Customer, Long> {
    Customer findById(Long id);
  }
@RequiredArgsConstructor
@RestController
public class GetCustomerByIdUseCase {
  private final MyRepo myRepo;

  @GetMapping("customer/{id}")
  public CustomerDto findById(@PathVariable long id) {
    return new CustomerDto(myRepo.findById(id));
  }
}
