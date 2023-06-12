//package victor.training.clean.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import victor.training.clean.application.CustomerApplicationService;
//import victor.training.clean.application.dto.CustomerDto;
//import victor.training.clean.application.dto.SearchCustomerCriteria;
//import victor.training.clean.application.dto.SearchCustomerResponse;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@RestController
//@RequiredArgsConstructor
//public class CustomerResource {
//   private final CustomerApplicationService customerApplicationService;
//
//   @GetMapping("customer/{id}")
//   public CustomerDto findById(@PathVariable long id) {
//      try {
////         return ResponseEntity.ok(customerApplicationService.findById(id));
////      } catch (NoSuchElementException e) {
////         // TODO return 404 from a global exception handler
////         return ResponseEntity.notFound().build();
////      }
//      return customerApplicationService.findById(id);
//   }
//
//
////   @Operation(description = "Search Customer")
////   @PostMapping("customer/search")
////   public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
////      return customerApplicationService.search(searchCriteria);
////   }
////
////   @PostMapping("customer")
////   public void register(@RequestBody @Validated CustomerDto dto) {
////      customerApplicationService.register(dto);
////   }
//
////   @PutMapping("customer/{id}")
////   public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
////          ResponseEntity.ok().build();
////   }
//}
//
