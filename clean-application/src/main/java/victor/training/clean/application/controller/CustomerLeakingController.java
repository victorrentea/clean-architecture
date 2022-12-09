package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customerLeaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }

   @GetMapping("{id}/2")
   public ResponseEntity<Customer> findByIdEntity(@PathVariable long id) {
      return ResponseEntity.ok(customerRepo.findById(id).orElseThrow());
   }

}
