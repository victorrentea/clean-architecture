package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.entity.Customer;
import victor.training.clean.entity.Email;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.service.RegisterCustomerService;

import java.util.List;

@Service
@Transactional
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
      return new CustomerDto(customerRepo.findById(customerId).get());
   }

   public void register(CustomerDto dto) {
      Customer customer = customerMapper.toEntity(dto);

      if (customerRepo.existsByEmail(customer.getEmail())) {
         throw new IllegalArgumentException("Email already registered");
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
