package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.infra.AnafClient;

@Service
@Slf4j
@RequiredArgsConstructor
//public class CustomerDomainService {  bad name, too broad
public class RegisterCustomerService { // do you see the name of this class
  // is not a 'thing' but an 'action'. ofc. it's a stateless piece of logic.
  private final CustomerRepo customerRepo;
  private final AnafClient anafClient;

  public void register(Customer customer) {
    // business rule
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("A customer with this email is already registered!");
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
    customerRepo.save(customer);
  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

}
