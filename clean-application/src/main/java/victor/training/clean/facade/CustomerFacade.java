package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.entity.Email;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();

      // TODO move mapping logic somewhere else
      CustomerDto dto = new CustomerDto();
      dto.id = customer.getId();
      dto.name = customer.getName();
      dto.email = customer.getEmail();
      dto.creationDateStr = customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      dto.siteId = customer.getSite().getId();
      return dto;
   }

   public void register(CustomerDto dto) {
      // mapping - keep DTOs out!
      Customer customer = new Customer();
      customer.setEmail(dto.email);
      customer.setName(dto.name);
      customer.setSite(siteRepo.getById(dto.siteId));

      // TODO experiment all the ways to do validation
      if (customer.getName().length() < 5) {
         throw new IllegalArgumentException("Name too short");
      }
      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Customer email is already registered");
//         throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
      }

      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // TODO Where can I move this little logic? (... operating on the state of a single entity)
      int discountPercentage = 3;
      if (customer.isGoldMember()) {
         discountPercentage += 1;
      }
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic
      quotationService.quoteCustomer(customer);

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
