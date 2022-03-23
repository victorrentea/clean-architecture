package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

//   @PreAuthorized("hasRole('ADMIN'")
   @GetMapping("{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerFacade.findById(id);
   }
   //the controller should exist if :   /*MultipartFile */ /*HttpServletRequest */
   // or if we expose data over 2 channels RMI, REST or GraphQL and WSDL

   @PostMapping("search")
   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
      return customerFacade.search(searchCriteria);
   }

   @PostMapping("")
   public void register( @Validated @RequestBody CustomerDto customerDto) {
      customerFacade.register(customerDto);
   }
}
