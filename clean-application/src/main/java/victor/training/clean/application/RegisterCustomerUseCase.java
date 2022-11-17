package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.domain.repo.SiteRepo;
import victor.training.clean.common.domain.service.NotificationService;
import victor.training.clean.insurance.domain.service.QuotationService;
import victor.training.clean.application.dto.CustomerDto;

@RequiredArgsConstructor
@RestController
public class RegisterCustomerUseCase { // feature slice
  private final SiteRepo siteRepo;
  private final CustomerRepo customerRepo;
  private final QuotationService quotationService;
  private final NotificationService notificationService;

  @PostMapping("feature-slice")
  public void register(@RequestBody CustomerDto dto) {
    Customer customer = new Customer();
    customer.setEmail(dto.getEmail());
    customer.setName(dto.getName());
    customer.setSite(siteRepo.getById(dto.getSiteId()));

    // TODO experiment all the ways to do validation
    if (customer.getName().length() < 5) {
      throw new IllegalArgumentException("Name too short");
    }
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Customer email is already registered");
      // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }
    register(customer);

    quotationService.quoteCustomer(customer);

    notificationService.sendRegistrationEmail(customer.getEmail());
  }

  private void register(Customer customer) {
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
    customerRepo.save(customer);
    // Heavy business logic
  }
}
