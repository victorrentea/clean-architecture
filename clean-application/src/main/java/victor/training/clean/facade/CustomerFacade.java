package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.user.entity.Email;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.user.repo.SiteRepo;
import victor.training.clean.insurance.service.QuotationService;
import victor.training.clean.customer.service.RegisterCustomerService;

import java.util.List;

@Service
@Transactional // as scoate-o daca:
// 1) lucrez cu @DomainEvents si vreau sa oblig save
// 2) mi-e frica de autoflush ca am avut belele in viata
// 3) high tps systems (1000 req/min) > vrei sa limitezi connection starvation mai ales daca chemi alte servicii REST
@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerRepo customerRepo;
   private final EmailSender emailSender;
   private final SiteRepo siteRepo;
   private final CustomerSearchRepo customerSearchRepo;
   private final QuotationService quotationService;
   private final RegisterCustomerService registerCustomerService;

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }

//      CustomerDto dto = mapper.toDto(customer);
//      CustomerDto dto =automapper.map(customer);
      //      CustomerDto dto = customer.toDto(); // la asta te sun. reject la PR si te sun. Vin la tine sa-ti explic
   public CustomerDto findById(long customerId) {
      Customer customer = customerRepo.findById(customerId).get();
      return new CustomerDto(customer);
   }
   public Customer findByIdBad(long customerId) {
      return customerRepo.findById(customerId).get();
   }

   @Transactional
   public void register(CustomerDto dto) {
      Customer customer = dto.toEntity();

      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Email already registered");
      }

      registerCustomerService.register(customer);

//      quotationService.requoteCustomer(customer.getId());

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
