package victor.training.clean.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.customer.model.Customer;
import victor.training.clean.customer.repo.CustomerRepo;

@RestController
@RequestMapping("customerBAD")
@RequiredArgsConstructor
public class CustomerControllerBad {
   private final CustomerRepo customerRepo;

//   @GetMapping("{id}")
//   public Customer findById(@PathVariable long id) {
//      return customerRepo.findById(id).orElseThrow();
//   }
   @GetMapping("{id}/2")
   public ResponseEntity<Customer> findByIdEntity(@PathVariable long id) {
      return ResponseEntity.ok(customerRepo.findById(id).orElseThrow());
   }

}
