package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.Site;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;

import java.util.List;

import static java.util.Objects.requireNonNull;

// scop: asta este un "Facade" - tre sa fie doar un coordonator care sa NU faca munca grea/boring.
// sa ofere un overview asupra fluxului respectiv, delegant la alte clase de sub

//@Service
@RestController
@RequestMapping("customer")
//@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService implements CustomerApi {
  private final CustomerRepo customerRepo;
  private final EmailSender emailSender;
  private final SiteRepo siteRepo;
  private final CustomerSearchRepo customerSearchRepo;
  private final CustomerMapStruct mapper;


  private final RegisterCustomerService registerCustomerService;
//
//  @Override
//  public CustomerDto findById(long id) {
//    Customer customer = customerRepo.findById(id).orElseThrow();
////    CustomerDto dto = mapper.toDto(customer);
//    // BORING mapping logic de la Entiy -> Dto? TODO move somewhere else
////    return CustomerDto.builder()
////            .id(customer.getId())
////            .name(customer.getName())
////            .email(customer.getEmail())
////            .siteId(customer.getSite().getId())
////            .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
////            .build();
//
//    // #1 in DTO constructor
//    return new CustomerDto(customer);
//
//    // #2
//    //return customerMapper.toDto(customer); // mapper scris de mana
//
//    // NICIODATA: incalca dependency rule: domain nu ar trebui sa stie de Dto
////    CustomerDto dto = customerEntity.toDto();
////    return dto;
//  }

  // 301 Moved Permanently  to victor.training.clean.verticalslice.FindCustomerByIdUseCase Vertical Slice


  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchRepo.search(searchCriteria);
  }


  // daca poti inregistra customer si prin POST REST si prin MQ =>
  // iti faci o structura noua de date numita CreateCustomerRequest pe care o creezi din
  // a) rest API
  // b) listener de ActiveMQ
  // pt a refolosi cat mai mult din application logic
  @Transactional
//  todo ? @PostMapping
  public void register(CustomerDto dto) { // TODO use different models for read vs write (Lite CQRS)
    Customer customer = dto.toEntity();
    // validation TODO explore alternatives:
    //   javax.validation pe Dto facut de la intrare + apare in swagger
//    if (customer.getName().length() < 5) {
//      throw new IllegalArgumentException("Name too short");
//    }
    registerCustomerService.register(customer);
    quotationService.quoteCustomer(customer);
    sendRegistrationEmail(customer);
  }

  private final QuotationService quotationService;

  @Transactional
  public void update(long id, CustomerDto dto) { // TODO move to Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.getName());
    customer.setEmail(dto.getEmail());
    customer.setSite(new Site().setId(dto.getSiteId()));

    // custom logic when a SPECIAL part of the customer data changes => Task-Based UI
    if (!customer.isGoldMember() && dto.isGold()) {
      customer.setGoldMember(true);
      sendGoldWelcomeEmail(customer);
    }

    if (customer.isGoldMember() && !dto.isGold()) {
      customer.setGoldMember(false);
      customer.setGoldMemberRemovalReason(requireNonNull(dto.getGoldMemberRemovalReason()));
      auditGoldMemberRemoval(customer, dto.getGoldMemberRemovalReason());
    }

     customerRepo.save(customer); // ORM Trick: not required by the ORM because of @Transactional on the method
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
    System.out.println("Kafka.send ( {name:" + customer.getName() + ", reason:" + reason + "} )");
  }
}
