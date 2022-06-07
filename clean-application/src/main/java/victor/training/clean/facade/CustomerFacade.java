package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.entity.Email;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.domain.service.QuotationService;

import java.util.List;

//@Service
@Facade
@Transactional // CAREFUL in a high RPS system.!! DO NOT CALL REST APIs in @Transacted method
@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerRepo customerRepo;
   private final EmailSender emailSender;
   private final CustomerSearchRepo customerSearchRepo;
   private final CustomerMapper customerMapper;
   private final RegisterCustomerService registerCustomerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      // where can I move this mapping logic to ?
//      return customer.toDto(); // NEVER DO THAT: you are polluting your DOMAIN with shitty API models!!
      return new CustomerDto(customer);
   }

   public void register(CustomerDto dto) {
      // **** DATA MAPPING
      // mapping - keep DTOs out!
//      mapper.fromDto()
//      new Customer(dto); NEVER
//      Customer customer = dto.asEntity(); // impossible here as I need access to @Autowired SiteRepo
      Customer customer = customerMapper.fromDto(dto);

      // **** DATA VALIDATION
      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Customer email is already registered");
//         throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
      }

      // ***** BIZ LOGIC

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
