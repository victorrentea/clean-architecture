package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;

import java.util.List;

import static java.util.Objects.requireNonNull;

@RestController
@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
//public class CustomerApplicationService implements CustomerApplicationServiceApi {
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomerService registerCustomerService;
    private final victor.training.clean.application.NotificationService notificationService;

    @PostMapping("customer/search")
    public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
//        extraInput = domainService.call()
        return customerSearchRepo.search(searchCriteria/*, extraInput*/);
    }

    @GetMapping("customer/{id}")
    public CustomerDto findById(@PathVariable long id) {
        // option 1: AppService -> Repo (
        Customer customer = customerRepo.findById(id).orElseThrow();

        // option 2 : AppService->DomainService->Repo

        // Small domain logic operating on the state of a single Entity.
        // TODO Where can I move it? PS: it's repeating somewhere else

        // long & boring mapping logic TODO move somewhere else
        return new CustomerDto(customer);
        // 1) manual mapper eg CustomerMapper
        // 2) MapStruct (bad habbit?)
        // 3) ❤️
    }

    @Transactional
    @PostMapping("customer")
    public void register(@RequestBody @Validated CustomerDto dto) {
        Customer customer = dto.asEntity();
        registerCustomerService.register(customer);
        notificationService.sendWelcomeEmail(customer);
    }




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
