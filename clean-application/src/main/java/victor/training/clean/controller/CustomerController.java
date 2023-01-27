package victor.training.clean.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.CustomerApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerRegistrationRequest;
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
   public CustomerDto findById(@PathVariable long id) {
      return customerApplicationService.findById(id);
   }

   @Operation(description = "Customer Search")
   @PostMapping("search")
   public List<CustomerSearchResult> search(@Validated @RequestBody CustomerSearchCriteria searchCriteria) {
      return customerApplicationService.search(searchCriteria); // code smell din carte Refactoring 2nd 2018 dec M Fowler
      // Middle Man = cod degeaba
   }

   @PostMapping
   public void register(@RequestBody CustomerRegistrationRequest customerDto) {
      customerApplicationService.register(customerDto);
   }

   @PutMapping("{id}")
   public void update(@RequestBody CustomerDto customerDto) {
      customerApplicationService.update(customerDto);
   }
}

