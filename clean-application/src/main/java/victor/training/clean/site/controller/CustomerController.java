package victor.training.clean.site.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.site.application.CustomerApplicationService;
import victor.training.clean.site.application.dto.CustomerDto;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerApplicationService customerApplicationService;

   @GetMapping("{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerApplicationService.findById(id);
   }

//   @Operation(description = "Customer Search")
//   @PostMapping("search")
//   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
//      return customerApplicationService.search(searchCriteria);
//   }

   @PostMapping("")
   public void register(@RequestBody CustomerDto customerDto) {
//      customerDto.setName("diff"); // terror: changing dto on the way.
      // to avoid: java 17 records, private final all fields.
      customerApplicationService.register(customerDto);
   }
}
