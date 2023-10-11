package victor.training.clean.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.application.service.CustomerApplicationService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerApplicationService customerApplicationService;

//   @PostMapping("customer")
//   public void register(@RequestBody @Validated CustomerDto dto) {
//      customerApplicationService.register(dto);
//   }

   @Operation(description = "Search Customer")
   @PostMapping("customer/search")
   public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
      return customerApplicationService.search(searchCriteria);
   }

//   @GetMapping("customer/{id}")
//   public CustomerDto findById(@PathVariable long id) {
//      return customerApplicationService.findById(id);
//   }

   //<editor-fold desc="GET returning ResponseEntity for 404 👎">
//   @GetMapping("customer/{id}")
//   public ResponseEntity<CustomerDto> findById(@PathVariable long id) {
//      try {
//         return ResponseEntity.ok(customerApplicationService.findById(id));
//      } catch (NoSuchElementException e) {
//         // TODO return 404 from a global @ExceptionHandler
//         return ResponseEntity.notFound().build();
//      }
//   }
   //</editor-fold>

   @PutMapping("customer/{id}")
   public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
      customerApplicationService.update(id, dto);
   }

   //<editor-fold desc="PUT returning ResponseEntity for 404 👎">
   //   @PutMapping("customer/{id}")
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

