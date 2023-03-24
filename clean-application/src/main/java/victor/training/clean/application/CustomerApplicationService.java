package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.repo.SearchRepo;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.Site;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;

import java.util.List;
import java.util.Objects;

@RestController
//@Service
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService/* implements CustomerApplicationServicePort*/ { // aka facade, orchestrator
  private final RegisterCustomerService registerCustomerService;
  private final CustomerRepo customerRepo;
  private final EmailSender emailSender;
  private final SearchRepo searchRepo;
  private final QuotationService quotationService;

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return searchRepo.search(searchCriteria);
  }

  @GetMapping("{id}")
  public CustomerDto findById(@PathVariable long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    // DTO, MAPPER, ENTITY{NEVER!}
    return new CustomerDto(customer);
  }

  @Transactional
  @PostMapping
  public void register(@RequestBody @Validated CustomerDto dto) {
    Customer customer = dto.toEntity();
    registerCustomerService.register(customer);
    quotationService.quoteCustomer(customer);
    sendRegistrationEmail(customer);
  }

  public void update(long id, CustomerDto dto) { // TODO move to Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.getName());
    customer.setEmail(dto.getEmail());
    customer.setSite(new Site().setId(dto.getSiteId()));

    // tricky part
    if (!customer.isGoldMember() && dto.isGold()) {
      customer.setGoldMember(true);
      sendGoldWelcomeEmail(customer);
    }
    if (customer.isGoldMember() && !dto.isGold()) {
      // TODO api call get order history, and check stuff..
      customer.setGoldMember(false);
      auditGoldMemberRemoval(customer, Objects.requireNonNull(dto.getGoldMemberRemovalReason()));
    }

    customerRepo.save(customer); // not required by the ORM because of @Transactional
  }

  private void sendRegistrationEmail(Customer customer) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(customer.getEmail());
    email.setSubject("Account created for");
    email.setBody("Welcome to our world, " + customer.getName() + ". You'll like it! Sincerely, Team");
    emailSender.sendEmail(email);
  }

  private void sendGoldWelcomeEmail(Customer customer) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(customer.getEmail());
    email.setSubject("Welcome to the Gold membership!");
    email.setBody("Here are your perks: ...");
    emailSender.sendEmail(email);
  }

  private void auditGoldMemberRemoval(Customer customer, String reason) {
    System.out.println("Send message on Kafka: name:" + customer.getName() + ", reason:" + reason);
  }
}
