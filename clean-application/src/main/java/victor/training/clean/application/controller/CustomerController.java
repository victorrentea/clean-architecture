package victor.training.clean.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.service.CustomerApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerApplicationService customerApplicationService;
  private final ObjectMapper jacksonObjectMapper;

//  @PostMapping("customers")
//  public void register(@RequestBody @Validated CustomerDto dto) {
//    customerApplicationService.register(dto);
//  }

  @Operation(description = "Search Customer")
  @PostMapping("customers/search")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
    return customerApplicationService.search(searchCriteria);
  }

  @GetMapping("customers/{id}")
  public CustomerDto findById(@PathVariable long id) {
    // ok, i can immediately see there's no logic involved- @alexandru
    Customer customer = customerRepo.findById(id).orElseThrow();
    return CustomerDto.fromEntity(customer);
  }

//    customerMapStruct.toDto(customer);
//    return CustomerMapper/Transformer/Converter.toDto(customer);
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
    customerApplicationService.update(id, dto);
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

  @PatchMapping(path = "customers/{id}", consumes = "application/json-patch+json")
  public void patch(@PathVariable long id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
    Customer oldCustomer = customerRepo.findById(id).orElseThrow();
    JsonNode oldJson = jacksonObjectMapper.convertValue(oldCustomer, JsonNode.class);
    JsonNode patchedJson = patch.apply(oldJson);
    Customer patchedCustomer = jacksonObjectMapper.treeToValue(patchedJson, Customer.class);
    customerRepo.save(patchedCustomer);
  }
  private final CustomerRepo customerRepo;

}

