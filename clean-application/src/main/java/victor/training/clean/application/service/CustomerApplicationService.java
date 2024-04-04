package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.repo.SupplierRepo;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.infra.AnafClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
  private final AnafClient anafClient;

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    // Several lines of domain logic operating on the state of a single Entity
    // TODO Where can I move it? PS: it's repeating somewhere else
    int discountPercentage = customer.getDiscountPercentage();

    // boilerplate mapping code TODO move somewhere else
    return CustomerDto.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .createdDateStr(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())

        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressZip(customer.getShippingAddress().zip())


        .discountPercentage(discountPercentage)
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        .build();
  }

  // Tell me a joke about cloud native apps:
  // Why do cloud native apps always carry an umbrella?
  // Because they can't handle a little server rain!

  private final SupplierRepo supplierRepo;
  @Transactional
  public void register(CustomerDto dto) {
    Customer customer = new Customer();
    customer.setEmail(dto.email());
    customer.setName(dto.name());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(supplierRepo.findById(dto.countryId())
        .orElseThrow(() -> new IllegalArgumentException("Country not found")));

//    customer.setCountry(new Country().setId(dto.countryId()));
    customer.setLegalEntityCode(dto.legalEntityCode());

    // request payload validation
    if (customer.getName().length() < 5) { // TODO alternatives to implement this?
      throw new IllegalArgumentException("The customer name is too short");
    }

    // business rule/validation
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("A customer with this email is already registered!");
      // throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

    // enrich data from external API
    if (customer.getLegalEntityCode() != null) {
      if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode())) {
        throw new IllegalArgumentException("Company already registered");
      }
      AnafResult anafResult = anafClient.query(customer.getLegalEntityCode());
      if (anafResult == null || !normalize(customer.getName()).equals(normalize(anafResult.getName()))) {
        throw new IllegalArgumentException("Legal Entity not found!");
      }
      if (anafResult.isVatPayer()) {
        customer.setDiscountedVat(true);
      }
    }
    log.info("More Business Logic (imagine)");
    log.info("More Business Logic (imagine)");
    customerRepo.save(customer); // INSERT not here in DB but AFTER the end of the method
//    kafka.send(..);
//    email.send();
//    api.call();
//    rabbit.send(); ; activeMQ (JMS supporting JTA = 2PC)
    notificationService.sendWelcomeEmail(customer, "FULL"); // userId from JWT token via SecuritContext
  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
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
