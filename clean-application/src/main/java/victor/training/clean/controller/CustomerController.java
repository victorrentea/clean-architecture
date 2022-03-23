package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.facade.CustomerFacade;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;

import java.util.List;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerFacade customerFacade; // "Facade" ~= "Application Service"

   @GetMapping("{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerFacade.findById(id);
   }

   @PostMapping("search")
   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
      return customerFacade.search(searchCriteria);
   }

   @PostMapping("")
   public void register(@RequestBody CustomerDto customerDto) {
      customerFacade.register(customerDto);
   }
}
