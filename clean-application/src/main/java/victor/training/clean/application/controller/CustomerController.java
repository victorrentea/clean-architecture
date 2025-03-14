package victor.training.clean.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.service.CustomerFacade;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerFacade customerFacade;

  @PostMapping("customers")
  public void register(@RequestBody @Validated CustomerDto dto) {
    customerFacade.register(dto);
  }

  @Operation(description = "Search Customer")
  @PostMapping("customers/search")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
    return customerFacade.search(searchCriteria);
  }

//  @GetMapping("customers/{id}")
//  public CustomerDto findById(@PathVariable long id) {
//    return customerFacade.findById(id);
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
  public void update(@PathVariable long id, @RequestBody  @Validated CustomerDto dto) {
    customerFacade.update(id, dto);
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
    customerFacade.patchUpdate(id, patch);
  }
}

