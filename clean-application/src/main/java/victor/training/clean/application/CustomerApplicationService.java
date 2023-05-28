package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.*;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.service.QuotationService;
import victor.training.clean.infra.AnafClient;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.requireNonNull;

@ApplicationService // custom annotation refining the classic @Service
@RequiredArgsConstructor // generates constructor for all 'private final' fields
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final AnafClient anafClient;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();

        // long & boring mapping logic TODO move somewhere else
        return CustomerDto.builder()
            .id(customer.getId())
            .name(customer.getName())
            .email(customer.getEmail())
            .countryId(customer.getCountry().getId())
            .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .gold(customer.isGoldMember())
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
        // Heavy business logic
        // TODO Where can I move this bit of domain logic? (using the state of a single Entity👑)
        int discountPercentage = 3;
        if (customer.isGoldMember()) {
            discountPercentage += 1;
        }
        System.out.println("Domain Logic using discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
        customerRepo.save(customer);
        quotationService.quoteCustomer(customer);
        sendWelcomeEmail(customer);
    }
    private String normalize(String s) {
        return s.toLowerCase().replace("\\s+", "");
    }

    @Transactional
    public void update(long id, CustomerDto dto) { // TODO move from CRUD-based API to fine-grained Task-based Commands
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
        email.setBody("Here are your perks: ...");
        emailSender.sendEmail(email);
    }
    private void auditGoldMemberRemoval(Customer customer, String reason) {
        // stuff...
        System.out.println("Kafka.send ( {name:" + customer.getName() + ", reason:" + reason + "} )");
    }
}
