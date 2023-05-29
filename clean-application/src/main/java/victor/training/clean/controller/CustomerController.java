package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.CustomerApplicationService;
import victor.training.clean.application.dto.CustomerDto;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerApplicationService customerApplicationService;

   @GetMapping("customer/{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerApplicationService.findById(id);
   }

//   @Operation(description = "Search Customer")
//   @PostMapping("customer/search")
//   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
//      return customerApplicationService.search(searchCriteria);
//   }

   // do you have controllers that look like this 1 line of code
//   @PostMapping("customer")
//   public void register(@RequestBody @Validated CustomerDto dto) {
//      customerApplicationService.register(dto); // code smell Middle Man
//   }

   @PutMapping("customer/{id}")
   public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
      customerApplicationService.update(id, dto);
   }
}

