package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.domain.client.LegalEntityProvider;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.LegalEntity;
import victor.training.clean.domain.repo.CustomerRepo;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterCustomerUseCase {
  private final NotificationService notificationService;
  private final LegalEntityProvider legalEntityProvider;
  private final CustomerRepo customerRepo;

  @Transactional
  @PostMapping("customer")
  public void register(@RequestBody @Validated CustomerDto dto) {
    Customer customer = dto.asEntity();
    // business rule
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("A customer with this email is already registered!");
      // throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

    // enrichment from external API in 'application' layer
    if (customer.getLegalEntityCode() != null) {
      if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode())) {
        throw new IllegalArgumentException("Company already registered");
      }
      LegalEntity legalEntity = legalEntityProvider.query(customer.getLegalEntityCode());
      if (legalEntity == null || !normalize(customer.getName()).equals(normalize(legalEntity.getName()))){
        throw new IllegalArgumentException("Legal Entity not found!");
      }
      if (legalEntity.isVatPayer()) {
        customer.setDiscountedVat(true);
      }
    }
    log.info("More Business Logic (imagine)");
    log.info("More Business Logic (imagine)");
    customerRepo.save(customer);
    notificationService.sendWelcomeEmail(customer);
  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

}
