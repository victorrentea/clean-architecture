package victor.training.clean.verticalslice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;

@RequiredArgsConstructor
@RestController
public class RegisterCustomerUseCase {
  private final QuotationService quotationService;
  private final CustomerRepo customerRepo;
  private final EmailSender emailSender;
//  public interface MyRepo extends Repository {
//    @Query
//    ceameunevoie()
//  }

  @Transactional
  @PostMapping("customer")
  public void register(@RequestBody @Validated CustomerDto dto) {
    Customer customer = dto.toEntity();
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Customer email is already registered");
      // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Domain Logic using discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic

    customerRepo.save(customer);
    // Heavy business logic
    // aici customer.id != null

    quotationService.quoteCustomer(customer);
    sendRegistrationEmail(customer); // DRY intre UC !
  }
  private void sendRegistrationEmail(Customer customer) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(customer.getEmail());
    email.setSubject("Account created for");
    email.setBody("Welcome to our world, " + customer.getName() + ". You'll like it! Sincerely, Team");
    emailSender.sendEmail(email);
  }

}
