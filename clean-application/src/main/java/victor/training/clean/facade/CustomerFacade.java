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
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

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
      return customerSearchRepo.search(searchCriteria); // Relaxed Layered Arch
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      // where can I move this mapping logic to ?
      CustomerDto dto = new CustomerDto(customer);
      // can go to...
      // 1 a mapper
      // 2 a generated mapper
      // 3 the DTO constructor if COde-First API (if changeable)
      // NO NO NO in the ENTITY NEVER
      // ? extension fucntions (Scala/Kotlin)

      return dto;
   }

   private final CustomerMapper customerMapper;

   public void register(CustomerDto dto) {
      // mapping - keep DTOs out!
      Customer customer = customerMapper.toEntity(dto);

      // validation
      if (customer.getName().length() < 5) {
         throw new IllegalArgumentException("Name too short");
      }
      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Customer email is already registered");
//         throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
      }

      registerCustomerService.register(customer);

      sendRegistrationEmail(customer.getEmail());
   }
   private final RegisterCustomerService registerCustomerService;


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
