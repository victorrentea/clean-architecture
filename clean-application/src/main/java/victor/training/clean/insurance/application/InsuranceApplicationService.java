package victor.training.clean.insurance.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.insurance.domain.service.QuotationService;

@ApplicationService
@Slf4j
@RestController
@RequiredArgsConstructor
public class InsuranceApplicationService { // using different arch in this module. scary, right ?!
  private final QuotationService quotationService;
  @PostMapping("policy")
  public void method() {
    quotationService.createPolicy("dummy");
  }
}
