package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.CustomerService;
import victor.training.clean.domain.service.FiscalDetailsProvider;
import victor.training.clean.domain.service.NotificationService;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
public class CustomerApplicationService {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchQuery customerSearchQuery;
  private final InsuranceService insuranceService;
  private final FiscalDetailsProvider anafClient;
  private final CustomerService customerService;

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    return CustomerDto.fromEntity(customer);
  }

  @Transactional
  @PostMapping("customers")
  public void register(@RequestBody @Validated CustomerDto dto) {
    Customer customer = dto.toEntity();
//    if (customerRepo.existsByEmail(customer.getEmail())) {
//      throw new IllegalArgumentException("A customer with this email is already registered!");
//       throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
//    }
    customerService.register(customer);
    notificationService.sendWelcomeEmail(customer, "FULL"); // userId from JWT token via SecuritContext
  }


  @Transactional
  public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.name());
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
