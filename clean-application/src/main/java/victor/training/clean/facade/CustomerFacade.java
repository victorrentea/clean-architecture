package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.entity.Email;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.customer.repo.SiteRepo;
import victor.training.clean.customer.service.RegisterCustomerService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomerFacade {
   private final EmailSender emailSender;
   private final SiteRepo siteRepo;
   private final CustomerRepo customerRepo;
   private final CustomerSearchRepo customerSearchRepo;
   private final RegisterCustomerService registerCustomerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

   public CustomerDto findById(long customerId) {
      // lock? authro
      return new CustomerDto(customerRepo.findById(customerId).get());
   }

//   @Autowired
//   private Validator validator;


   public void register(CustomerDto dto) {
      Customer customer = dto.asEntity();
//      validator.validate(customer);

//      if (customer.getName().length() < 5) {
//         throw new IllegalArgumentException("Name too short");
//      }
      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Email already registered");
      }

      //1 : both dto and entity = DRY violation
      //2: only in Dto : allows biz logic to break the constraint and persist shitty data.
      //3 : only in @Entity will allow invalid Dto to be converted to corrupted entity and processed
      //4: Entities guarding their constraints << scary but DDD
         // - YOU ONLY know about first error. OKish if your client is only YOUR FE.


      registerCustomerService.registerCustomer(customer);

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
