//package victor.training.clean.application.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import victor.training.clean.application.dto.CustomerDto;
//import victor.training.clean.domain.model.Customer;
//import victor.training.clean.domain.repo.CustomerRepo;
//
////@RestController
//@RequestMapping("customer-leaking")
//@RequiredArgsConstructor
//public class CustomerLeakingController {
//   private final CustomerRepo customerRepo;
//
//
////   @GetMapping("{id}")
//////   public Customer findById(@PathVariable long id) {
//////      return customerRepo.findById(id).orElseThrow();
//////   }
//////   public CustomerDto findById(@PathVariable long id) {
////   public GetCustomerResponse findById(@PathVariable long id) {
////      return customerRepo.findById(id).orElseThrow();
////   }
////
////}
////
////
////class GetCustomerResponse {
////   String name; // *
////   String email; // *
////   Long countryId; // *
////   String legalEntityCode; // *
////}