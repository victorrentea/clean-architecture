package victor.training.clean.application.service;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.infra.AnafClient;

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
  private final AnafClient anafClient;

   @Operation(description = "Search Customer")
   @PostMapping("customers/search")
   public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
    // in case I cannot pass in application service the CSC (dto) directly, then I will copy-paste that class
    // into a DOMAIN class identical in structure and map field-to-field to it.

    // if the dto cannot be easily mapped to one DM object, you will create another class just to be able to pass it to the domain
    return customerSearchQuery.search(searchCriteria);
  }
  // BREAKING THE LAW: couplig nthe application code to REST. (to KISS, pragmatic variation) many would label this "BLASPHEMY"
  // - versioning: I can't do v1.CustomerController  vs CustomerControllerV2 vs CustomerControllerV3 (interfaces) + Dto
  // - expose the same feature over multiple channels: REST, SOAP, Kafka, JMS, etc.


  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    // Several lines of domain logic operating on the state of a single Entity
    // TODO Where can I move it? PS: it's repeating somewhere else
    // a) move it in a domain.service.DiscountService💖 (if small)
    // -) CustomerHellper/Util.getDiscountPercentage(customer) < NEVER
    // -) CustomerService🛑.getDiscountPercentage(customer) < NEVER too broad

    // boilerplate mapping code TODO move somewhere else
    // a) dto = mapStruct.map(customer);
    // b) dto = new CustomerDto(customer); 💖
    // c) dto = customer.toDto(); NO: couples DM to external API
    // d) dto = CustomerUtil.toDto(); NO
    return CustomerDto.fromEntity(customer);
  }

  @Transactional
  public void register(CustomerDto dto) {
    Customer customer = dto.asEntity(); // can't do this if your DTOs are generated

    // request payload validation
    // anti-social validoation: the client only sees the FIRST failure.
    // annotation report all violations at once,
//    if (customer.getName().length() < 5) { // TODO alternatives to implement this?
//      throw new IllegalArgumentException("The customer name is too short");
//    }

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
    customerRepo.save(customer);
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
}
