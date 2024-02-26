package victor.training.clean.application.service;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.CustomerService;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.FiscalDetailsProvider;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@RestController // custom annotation refining the classic @Service
public class CustomerApplicationService implements CustomerApplicationApi {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchRepo customerSearchRepo;
  private final InsuranceService insuranceService;
  private final FiscalDetailsProvider anafClient;

   @Operation(description = "Search Customer")
   @PostMapping("customers/search")
//   @KafkaListener(topics = "customer-search")
   public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
    return customerSearchRepo.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    // boilerplate mapping code converting Entity->Dto TODO move somewhere else:
//    return customer.toDto();
//    return new CustomerDto(customer);
    return CustomerDto.fromEntity(customer);
    // other options:
    // manualMapper.toDto
    // mapStruct.toDto
  }

   @Override
   @Transactional
   public void register(CustomerDto dto) {
     Customer customer = dto.toEntity();
     customerService.register(customer);
     notificationService.sendWelcomeEmail(customer, "FULL"); // userId from JWT token via SecuritContext
  }

  private final CustomerService customerService;

  @Transactional
  public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.name());
    customer.setEmail(dto.email());
    customer.setCountry(new Country().setId(dto.country()));

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
