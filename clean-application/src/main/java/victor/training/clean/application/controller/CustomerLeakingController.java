package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController // breaking my clients whenever I
// change my internal domain model structure, causing breaking changes in my clients
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }

}
