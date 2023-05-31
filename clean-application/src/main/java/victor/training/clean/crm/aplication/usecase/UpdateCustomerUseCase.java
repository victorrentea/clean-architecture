package victor.training.clean.crm.aplication.usecase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.crm.api.event.CustomerDetailsChangedEvent;
import victor.training.clean.crm.api.event.CustomerUpgradedToGoldEvent;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateCustomerUseCase {
  private final CustomerRepo customerRepo;
//  private final NotificationService notificationService;
  private final ApplicationEventPublisher eventPublisher;


  @Builder
  @Value
  @AllArgsConstructor
  static class UpdateCustomerRequest { // Dto used to both QUERY and COMMAND use-cases ?
    @Size(min = 5) // yes OK earlier.
    String name; // *

    @Email
    String email; // *

    Long countryId; // *

    boolean gold; // GET & PUT
    String goldMemberRemovalReason; // GET & PUT if gold=true->false

    String legalEntityCode; // *
  }

  @Transactional
  @PutMapping("customer/{id}")
  public void update(@PathVariable long id, @RequestBody UpdateCustomerRequest dto) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.getName());
    customer.setEmail(dto.getEmail());
    customer.setCountryId(dto.getCountryId());

    if (!customer.isGoldMember() && dto.isGold()) {
      // enable gold member status
      customer.setGoldMember(true);
//      notificationService.sendGoldBenefitsEmail(customer);
      eventPublisher.publishEvent(new CustomerUpgradedToGoldEvent(customer.getId()));
    }

    if (customer.isGoldMember() && !dto.isGold()) {
      // remove gold member status
      customer.setGoldMember(false);
      customer.setGoldMemberRemovalReason(requireNonNull(dto.getGoldMemberRemovalReason()));
      auditGoldMemberRemoval(customer, dto.getGoldMemberRemovalReason());
    }

    customerRepo.save(customer); // not actually required within a @Transactional method if using ORM(JPA/Hibernate)
    eventPublisher.publishEvent(new CustomerDetailsChangedEvent(customer.getId()));
  }

  private void auditGoldMemberRemoval(Customer customer, String reason) {
    // [imagine]
    System.out.println("Kafka.send ( {name:" + customer.getName() + ", reason:" + reason + "} )");
  }
}
