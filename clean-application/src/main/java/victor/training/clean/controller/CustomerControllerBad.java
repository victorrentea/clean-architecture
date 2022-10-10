package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;

import java.util.List;

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
