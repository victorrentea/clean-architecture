package victor.training.clean.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.CustomerDto;
import victor.training.clean.application.NotificationService;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.QuotationService;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateCustomerUseCase {
  private final CustomerRepo customerRepo;
  private final NotificationService notificationService;
  private final QuotationService quotationService;

  @Transactional
  @PutMapping("customer/{id}")
  public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    // CRUD part
    customer.setName(dto.getName());
    customer.setEmail(dto.getEmail());
    customer.setCountry(new Country().setId(dto.getCountryId()));

    if (!customer.isGoldMember() && dto.isGold()) {
      // enable gold member status
      customer.setGoldMember(true);
      notificationService.sendGoldBenefitsEmail(customer);
    }

    if (customer.isGoldMember() && !dto.isGold()) {
      // remove gold member status
      customer.setGoldMember(false);
      customer.setGoldMemberRemovalReason(requireNonNull(dto.getGoldMemberRemovalReason()));
      auditGoldMemberRemoval(customer, dto.getGoldMemberRemovalReason());
    }

    customerRepo.save(customer); // not actually required within a @Transactional method if using ORM(JPA/Hibernate)
    quotationService.customerDetailsChanged(customer);
  }

  private void auditGoldMemberRemoval(Customer customer, String reason) {
    // [imagine]
    System.out.println("Kafka.send ( {name:" + customer.getName() + ", reason:" + reason + "} )");
  }
}
