package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.customer.application.CustomerApplicationService;
import victor.training.clean.application.CustomerMapper;
import victor.training.clean.application.dto.CustomerDto;

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
      customerApplicationService.register(customerMapper.mapToEntity(customerDto));
   }
   private final CustomerMapper customerMapper;
}
