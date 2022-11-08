package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.common.Facade;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.infra.NotificationService;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.util.List;

//@Service
@Facade
@Transactional // probably too broad for high-TPS systems
@RequiredArgsConstructor
@RestController
public class CustomerFacade {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final CustomerMapper customerMapper;
    private final RegisterCustomerService registerCustomerService;
    private final NotificationService notificationService;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
//        return customerService.search(searchCriteria);
        return customerSearchRepo.search(searchCriteria);
    }
//    @PreAuthorized
    @GetMapping("{id}")
    public CustomerDto findById(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // TODO move mapping logic somewhere else
        // 1 map structs & co.
        // 2 CustomerMapper / Util🤢 / Factory🤢 / Transformer🔥
        // 3 new CustomerDto(customerEntity) (only if you have control on your Dto - not generated ie. code-first)
        // 4 NEVER: customer.toDto(); // never
       return new CustomerDto(customer);
    }


    public void register(CustomerDto dto) {
        Customer customer = customerMapper.toEntity(dto);

        //validation in DB
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }

        registerCustomerService.register(customer);
        quotationService.quoteCustomer(customer);
        notificationService.sendRegistrationEmail(customer.getEmail());
    }


}
