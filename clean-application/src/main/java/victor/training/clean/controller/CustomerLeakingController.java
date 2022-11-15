package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequestMapping("customerLeaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) { // option A
      // exposing hibernate entities as JSON to your clients
      // - exposing the domain data structures to our clients
      // 1) what if you decide tomorrow to refactor the model > your clients will be 🤬.
            // --> you will give up exploring better ways to model your problem
            // => Domain Model freezes. => patching around => bugs, slow
      // 2) your entities will be burdened with +1 responsibility: formatting themsvels -> JSONifyable
         // terrible pollution of the model codebase.

      // - Some values are auto generated and are only useful to our
      //       entities and hibernate, exposing them might be fatal: id, createdBy

      // If jackson has to unmarshall JSON into it, the model will become ANEMIC.

      //- Should we create separate Dto even if we have a single client = our UI
         // yes; FE grows big, BE grows.

      // - Lazy loading in serialization (accidental exposure of unneeded data)

      // your Domain Model should be (somewhat) independent of the DB schema decisions
      // - the column names might not be what the client of the API expects.
      // - Bad because it exposes database design,
      //       making API response brittle whenever we change the underlying database design
      // !! there are (many) times in DDD projects that the DB design is NOT 1:1 with the Domain Model
         // @Embeddable

      // the Domain Model (java classes) are KING. we adjust the schema to persist that model
         // an ORM can help you decouple the object model from the DB schema quite a bit.
      return customerRepo.findById(id).orElseThrow();
   }
//   public CustomerDto findById(@PathVariable long id) { // option B

   @GetMapping("{id}/2")
   public ResponseEntity<Customer> findByIdEntity(@PathVariable long id) {
      return ResponseEntity.ok(customerRepo.findById(id).orElseThrow());
   }

//   public void create(@RequestBody Customer customer) {}

}
