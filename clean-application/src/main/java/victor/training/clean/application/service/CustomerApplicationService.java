package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.repo.SupplierRepo;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.FiscalDetailsProvider;
import victor.training.clean.domain.service.RegisterCustomerService;

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

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
// a: mapstruct addiction
//    var dto = customer.toDto(); // SO WRONG:  couples Domain Model to outside world
    // b:
    return CustomerDto.fromEntity(customer);
  }

  // Tell me a joke about cloud native apps:
  // Why do cloud native apps always carry an umbrella?
  // Because they can't handle a little server rain!

  private final SupplierRepo supplierRepo;
  private final RegisterCustomerService registerCustomerService;
  @Transactional
  public void register(CustomerDto dto) {
    Customer customer = dto.toEntity();
    registerCustomerService.register(customer);
//    kafka.send(..);
//    email.send();
//    api.call();
//    rabbit.send(); ; activeMQ (JMS supporting JTA = 2PC)
    eventPublisher.publishEvent(new SendEmailAfterTransaction("FULL",customer));
  }
  private final ApplicationEventPublisher eventPublisher;

  record SendEmailAfterTransaction(String email, Customer customer) {}

  // OR avoid Spring kung fu and move @Transactional one level below! DO THIS. i'm sorry to have shown you this:
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void method(SendEmailAfterTransaction event) {
    notificationService.sendWelcomeEmail(event.customer(), event.email()); // userId from JWT token via SecuritContext

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

  public void removeGold(long id, String reason) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    if (!customer.isGoldMember()) {
      throw new IllegalArgumentException("Customer is not a Gold Member");
    }
    customer.setGoldMember(false);
    customer.setGoldMemberRemovalReason(reason);
    customerRepo.save(customer);
  }
}
