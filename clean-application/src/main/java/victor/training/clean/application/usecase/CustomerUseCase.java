package victor.training.clean.application.usecase;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.in.rest.dto.CustomerDto;
import victor.training.clean.in.rest.dto.SearchCustomerCriteria;
import victor.training.clean.in.rest.dto.SearchCustomerResponse;
import victor.training.clean.application.entity.Country;
import victor.training.clean.application.entity.Customer;
import victor.training.clean.application.repo.CustomerRepo;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
//@ApplicationService // custom annotation refining the classic @Service
@RestController
public class CustomerUseCase {

  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchRepo customerSearchRepo;
  private final InsuranceService insuranceService;

  public List<SearchCustomerResponse> search(SearchCustomerCriteria searchCriteria) {
    return customerSearchRepo.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    // boilerplate mapping code TODO move somewhere else
    // - NU Domain Service caci e cuplat la Dto
    // - o clasa Mapper in care indesi logica asta boring
    // - MapStruct
    //- NU customer.toDto(); // datorita dependency rule
    return new CustomerDto(customer);
  }

  @Transactional
  public void register(CustomerDto dto) {
    Customer customer = dto.toEntity();
    // request payload validation - poate fi facuta pe DTO singur
//    AnafResult anafResult = legalEntityProvider.query(customer.getLegalEntityCode());
    registerCustomerService.register(customer);
    notificationService.sendWelcomeEmail(customer, "1"); // userId from JWT token via SecuritContext
  }
  private final RegisterCustomerService registerCustomerService;

  @Transactional
  public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.getName());
    customer.setEmail(dto.getEmail());
    customer.setCountry(new Country().setId(dto.getCountryId()));

    if (!customer.isGoldMember() && dto.isGold()) {
      // enable gold member status
      customer.setGoldMember(true);
      notificationService.sendGoldBenefitsEmail(customer, "1"); // userId from JWT token via SecuritContext
    }

    if (customer.isGoldMember() && !dto.isGold()) {
      // remove gold member status
      customer.setGoldMember(false);
      customer.setGoldMemberRemovalReason(requireNonNull(dto.getGoldMemberRemovalReason()));
      auditRemovedGoldMember(customer.getName(), dto.getGoldMemberRemovalReason());
    }

    customerRepo.save(customer); // not required within a @Transactional method if using ORM(JPA/Hibernate)
    insuranceService.customerDetailsChanged(customer);
  }

  private void auditRemovedGoldMember(String customerName, String reason) {
    log.info("Kafka.send ( {name:" + customerName + ", reason:" + reason + "} )");
  }
}
