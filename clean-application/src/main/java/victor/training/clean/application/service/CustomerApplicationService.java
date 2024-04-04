package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.domain.IAnafClient;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.RegisterCustomerService;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
@RestController
public class CustomerApplicationService {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchQuery customerSearchQuery;
  private final InsuranceService insuranceService;

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    return CustomerDto.fromEntity(customer);
  }

    // + less boiler code
    // - couple the logic with the transport mechanism (ie 3 annotations)
  @Transactional
  @PostMapping("customers")
  public void register(@RequestBody @Validated CustomerDto dto) {
    MDC.put("email", dto.emailAddress()); // OpenTelemetry "Baggage"
    Customer customer = dto.toEntity();

    registerCustomerService.register(customer);
    notificationService.sendWelcomeEmail(customer, "FULL"); // userId from JWT token via SecuritContext
  }

  private final RegisterCustomerService registerCustomerService;

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.name());
    customer.setEmail(dto.emailAddress());
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
