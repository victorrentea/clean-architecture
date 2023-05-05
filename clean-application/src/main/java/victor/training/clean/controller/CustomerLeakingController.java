package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customerLeaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;


// in nici un caz nu returnezi o @Entity in afara!
   // ci un DTO
   // - cu strict ce are nevoie clientul - ISP
   // - periculos sa faca lazy loading -> QUERYURI IN PLUS
   // -

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }

}
