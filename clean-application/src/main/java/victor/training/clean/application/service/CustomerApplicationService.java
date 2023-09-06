package victor.training.clean.application.service;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.dto.CreateCustomerRequest;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.IAnafClient;

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
  private final RegisterCustomerService registerCustomerService;

  @Operation(description = "Search Customer")
  @PostMapping("customer/search")
  public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
    return customerSearchRepo.search(searchCriteria); // "Relaxed Layered Architecture" :
    // we are allowed to SKIP layers if we go in the same direction
  }

  @GetMapping("customer/{id}")
  public CustomerDto findById(@PathVariable long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    // Several lines of domain logic operating on the state of a single Entity
    // TODO Where can I move it? PS: it's repeating somewhere else

//    #1 return customerMapper.toDto(customer);
    return new CustomerDto(customer); // #2 it is OK to have DTO depending on Entity, but NOT Entity -> Dto

//    CustomerDto dto = customer.toDto(); WRONG!
  }

  @Transactional
  @PostMapping("customer")
  public void register(@RequestBody @Validated CreateCustomerRequest dto) {
    Customer customer = dto.toEntity(); // or mapper
    // Facade design pattern orchestrates the use-case
    // provides a high-level view of the STEPS of this UC
    registerCustomerService.register(customer);

    notificationService.sendWelcomeEmail(customer, "1"); // userId from JWT token via SecuritContext
  }



  @Transactional
  @PutMapping("customer/{id}/gold/activate")
  public void setGold(@PathVariable long id) {
    Customer old = customerRepo.findById(id).orElseThrow();
    if (old.isGoldMember()) {
      throw new IllegalStateException("Already gold");
    }
    // enable gold member status
    old.setGoldMember(true);
    notificationService.sendGoldBenefitsEmail(old, "1"); // userId from JWT token via SecuritContext
  }
  @Transactional
  @PutMapping("customer/{id}")
  public void update(@PathVariable long id, @RequestBody @Validated CustomerDto dto) {
    Customer old = customerRepo.findById(id).orElseThrow();
    // CRUD part
    old.setName(dto.getName());
    old.setEmail(dto.getEmail());
    old.setCountry(new Country().setId(dto.getCountryId()));

    if (!old.isGoldMember() && dto.isGold()) {
      // enable gold member status
      old.setGoldMember(true);
      notificationService.sendGoldBenefitsEmail(old, "1"); // userId from JWT token via SecuritContext
    }

    if (old.isGoldMember() && !dto.isGold()) {
      // remove gold member status
      old.setGoldMember(false);
      old.setGoldMemberRemovalReason(requireNonNull(dto.getGoldMemberRemovalReason()));
      auditRemovedGoldMember(old.getName(), dto.getGoldMemberRemovalReason());
    }

    customerRepo.save(old); // not required within a @Transactional method if using ORM(JPA/Hibernate)
    insuranceService.customerDetailsChanged(old);
  }


  private void auditRemovedGoldMember(String customerName, String reason) {
    log.info("Kafka.send ( {name:" + customerName + ", reason:" + reason + "} )");
  }
}
