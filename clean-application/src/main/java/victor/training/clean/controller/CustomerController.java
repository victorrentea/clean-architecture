package victor.training.clean.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.CustomerApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerApplicationService customerApplicationService;

   @GetMapping("{id}")
   public ResponseEntity<CustomerDto> findById(@PathVariable long id) {
      CustomerDto customer = null;
      try {
         customer = customerApplicationService.findById(id);
      } catch (NoSuchElementException e) {
         return ResponseEntity.status(404).build();
      }
      return ResponseEntity.ok(customer);
   }

   @Operation(description = "Customer Search")
   @PostMapping("search")
   public List<CustomerSearchResult> search(@Validated @RequestBody CustomerSearchCriteria searchCriteria) {
      return customerApplicationService.search(searchCriteria);
   }

   @PostMapping
   public void register(@RequestBody CustomerDto customerDto) {
      customerApplicationService.register(customerDto);
   }

   @PutMapping("{id}")
   public void update(@RequestBody CustomerDto customerDto) {
      customerApplicationService.update(customerDto);
   }
}

