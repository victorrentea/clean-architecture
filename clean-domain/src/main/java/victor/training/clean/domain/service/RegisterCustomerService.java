package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@RequiredArgsConstructor
@Service
@Slf4j
// precise class names to host complex use-cases prevent them from exploding (GOD CLASS)
// Avoid Project Service
public class RegisterCustomerService {
  private final CustomerRepo customerRepo;
  private final IAnafClient anafClient;

  public void register(Customer customer) {
    if (customerRepo.existsByEmail(customer.getEmail())) { // business rule/validation
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
    // Sending email INSIDE the DS?
    // PRO: anyone calling this .register() will send email
    // CONS: more coupling: I need to inject a NotificationService in A FIELD
//    notificationService.sendWelcomeEmail(customer, "1"); // userId from JWT token via SecuritContext

  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

}
