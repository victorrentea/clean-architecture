package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
  private final CustomerRepo customerRepo;
  // gu-noi
//  public Customer findById(long id) {
//    return customerRepo.findById(id).orElseThrow();
//  }
}
