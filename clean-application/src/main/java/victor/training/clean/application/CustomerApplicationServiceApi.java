package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;

import java.util.List;

public interface CustomerApplicationServiceApi {
//  @Operation(description = "Search Customer poetry" +
//                           "asdasdas" +
//                           "das" +
//                           "dasd" +
//                           "asd")
//  @PostMapping("customer/search")
//  List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria);

  @GetMapping("customer/{id}")
  CustomerDto findById(@PathVariable long id);

  @Transactional
  @PostMapping("customer")
  void register(@RequestBody @Validated CustomerDto dto);

  @Transactional
  @PutMapping("customer/{id}")
  void update(@PathVariable long id, @RequestBody CustomerDto dto);
}
