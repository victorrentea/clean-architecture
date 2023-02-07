package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@Service
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // mapping logic TODO move somewhere else
       return new CustomerDto(customer);
       // 1) in ctor Dto, ca si-asa statea Dtoul degeaba
        // 2) in alta clasa mapper/ transformers.CustomerTransformer
//        return customer.toDto(); // 3) -> NU CUMVA
    }

    @Transactional
    public void register(CustomerDto dto) { // TODO use different models for read vs write (Lite CQRS)
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setCreationDate(LocalDate.now());
        customer.setSite(siteRepo.getReferenceById(dto.getSiteId()));

        // validation TODO explore alternatives
//        if (customer.getName().length() < 5) {
//            throw new IllegalArgumentException("Name too short");
//        }
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }

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
        quotationService.quoteCustomer(customer);

        sendRegistrationEmail(customer);
    }

    public void method(Customer customer) {

        int discountPercentage = customer.getDiscountPercentage();
        System.out.println("Biz Logic with discount " + discountPercentage);

    }

    public void update(CustomerDto dto) { // TODO move to Task-based Commands
        Customer customer = customerRepo.findById(dto.getId()).orElseThrow();
        // CRUD part
        if (customer.getName().length() < 5) {
            throw new IllegalArgumentException("Name too short");
        }
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setSite(siteRepo.getReferenceById(dto.getSiteId()));

        // tricky part
        if (!customer.isGoldMember() && dto.isGold()) {
            customer.setGoldMember(true);
            sendGoldWelcomeEmail(customer);
        }
        if (customer.isGoldMember() && !dto.isGold()) {
            // TODO api call get order history, and check stuff..
            customer.setGoldMember(false);
            auditGoldMemberRemoval(customer, dto.getGoldMemberRemovalReason());
        }

        customerRepo.save(customer); // not required by the ORM because of @Transactional
    }

    private void sendRegistrationEmail(Customer customer) {
        Email email = new Email();
        email.setFrom("noreply@cleanapp.com");
        email.setTo(customer.getEmail());
        email.setSubject("Account created for");
        email.setBody("Welcome to our world, "+ customer.getName()+". You'll like it! Sincerely, Team");
        emailSender.sendEmail(email);
    }

    private void sendGoldWelcomeEmail(Customer customer) {
        Email email = new Email();
        email.setFrom("noreply@cleanapp.com");
        email.setTo(customer.getEmail());
        email.setSubject("Welcome to the Gold membership!");
        email.setBody("Here are your perks: ...");
        emailSender.sendEmail(email);
    }
    private void auditGoldMemberRemoval(Customer customer, String reason) {
        System.out.println("Send message on Kafka: name:" + customer.getName() + ", reason:" + reason);
    }
}
