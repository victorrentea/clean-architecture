package victor.training.clean.in.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.in.rest.dto.CustomerDto;
import victor.training.clean.application.usecase.CustomerUseCase;
import victor.training.clean.in.rest.dto.SearchCustomerCriteria;
import victor.training.clean.in.rest.dto.SearchCustomerResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerRestAdapter {
   private final CustomerUseCase customerUseCase;

   @PostMapping("customers")
   public void register(@RequestBody @Validated CustomerDto dto) {
      customerUseCase.register(dto);
   }

   @Operation(description = "Search Customer, a fost odata ca-n povesti, a fost ca niciodata , o fata mandra ca-n povesti")
   @PostMapping("customers/search")
   public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
      return customerUseCase.search(searchCriteria);
   }

   @GetMapping("customers/{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerUseCase.findById(id);
   }

   //<editor-fold desc="GET returning ResponseEntity for 404 👎">
//   @GetMapping("customers/{id}")
//   public ResponseEntity<CustomerDto> findById(@PathVariable long id) {
//      try {
//         return ResponseEntity.ok(customerApplicationService.findById(id));
//      } catch (NoSuchElementException e) {
//         // TODO return 404 from a global @ExceptionHandler
//         return ResponseEntity.notFound().build();
//      }
//   }
   //</editor-fold>

   @PutMapping("customers/{id}")
   public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
      customerUseCase.update(id, dto);
   }

   //<editor-fold desc="PUT returning ResponseEntity for 404 👎">
   //   @PutMapping("customers/{id}")
//   public ResponseEntity<Void> update(@PathVariable long id, @RequestBody CustomerDto dto) {
//      try {
//         customerApplicationService.update(id, dto);
//         return ResponseEntity.ok().build();
//      } catch (NoSuchElementException e) {
//         // TODO return 404 from a global @ExceptionHandler
//         return ResponseEntity.notFound().build();
//      }
//   }
   //</editor-fold>
}

