package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.customer.AnafResult;
import victor.training.clean.domain.model.customer.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Slf4j // Lombok, dovada ca Java e naspa, motivul pt care java inca e tolerat
@RequiredArgsConstructor// Lombok, dovada ca Java e naspa '95, motivul pt care java inca e tolerat
@Service // Spring non-invasive Dep Injection OK
//public class CustomerRegistrationService { // verb/action not noun (OOP)
public class RegisterCustomerService { // verb/action not noun (OOP)
  private final CustomerRepo customerRepo;
  private final AnafClientInterface anafClient;

  public void register(Customer customer) {
    // business rule
//    search(criteria.setName(customer.getName()))
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("A customer with this email is already registered!");
      // throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

//    CustomerDto
    // enrichment from external API in 'application' layer
    if (customer.getLegalEntityCode() != null) {
      if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode())) {
        throw new IllegalArgumentException("Company already registered");
      }
      AnafResult anafResult = anafClient.query(customer.getLegalEntityCode());
      if (anafResult == null || !normalize(customer.getName()).equals(normalize(anafResult.getName()))){
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
