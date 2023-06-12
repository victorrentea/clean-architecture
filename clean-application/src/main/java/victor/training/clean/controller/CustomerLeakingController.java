package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      return customerRepo.findById(id).orElseThrow();
   }
   // gresit pt ca
   // 1. poti expune prea mutle date. (o sa uiti sa pui @JsonIgnore)
   // 2. Customer->* Contract -> Lazy Loading d
   // 3. Modelul tau de entity face freeze ca sa nu-ti strici clientii !! 1MLN code client ce folosea Customer expus de ei

}
