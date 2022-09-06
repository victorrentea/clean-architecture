package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.entity.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.util.List;

//@Service
@ApplicationService
@Transactional
@RequiredArgsConstructor
public class CustomerApplicationService {
   private final CustomerRepo customerRepo;
   private final EmailSender emailSender;
   private final SiteRepo siteRepo;
   private final CustomerSearchRepo customerSearchRepo;
   private final QuotationService quotationService;
   private final RegisterCustomerService registerCustomerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria); // relaxed layer arch:
      // avoid writing a silly brainless method in a service just for this usecase
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      return new CustomerDto(customer);
   }

   public void register(CustomerDto dto) {
      Customer customer = fromDto(dto); //todo into Mapper

      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Customer email is already registered");
      }
      registerCustomerService.registerCustomer(customer);
      quotationService.quoteCustomer(customer);
      sendRegistrationEmail(customer.getEmail());
   }

   private Customer fromDto(CustomerDto dto) {
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
