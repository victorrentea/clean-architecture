package victor.training.clean.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.CustomerApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;

import java.util.List;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerApplicationService customerApplicationService;

   @GetMapping("{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerApplicationService.findById(id);
      // it is a bad practice to see catch () { ResponseEntity.
   }

//   @Operation(description = "Customer Search")
//   @PostMapping("search")
//   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
//      return customerApplicationService.search(searchCriteria);
//   }

   @PostMapping("")
   public void register(@RequestBody CustomerDto customerDto) {
      customerApplicationService.register(customerDto);
   }
}
