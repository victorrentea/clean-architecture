package victor.training.clean.application;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.service.EmailService;

import java.util.List;

import static java.util.Objects.requireNonNull;
@RestController
@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailService emailSender;
    private final CustomerSearchRepo customerSearchRepo;
    private final InsuranceApplicationService insuranceService;
    private final RegisterCustomerService customerService;
    private final NotificationService notificationService;
//    EmailSenderInternalSmtp

    // SCOPUL unui ApplicationService este sa orchestreze use-caseurile functionand ca un Facade
    // un Facade design pattern prin def isi ia multa cuplare cu altii ca sa-i scuteasca pe altii sa stie de fratii lor

    // De obicei in Applicaton service gasesti mai multe use-caseuri
    // ALTERNATIVA : Vertical Slice Architecture (VSA) : 1 endpoint / clasa

//    private final CustomerDomainService customerDomainService;

    @Operation(description = "Search Customer")
    @PostMapping("customer/search")

    public List<SearchCustomerResponse> search(@RequestBody SearchCustomerCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
        // Use-Case Optimized Query: daca ai de tras cateva date (search) > nu incarca entitatea toata ("Table") ci scoti din query strict cele 5 campuri de care ai nevoie
        // eviti sa intri in domeniu
    }

    // DTO = API model, leaga Bounded Contexte diferite (app)
            // daca le schibi, te cauta clientii cu sapa
    //   parte din OpenAPI/swagger
    // vs VO care traiesc doar in Domain
    @GetMapping("customer/{id}")
    public CustomerDto findById(@PathVariable long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();
        return new CustomerDto(customer);
    }

    @Timed
    @Transactional
//    @Secured("ROLE_ADMIN")
    @PostMapping("customer")
    public void register(@RequestBody @Validated CustomerDto dto) {
        Customer customer = dto.toEntity();
        customerService.register(customer);
        notificationService.sendWelcomeEmail(customer);
    }

    @Transactional
    @PutMapping("customer/{id}")
    public void update(@PathVariable long id, @RequestBody CustomerDto dto) {
//        public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
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
