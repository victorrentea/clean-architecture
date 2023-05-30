package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.client.LegalEntityProvider;
import victor.training.clean.domain.model.LegalEntity;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCustomerService { //
  private final LegalEntityProvider legalEntityProvider;
  private final CustomerRepo customerRepo;

  public void register(Customer customer) {
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
  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }


}
