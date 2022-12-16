package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;

import java.util.List;

public interface CustomerRestAPI {
  @Operation(description = "Customer Search")
  @PostMapping("search")
  List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria);

  @GetMapping("{id}")
    //    @Secured
    //    @Timed
    //    @Cacheable("search")
  CustomerDto findById(@PathVariable long customerId);

  @PostMapping("")
  void register(@Validated @RequestBody CustomerDto dto);


}
