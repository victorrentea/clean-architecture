package victor.training.clean.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

   // JSP, JSF, Thymeleaf, FreeMarker, Velocity, Groovy, Mustache, Pebble, Handlebars, etc. VAADIN
//   @PostMapping("customers")
//   public void register(@RequestBody @Validated CustomerDto dto) {
//      customerApplicationService.register(dto);
//   }

//   @Secured("ROLE_ADMIN")
//   @Operation(description = "Search Customer")
//   @PostMapping("customers/search")
//   public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
//      // Reasons to keep a dedicated controller:
//      //1) mapping to Entity
//      //2) compose a response from various modules = orchestration !! to read or write
//      //3) @Secured("SEARCH_CUSTOMER") on service which is also called from a MessageListener @RunAs("ROLE_SEARCH_CUSTOMER")
//      //4) if the same feature is used both via REST and MQ
//      return customerApplicationService.search(searchCriteria);
//   }

   @GetMapping("customers/{id}")
   public CustomerDto findById(@PathVariable long id) {
      return customerApplicationService.findById(id);
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
//   public void update(@PathVariable long id, @RequestBody UpdateCustomerRequest dto) {
   public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
      customerApplicationService.update(id, dto);
   }

//   @PutMapping("customers/{id}/details")
//   public void update(@PathVariable long id, @RequestBody UpdateCustomerDetailsRequest dto) {
//   @PutMapping("customers/{id}/gold/enable")
//   public void setGold(@PathVariable long id) {
//   @PutMapping("customers/{id}/gold/disable")
//   public void setNotGold(@PathVariable long id, @RequestBody String reason) {
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

