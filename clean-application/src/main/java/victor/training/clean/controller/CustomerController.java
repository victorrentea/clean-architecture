package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.facade.CustomerFacade;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;

import java.util.List;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerFacade customerFacade;

   @GetMapping("{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerFacade.findById(id);
   }
   @GetMapping("{id}/bad")
   public Customer findByIdBad(@PathVariable long id) {
      return customerFacade.findByIdBad(id);
   }
//@PreAtho
   @PostMapping("search")
   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
      return customerFacade.search(searchCriteria);
   }

   @PostMapping
   //   @PreAuthorized("hasRole('CRM')")
   public void register(@RequestBody CustomerDto customerDto) {
      customerFacade.register(customerDto);
   }
}
