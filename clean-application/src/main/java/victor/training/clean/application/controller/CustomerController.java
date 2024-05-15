package victor.training.clean.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.controller.api.CustomerControllerApi;
import victor.training.clean.application.controller.api.dto.CustomerDto;
import victor.training.clean.application.controller.api.dto.CustomerSearchCriteria;
import victor.training.clean.application.controller.api.dto.CustomerSearchResult;
import victor.training.clean.application.service.CustomerApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerApi /*implements InterfataOpenAPI*/ {
  private final CustomerApplicationService customerApplicationService;
  private final ObjectMapper jacksonObjectMapper;

  @Override
  public void register(CustomerDto dto) {
    customerApplicationService.register(dto); // Middle Man code smell


//    customerApplicationService.register(mapper.toEntity(dto, "alte")); // incepe sa merite controllerul.
//     DaR NU TE OPRI DOAR LA MAPPING. pune si mici bucati de logica simpla high-level
    // daca faci doar mapare vei vedea ca iti vei inventa noi structuri de date degeaba
  }

  @Operation(description = "Search Customer")
  @PostMapping("customers/search")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
    return customerApplicationService.search(searchCriteria);
  }

//  @GetMapping("customers/{id}")
//  public CustomerDto findById(@PathVariable long id) {
//    return customerApplicationService.findById(id);
//  }

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

