package victor.training.clean.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.core.model.Customer;
import victor.training.clean.core.repo.CustomerRepo;

@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
  private final CustomerRepo customerRepo;

  @GetMapping("{id}")
  public Customer findById(@PathVariable long id) {
    return customerRepo.findById(id).orElseThrow();
  }

}
