package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.user.entity.Email;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.customer.repo.SiteRepo;
import victor.training.clean.customer.service.RegisterCustomerService;
import victor.training.clean.insurance.service.QuotationService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.repo.CustomerSearchRepo;

import java.util.List;

//@Service
@Facade
@Transactional
@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerRepo customerRepo;
   private final EmailSender emailSender;
   private final SiteRepo siteRepo;
   private final CustomerSearchRepo customerSearchRepo;
   private final QuotationService quotationService;
   private final CustomerMapper customerMapper;
   private final RegisterCustomerService registerCustomerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      return new CustomerDto(customer);
   }

@Transactional
   public void register(CustomerDto dto) {
      Customer customer = new Customer();
      customer.setEmail(dto.email);
      customer.setName(dto.name);
      customer.setSite(siteRepo.getOne(dto.siteId));

      // TODO after lunch
//      if (customer.getName().length() < 5) {
//         throw new IllegalArgumentException("Name too short");
//      }
      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Customer email is already registered");
//         throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
      }

      registerCustomerService.register(customer);



      sendRegistrationEmail(customer.getEmail());
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
