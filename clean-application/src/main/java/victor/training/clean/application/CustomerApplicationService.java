package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import victor.training.clean.common.Facade;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.common.domain.service.NotificationService;
import victor.training.clean.customer.domain.service.RegisterCustomerService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.customer.domain.repo.SiteRepo;
import victor.training.clean.customer.door.event.CustomerRegisteredEvent;
import victor.training.clean.insurance.domain.service.QuotationService;

import java.time.format.DateTimeFormatter;
import java.util.List;

//@Service
@Facade
//@RestController
@RequestMapping("customer")
@Transactional // probably too broad for high-TPS systems
@RequiredArgsConstructor
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final ApplicationEventPublisher eventPublisher;
//    private final QuotationService quotationService; // black sheep of the family
    private final RegisterCustomerService registerCustomerService;
    private final NotificationService notificationService;

    @GetMapping("{id}")
    public CustomerDto findById(@PathVariable long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // TODO move mapping logic somewhere else
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .siteId(customer.getSite().getId())
                .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }
    public void register(CustomerDto dto) {
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

        registerCustomerService.register(customer);

//        quotationService.quoteCustomer(customer.getId(), customer.getName());
        eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId(), customer.getName()));

        notificationService.sendRegistrationEmail(customer.getEmail());
    }




}
