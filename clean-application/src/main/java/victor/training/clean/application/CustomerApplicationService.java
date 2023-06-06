package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.domain.service.InsuranceService;
import victor.training.clean.infra.AnafClient;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j // ❤️Lombok adds private static final Logger log = LoggerFactory.getLogger(CustomerApplicationService.class);
@RequiredArgsConstructor // ❤️Lombok generates constructor including all 'private final' fields
@ApplicationService // custom annotation refining the classic @Service
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final CustomerSearchRepo customerSearchRepo;
    private final InsuranceService insuranceService;
    private final AnafClient anafClient;

    public List<SearchCustomerResponse> search(SearchCustomerCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();

        // Small domain logic operating on the state of a single Entity.
        // TODO Where can I move it? PS: it's repeating somewhere else
        int discountPercentage = 1;
        if (customer.isGoldMember()) {
            discountPercentage += 3;
        }

        // long & boring mapping logic TODO move somewhere else
        return CustomerDto.builder()
            .id(customer.getId())
            .name(customer.getName())
            .email(customer.getEmail())
            .countryId(customer.getCountry().getId())
            .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .gold(customer.isGoldMember())
            .discountPercentage(discountPercentage)
            .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
            .legalEntityCode(customer.getLegalEntityCode())
            .discountedVat(customer.isDiscountedVat())
            .build();
    }

    @Transactional
    public void register(CustomerDto dto) { // TODO use separate DTOs for POST vs GET
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setCreationDate(LocalDate.now());
        customer.setCountry(new Country().setId(dto.getCountryId()));
        customer.setLegalEntityCode(dto.getLegalEntityCode());

        // input validation
        if (customer.getName().length() < 5) { // TODO other places to move this validation to?
            throw new IllegalArgumentException("The customer name is too short");
        }

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
    }
    private String normalize(String s) {
        return s.toLowerCase().replace("\\s+", "");
    }

    @Transactional
    public void update(long id, CustomerDto dto) { // TODO move to fine-grained Task-based Commands
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

        customerRepo.save(customer); // not actually required within a @Transactional method if using ORM(JPA/Hibernate)
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
        int discountPercentage = 1;
        if (customer.isGoldMember()) {
            discountPercentage += 3;
        }
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
