package victor.training.clean.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.in.rest.dto.CustomerSearchCriteria;
import victor.training.clean.in.rest.dto.CustomerSearchResult;
import victor.training.clean.app.model.AnafResult;
import victor.training.clean.app.model.Country;
import victor.training.clean.app.model.Customer;
import victor.training.clean.app.repo.CustomerRepo;
import victor.training.clean.app.repo.CustomerSearchQuery;
import victor.training.clean.out.AnafClient;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@Service // custom annotation refining the classic @Service
public class CustomerApplicationService {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchQuery customerSearchQuery;
  private final InsuranceService insuranceService;
  private final AnafClient anafClient;

  // code smell = stupid method ("Middle Man")
  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  public Customer findById(long id) {
    return customerRepo.findById(id).orElseThrow();
  }

  @Transactional
  public void register(Customer customer) {
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
    if (customer.getLegalEntityCode().isPresent()) {
      if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode().get())) {
        throw new IllegalArgumentException("Company already registered");
      }
      AnafResult anafResult = anafClient.query(customer.getLegalEntityCode().get());
      if (anafResult == null || !normalize(customer.getName()).equals(normalize(anafResult.getName()))) {
        throw new IllegalArgumentException("Legal Entity not found!");
      }
      if (anafResult.isVatPayer()) {
        customer.setDiscountedVat(true);
      }
    }
    log.info("More Business Logic (imagine)");
    log.info("More Business Logic (imagine)");
    customerRepo.save(customer);
    notificationService.sendWelcomeEmail(customer, "FULL"); // userId from JWT token via SecuritContext
  }

  private String normalize(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }

  @Transactional
  public void update(long id, UpdateCommand command) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(command.name());
    customer.setEmail(command.email());
    customer.setCountry(new Country().setId(command.countryId()));


    if (!customer.isGoldMember() && command.gold()) {
      // enable gold member status
      customer.setGoldMember(true);
    }

    if (customer.isGoldMember() && !command.gold()) {
      // remove gold member status
      customer.setGoldMember(false);
      customer.setGoldMemberRemovalReason(requireNonNull(command.goldMemberRemovalReason()));
      auditRemovedGoldMember(customer.getName(), command.goldMemberRemovalReason());
    }

    customerRepo.save(customer); // not required within a @Transactional method if using ORM(JPA/Hibernate)
    insuranceService.customerDetailsChanged(customer);
  }

  public record UpdateCommand(
      String name,
      String email,
      Long countryId,
      boolean gold,
      String goldMemberRemovalReason
  ) {}


  private void auditRemovedGoldMember(String customerName, String reason) {
    log.info("Kafka.send ( {name:" + customerName + ", reason:" + reason + "} )");
  }
}
