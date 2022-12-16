package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepo customerRepo;

  public Optional<Customer> findById(long customerId) {
    return customerRepo.findById(customerId); // code smell : Middle Man
  }
}
