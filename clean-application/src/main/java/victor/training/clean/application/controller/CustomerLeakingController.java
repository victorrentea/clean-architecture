//package victor.training.clean.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import victor.training.clean.application.dto.CustomerDto;
//import victor.training.clean.domain.model.customer.Customer;
//import victor.training.clean.domain.repo.CustomerRepo;
//
////@RestController
//@RequestMapping("customer-leaking")
//@RequiredArgsConstructor
//public class CustomerLeakingController {
//   private final CustomerRepo customerRepo;
//
//   @GetMapping("{id}")
//   public CustomerDto findById(@PathVariable long id) {
////      JSONObject jsonObject = new JSONObject();
////      jsonObject.put("a", "") // java old
//
//      // Laravel copiaza automat campurile pe care i le zici. php
//      Customer customer = customerRepo.findById(id).orElseThrow();
////      return new CustomerDto(a,v,x,x,s,a)
//      return new CustomerDto(customer);
////      return customer.toResponse(); // niciodata
//   }
//
//}
