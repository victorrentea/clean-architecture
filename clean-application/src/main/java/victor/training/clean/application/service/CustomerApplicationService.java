package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.controller.api.dto.CustomerDto;
import victor.training.clean.application.controller.api.dto.CustomerSearchCriteria;
import victor.training.clean.application.controller.api.dto.CustomerSearchResult;
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
@RestController
public class CustomerApplicationService {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchQuery customerSearchQuery;
  private final InsuranceService insuranceService;
  private final FiscalDetailsProvider anafClient;

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  // poti squashui controller REST cu primul layer de logica
  // DOAR DACA nu ai lucru cu HTTP scarbos: ResponseEntity, MultipartFile
  // TOT NU FACI ASTA daca expui si REST si Kafka... GPRC

  @GetMapping("customers/{id}") //  M VC->FE(ts/js);
  // MVC avea sens in vremea .jsp / .jsf / .velocity / .thymeleaf / .freemarker / .ftl
  public CustomerDto findById(@PathVariable long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    // Bit of domain logic on the state of one Entity?  What TODO?
    // PS: it's also repeating somewhere else

    // boilerplate mapping code TODO move somewhere else
    //  1) MapStruct
    //  2) dto = customer.toDto(); RAU: ar cupla domeniul de API DTO
    return CustomerDto.fromEntity(customer);  // daca nu ti-ai generat DTO din openapi
  }



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
