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

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }

   @GetMapping("two/{id}")
   public Customer findById2(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }

   @GetMapping("two/{id}")
   public Customer findById3(@PathVariable long id) { // fails the test. +1 violation
      return customerRepo.findById(id).orElseThrow();
   }

}
