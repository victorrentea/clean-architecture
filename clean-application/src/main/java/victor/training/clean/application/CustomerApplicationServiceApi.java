package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;

import java.util.List;

public interface CustomerApplicationServiceApi {
  @Operation(description = "Search Customer poetry" +
                           "asdasdas" +
                           "das" +
                           "dasd" +
                           "asd")
  @PostMapping("customer/search")
  List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria);

  @Transactional
  @PostMapping("customer")
  void register(@RequestBody @Validated CustomerDto dto);
}
