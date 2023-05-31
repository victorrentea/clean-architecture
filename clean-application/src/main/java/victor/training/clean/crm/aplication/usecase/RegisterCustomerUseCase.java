package victor.training.clean.crm.aplication.usecase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.crm.api.event.CustomerRegisteredEvent;
import victor.training.clean.crm.domain.service.LegalEntityProvider;
import victor.training.clean.common.country.Country;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.model.LegalEntity;
import victor.training.clean.crm.domain.repo.CustomerRepo;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterCustomerUseCase {
  private final LegalEntityProvider legalEntityProvider;
  private final CustomerRepo customerRepo;

  @Builder
  @Value
  @AllArgsConstructor
  public static class RegisterCustomerRequest { // Dto used to both QUERY and COMMAND use-cases ?
    @Size(min = 5) // yes OK earlier.
    String name; // *
    @Email
    String email; // *
    Long countryId; // *
    String legalEntityCode; // *

    public Customer asEntity() {
      Customer customer = new Customer();
      customer.setEmail(email);
      customer.setName(name);
      customer.setCreationDate(LocalDate.now());
      customer.setCountryId(countryId);
      customer.setLegalEntityCode(legalEntityCode);
      return customer;
    }
  }

  @Transactional
  @PostMapping("customer")
  public void register(@RequestBody @Validated RegisterCustomerRequest dto) {
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
    eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId(), customer.getEmail(), customer.getEmail()));
//    notificationService.sendWelcomeEmail(customer);
  }
  private final ApplicationEventPublisher eventPublisher;

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

}
