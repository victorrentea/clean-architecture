package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.FiscalDetailsProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCustomerService {
  private final CustomerRepo customerRepo;
  private final FiscalDetailsProvider anafClient;

   public void register(Customer customer) {
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("A customer with this email is already registered!");
      // throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

    // enrich data from external API
    if (customer.getLegalEntityCode() != null) {
      if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode())) {
        throw new IllegalArgumentException("Company already registered");
      }
      AnafResult anafResult = anafClient.query(customer.getLegalEntityCode());
      if (anafResult == null || !normalize(customer.getName()).equals(normalize(anafResult.getName()))) {
        throw new IllegalArgumentException("Legal Entity not found!");
      }
      if (anafResult.isVatPayer()) {
        customer.setDiscountedVat(true);
      }
    }
    log.info("More Business Logic (imagine)");
    log.info("More Business Logic (imagine)");
    customerRepo.save(customer); // INSERT not here in DB but AFTER the end of the method
  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

}
