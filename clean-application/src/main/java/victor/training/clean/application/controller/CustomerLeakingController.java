package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   @GetMapping("{id}") // @GET
   public Customer findById(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }

}
