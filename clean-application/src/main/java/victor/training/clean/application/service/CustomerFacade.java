package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.application.controller.dto.CustomerDto;
import victor.training.clean.application.controller.dto.CustomerSearchCriteria;
import victor.training.clean.application.controller.dto.CustomerSearchResult;
import victor.training.clean.application.ApplicationService;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.IAnafClient;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.domain.service.UserFetcher;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
public class CustomerFacade {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final CustomerSearchQuery customerSearchQuery;
  private final InsuranceService insuranceService;
  private final IAnafClient anafClient;
  private final RegisterCustomerService registerCustomerService;

  public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
    return customerSearchQuery.search(searchCriteria);
  }

  public CustomerDto findById(long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();

    // Bit of domain logic on the state of one Entity?  What TODO?
    // PS: it's also repeating somewhere else

    // boilerplate mapping code TODO move somewhere else
    return CustomerDto.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .status(customer.getStatus())
        .createdDate(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .goldMember(customer.isGoldMember())

        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressZip(customer.getShippingAddress().zip())

        .canReturnOrders(customer.canReturnOrders())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        .build();
  }

  @Transactional
  public void register(CustomerDto dto) {
    Customer customer = dto.toEntity();
    registerCustomerService.register(customer);
    User user = userFetcher.fetchUser("FULL");
    notificationService.sendWelcomeEmail(customer, user); // userId from JWT token via SecuritContext
  }
  private final UserFetcher userFetcher;

  @Transactional
  public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.name());
    customer.setEmail(dto.email());
    customer.setCountry(new Country().setId(dto.countryId()));

    if (!customer.isGoldMember() && dto.goldMember()) {
      // enable gold member status
      customer.setGoldMember(true);
    }

    if (customer.isGoldMember() && !dto.goldMember()) {
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
