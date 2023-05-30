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
import victor.training.clean.domain.model.AnafResult;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.infra.AnafClient;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.client.NotificationService;

import java.util.List;

import static java.util.Objects.requireNonNull;

@RestController
@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
//public class CustomerApplicationService implements CustomerApplicationServiceApi {
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final NotificationService emailSender;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final AnafClient anafClient;

    @PostMapping("customer/search")
    public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    @GetMapping("customer/{id}")
    public CustomerDto findById(@PathVariable long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();
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

        // business rule
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("A customer with this email is already registered!");
            // throw new CleanException(CleanException.ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }

        // enrichment from external API in 'application' layer
        if (customer.getLegalEntityCode() != null) {
            if (customerRepo.existsByLegalEntityCode(customer.getLegalEntityCode())) {
                throw new IllegalArgumentException("Company already registered");
            }
            AnafResult anafResult = anafClient.query(customer.getLegalEntityCode());
            if (anafResult == null || !normalize(customer.getName()).equals(normalize(anafResult.getName()))){
                throw new IllegalArgumentException("Legal Entity not found!");
            }
            if (anafResult.isVatPayer()) {
                customer.setDiscountedVat(true);
            }
        }
        log.info("More Business Logic (imagine)");
        log.info("More Business Logic (imagine)");

        customerRepo.save(customer);
        sendWelcomeEmail(customer);
        // vs emailsToSend.save(new EmailToSend()); + COMMIT , then = transaction outbox pattern
        // a) scheduler polls for this
        // b) debezium/kafka connect pulls this data out on a Kafka Topic
    }

    private String normalize(String s) {
        return s.toLowerCase().replace("\\s+", "");
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
            auditGoldMemberRemoval(customer, dto.getGoldMemberRemovalReason());
        }

        customerRepo.save(customer); // not actually required within a @Transactional method if using ORM(JPA/Hibernate)
        quotationService.customerDetailsChanged(customer);
    }

    private void sendWelcomeEmail(Customer customer) {
        Email email = new Email();
        email.setFrom("noreply@cleanapp.com");
        email.setTo(customer.getEmail());
        email.setSubject("Account created for");
        email.setBody("Welcome to our world, "+ customer.getName()+". You'll like it! Sincerely, Team");
        emailSender.sendEmail(email);
    }

    private void sendGoldBenefitsEmail(Customer customer) {
        Email email = new Email();
        email.setFrom("noreply@cleanapp.com");
        email.setTo(customer.getEmail());
        email.setSubject("Welcome to the Gold membership!");
        int discountPercentage = customer.getDiscountPercentage();
        email.setBody("Here are your perks: ... Enjoy your special discount of " + discountPercentage + "%");
        emailSender.sendEmail(email);
    }
    private void auditGoldMemberRemoval(Customer customer, String reason) {
        // [imagine]
        System.out.println("Kafka.send ( {name:" + customer.getName() + ", reason:" + reason + "} )");
    }
}
