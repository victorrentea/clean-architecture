package victor.training.clean.application.service;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.AnafClient;
import victor.training.clean.infra.FiscalDetailsProvider;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
@RestController
public class CustomerApplicationService {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchRepo customerSearchRepo;
  private final InsuranceService insuranceService;
  private final AnafClient anafClient;

  @Operation(description = "Search Customer; For example:...")
  // move Swagger/OpenAPi docs on an interface that you implement
  @PostMapping("customers/search")
  public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
    return customerSearchRepo.search(searchCriteria);
  }
  // IMPOSSIBLE if you expose the same usecase from a MQ (Kafka) consumer? Grpc endpoint? SOAP? GraphQL? WebSockets?
  // Volatile: if you need to change the contract you toucj the service

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    // Several lines of domain logic operating on the state of a single Entity
    // TODO Where can I move it? PS: it's repeating somewhere else

    // boilerplate mapping code TODO move somewhere else
    return CustomerDto.fromEntity(customer)
//        .countryId()//more/..
        // @ciprian: If I customise the DTO outside the DTO, will this be a pitfall for juniors?
        // introduces heterogeneity in the design (eg via an .adr + pairing)
        // a more homogenous solution -> a dedicated "mapper" class
        .build();
  }

  @Transactional
  @PostMapping("customers")
  public void register(@RequestBody @Validated CustomerDto dto) {
    // validation has two types:
    // 1. request payload validation/sanitize/ranges/sizes/XSS/whitelisting that you can apply on the DTO alone
    // returns 404 STATUS CODE
    // 2. business rule
    Customer customer = dto.toEntity(); // abuse of DTO: used in two endpoints > go CQRS

    registerCustomerService.register(customer);
    notificationService.sendWelcomeEmail(customer, "FULL"); // userId from JWT token via SecuritContext
  }

  private final RegisterCustomerService registerCustomerService;


  @Transactional
  public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
//    customer.setName(dto.name());
    customer.setEmail(dto.email());
    customer.setCountry(new Country().setId(dto.countryId()));

    if (!customer.isGoldMember() && dto.gold()) {
      // enable gold member status
      customer.setGoldMember(true);
      notificationService.sendGoldBenefitsEmail(customer, "1"); // userId from JWT token via SecuritContext
    }

    if (customer.isGoldMember() && !dto.gold()) {
      // remove gold member status
      customer.setGoldMember(false);
      customer.setGoldMemberRemovalReason(requireNonNull(dto.goldMemberRemovalReason()));
      auditRemovedGoldMember(customer.getName(), dto.goldMemberRemovalReason());
    }

    customerRepo.save(customer); // not required within a @Transactional method if using ORM(JPA/Hibernate)
    insuranceService.customerDetailsChanged(customer);
  }

  private void auditRemovedGoldMember(String customerName, String reason) {
    log.info("Kafka.send ( {name:" + customerName + ", reason:" + reason + "} )");
  }


}
