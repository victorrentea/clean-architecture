package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.insurance.domain.service.QuotationService;
import victor.training.clean.user.domain.model.Email;
import victor.training.clean.customer.domain.service.RegisterCustomerService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.customer.domain.repo.SiteRepo;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerRepo customerRepo;
   private final EmailSender emailSender;
   private final SiteRepo siteRepo;
   private final CustomerSearchRepo customerSearchRepo;
   private final RegisterCustomerService registerCustomerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      return new CustomerDto(customer);
   }

   public void register(CustomerDto dto) {
      Customer customer = toEntity(dto);

      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Customer email is already registered");
//         throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
      }

      registerCustomerService.registerCustomer(customer);
      quotationService.quoteCustomer(customer);

      sendRegistrationEmail(customer.getEmail());
      System.out.println("gata");
   }

   private final QuotationService quotationService;


   public void updateCustomer(CustomerDto customerDto35campuri) {
      Customer customer = customerRepo.findById(customerDto35campuri.id).orElseThrow();

   }

@Transactional
   public void updateEmail(long customerId, String newEmail) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      customer.setEmail(newEmail);
      customerRepo.save(customer);
//      customerRepo.save(); // nu face inca INSERT
//      customerRepo.saveAndFlush() // face mereu insert
//      sendEmail()
   }

   private Customer toEntity(CustomerDto dto) {
      Customer customer = new Customer(dto.name);
      customer.setEmail(dto.email);
      customer.setSite(siteRepo.getById(dto.siteId));
      return customer;
   }

   private void sendRegistrationEmail(String emailAddress) {
      System.out.println("Sending activation link via email to " + emailAddress);
      Email email = new Email();
      email.setFrom("noreply");
      email.setTo(emailAddress);
      email.setSubject("Welcome");
      email.setBody("You'll like it! Sincerely, Team");
      emailSender.sendEmail(email);
   }
}
