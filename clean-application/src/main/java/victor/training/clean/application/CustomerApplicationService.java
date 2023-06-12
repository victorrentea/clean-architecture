package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.application.mapper.CustomerMapper;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.service.IAnafClient;

import java.util.List;

import static java.util.Objects.requireNonNull;

@RestController
//@ScaryEntity
@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final CustomerSearchRepo customerSearchRepo;
    private final InsuranceService insuranceService;
    private final IAnafClient anafClient;
    private final RegisterCustomerService registerCustomerService;

    @Operation(description = "Search Customer")
    @PostMapping("customer/search")
    public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    @GetMapping("customer/{id}")
    public CustomerDto findById(@PathVariable long id) {
        // long & boring mapping logic TODO move somewhere else
//        return customer.toDto();
//        return CustomerDto.fromEntity(customer);
        return new CustomerDto(customerRepo.findById(id).orElseThrow());
    }

    @Transactional
    @PostMapping("customer")
    public void register(@RequestBody @Validated CustomerDto dto) {
        Customer customer = CustomerMapper.asEntity(dto);
        registerCustomerService.register(customer);
        sendWelcomeEmail(customer);
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
            sendGoldBenefitsEmail(customer);
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

    private void sendWelcomeEmail(Customer customer) {
        Email email = Email.builder()
            .from("noreply@cleanapp.com")
            .to(customer.getEmail())
            .subject("Account created for")
            .body("Welcome to our world, " + customer.getName() + ". You'll like it! Sincerely, Team")
            .build();
        emailSender.sendEmail(email);
    }

    private void sendGoldBenefitsEmail(Customer customer) {
        // repeated business rule! 😱
        int discountPercentage = customer.getDiscountPercentage();
        Email email = Email.builder()
            .from("noreply@cleanapp.com")
            .to(customer.getEmail())
            .subject("Welcome to the Gold membership!")
            .body("Here are your perks: ... Enjoy your special discount of " + discountPercentage + "%")
            .build();
        emailSender.sendEmail(email);
    }

    private void auditRemovedGoldMember(String customerName, String reason) {
        log.info("Kafka.send ( {name:" + customerName + ", reason:" + reason + "} )");
    }
}
