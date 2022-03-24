package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.CleanException;
import victor.training.clean.CleanException.ErrorCode;
import victor.training.clean.common.Facade;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.user.entity.Email;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.insurance.service.QuotationService;
import victor.training.clean.customer.service.RegisterCustomerService;

import java.util.List;


//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class SearchUseCase {
//   private final CustomerSearchRepo customerSearchRepo;
//
//   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
//      return customerSearchRepo.search(searchCriteria);
//   }
//}
//


//@Scope("singleton") // default

//@Scope("session") // browser session > NOT REST.
//@Scope("request") // current http request > replace with SecurityContextHolder.
//@Scope("prototype") // every time you ask, spring gives you a new instance./
//@Service
@Facade
@Transactional
@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerRepo customerRepo;
   private final EmailSender emailSender;
   private final CustomerSearchRepo customerSearchRepo;
   private final QuotationService quotationService;
   private final CustomerMapper customerMapper;
   private final RegisterCustomerService customerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).orElseThrow();
      // Ways to convert data:
      // - on the spot
      // - in a "Mapper"
      // - in the Dto ctor - OK to couple insignificant API/presentation model to the CORE MODEL
      // - MapStruct (the first year of love)
      // - NEVER: dto = customer.toDto(); // NOT OK, as it couples CRITICAL
      // CORE ENTITIES to PRESENTATION/API (MVC principle violation)
      return new CustomerDto(customer);
   }

   public void register(CustomerDto dto) {
      Customer customer = customerMapper.toEntity(dto);

      if (customerRepo.existsByEmail(customer.getEmail())) {
//         throw new IllegalArgumentException("Customer email is already registered");
         throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
      }

      customerService.register(customer);



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
