package victor.training.clean.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
   }

   @Operation(description = "Customer Search")
   @PostMapping("search")
   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
      return customerApplicationService.search(searchCriteria);
   }

   @PostMapping("")
   public void register(@RequestBody@Validated CustomerDto customerDto) {
      customerApplicationService.register(customerDto);
   }
}
