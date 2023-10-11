package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.infra.FiscalAuthority;

import java.util.List;

import static java.util.Objects.requireNonNull;

@RestController
@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
public class CustomerApplicationService {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchRepo customerSearchRepo;
  private final InsuranceService insuranceService;
  private final FiscalAuthority anafClient;
  private final RegisterCustomerService registerCustomerService;

  //@PostMapping

  public List<SearchCustomerResponse> search(SearchCustomerCriteria searchCriteria) {
    return customerSearchRepo.search(searchCriteria);
  }

  @GetMapping("customer/{id}")
  public CustomerDto findById(@PathVariable long id) {
    return CustomerDto.from(customerRepo.findById(id).orElseThrow());
  }
  @PostMapping("customer")
  @Transactional
//  @Kafka
  public void register(@RequestBody @Validated CustomerDto dto) {
    Customer customer = dto.toEntity();
    registerCustomerService.register(customer);
    notificationService.sendWelcomeEmail(customer, "1"); // userId from JWT token via SecuritContext
  }



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
