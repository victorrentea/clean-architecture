package victor.training.clean.verticalslice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
import victor.training.clean.domain.model.Site;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class RegisterCustomerUseCase {
  private final QuotationService quotationService;
  private final CustomerRepo customerRepo;
  private final EmailSender emailSender;

  @Builder
  @Value
  @AllArgsConstructor
  public static class RegisterCustomerRequest { // Dto used to both QUERY and COMMAND use-cases ?
    @Schema(description = "Name of the customer")
    @Size(min = 5, message = "{customer-name-too-short}")
    String name;
    @javax.validation.constraints.Email
    @NotNull
    String email;
    Long siteId;
  }
  @Transactional
  @PostMapping("customer")
  public void register(@RequestBody @Validated RegisterCustomerRequest request) {
    Customer customer = new Customer(request.name);
    customer.setEmail(request.email);
    customer.setSite(new Site().setId(request.siteId));
    customer.setCreationDate(LocalDate.now());

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
