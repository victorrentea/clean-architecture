package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.mapper.CustomerMapStruct;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

//@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

//   @GetMapping("{id}")
//   //NEVER
//   public Customer findById(@PathVariable long id) {
//      return customerRepo.findById(id).orElseThrow();
//   }

   // expose carefully HAND crafted Dtos (written in .java or .yaml) to the outside world
   // so that your API is stable and you can refactor the internals
   public CustomerDto findById(@PathVariable long id) {
//    initially you can use MapStruct to map between Entity and Dto or vice-versa
      // but after 1-2 years PLEASE if the mapping  gets tricky
      // , give up on MapStruct and write the mapping code by hand
      // mapStruct just copies the data
      return mapStruct.toDto(customerRepo.findById(id).orElseThrow());
   }
   private final CustomerMapStruct mapStruct;
}
