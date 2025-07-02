package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   @GetMapping("{id}")
//   public CustomerDto findById(@PathVariable long id) {
   public CustomerDto findById(@PathVariable long id) {
      var customer = customerRepo.findById(id).orElseThrow();
//      return CustomerDto.frobuilder()
//         .id(customer.getId())
//         .name(customer.getName())
//         .email(customer.getEmail())
//         .countryId(customer.getCountry().getId())
//         .status(customer.getStatus())
//         .createdDate(customer.getCreatedDate())
//         .gold(customer.isGoldMember())
//         .shippingAddressStreet(customer.getShippingAddressStreet())
//         .shippingAddressCity(customer.getShippingAddressCity())
//         .shippingAddressZipCode(customer.getShippingAddressZipCode())
//         .build();
      return null;
   }

   // We should NOT expose Entity in REST APIs->return DTOs
   // PRO: expose only what users need (privacy)
   // PRO: I may change the internal model w/o breaking my public contract
   // CON: overly complex for a CRUD app (eg List management system): +1 class, mappers..
}
