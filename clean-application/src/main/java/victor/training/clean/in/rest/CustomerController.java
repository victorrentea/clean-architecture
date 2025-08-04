package victor.training.clean.in.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.app.model.Country;
import victor.training.clean.app.model.Customer;
import victor.training.clean.in.rest.dto.CustomerDto;
import victor.training.clean.in.rest.dto.CustomerSearchCriteria;
import victor.training.clean.in.rest.dto.CustomerSearchResult;
import victor.training.clean.app.service.CustomerApplicationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerApplicationService customerApplicationService;

  @PostMapping("customers")
  public void register(@RequestBody @Validated CustomerDto dto) {
    Customer customer = new Customer();
    customer.setEmail(dto.email());
    customer.setName(dto.name());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(dto.countryId()));
    customer.setLegalEntityCode(dto.legalEntityCode());
    customerApplicationService.register(customer);
  }

  @Operation(description = "Search Customer")
  @PostMapping("customers/search")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
    return customerApplicationService.search(searchCriteria);
  }

  @GetMapping("customers/{id}")
  public CustomerDto findById(@PathVariable long id) {
    Customer customer = customerApplicationService.findById(id);
    // boilerplate mapping code TODO move somewhere else
    return CustomerDto.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .status(customer.getStatus())
        .createdDate(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())

        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressZip(customer.getShippingAddress().zip())

        .canReturnOrders(customer.canReturnOrders())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode().orElse(null))
        .discountedVat(customer.isDiscountedVat())
        .build();
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
    CustomerApplicationService.UpdateCustomerCommand command = new CustomerApplicationService.UpdateCustomerCommand(
        id,
        dto.name(),
        dto.email(),
        dto.countryId(),
        dto.gold(),
        dto.goldMemberRemovalReason(
        ));
    customerApplicationService.update(command);
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

