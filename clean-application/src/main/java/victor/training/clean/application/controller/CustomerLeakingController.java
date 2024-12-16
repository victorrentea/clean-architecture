package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   // - Data Transfer Object = used in communication with the outside world
   // - Domain Object = used in core logic

   // in Atanu's project
   // - Business Object = used in core logic
   // - Domain Object = object mapped to/from DB tables

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      // TODO return a CustomerDto instead!!!
      // WRONG because tomorrow
      return customerRepo.findById(id).orElseThrow();
   }

}
