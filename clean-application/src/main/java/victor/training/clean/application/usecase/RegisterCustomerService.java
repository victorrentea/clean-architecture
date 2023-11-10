package victor.training.clean.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.application.entity.AnafResult;
import victor.training.clean.application.entity.Customer;
import victor.training.clean.application.repo.CustomerRepo;
import victor.training.clean.application.port.LegalEntityPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterCustomerService {
  // verb nu substantiv, pt ca e mai precis numele => clasa va ramane mai mica
  private final CustomerRepo customerRepo;
  private final LegalEntityPort legalEntityProvider;

  public void register(Customer customer) {
    // business rule
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("A customer with this email is already registered!");
      // throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

    // enrich data from external API
    if (customer.getLegalEntityCode() != null) {
      if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode())) {
        throw new IllegalArgumentException("Company already registered");
      }
      AnafResult anafResult = legalEntityProvider.query(customer.getLegalEntityCode());
      if (anafResult == null || !normalize(customer.getName()).equals(normalize(anafResult.getName()))) {
        throw new IllegalArgumentException("Legal Entity not found!");
      }
      if (anafResult.isVatPayer()) {
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
