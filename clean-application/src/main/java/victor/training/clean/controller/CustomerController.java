package victor.training.clean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.CustomerApplicationService;
import victor.training.clean.application.dto.CustomerDto;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
   private final CustomerApplicationService customerApplicationService;
//
//   @GetMapping("{id}")
//   public ResponseEntity<CustomerDto> findById(@PathVariable long id) {
//      try {
//         return ResponseEntity.ok(customerApplicationService.findById(id));
//      } catch (NoSuchElementException e) {
//         return ResponseEntity.status(404).build(); // bad practice. in schimb e mai
//         // elegant daca tratezi orice NoSuchElementException aparute pe oriunde
//         // intr-un @RestControllerAdvice
//      }
//   }

//   @Operation(description = "Customer Search")
//   @PostMapping("search")
//   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
//      return customerApplicationService.search(searchCriteria);
//   }

//   @PostMapping
//   public void register(@RequestBody   @Validated   CustomerDto dto) {
//      customerApplicationService.register(dto);
//   }

   @PutMapping("{id}")
   public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
      customerApplicationService.update(id, dto);
   }
}

