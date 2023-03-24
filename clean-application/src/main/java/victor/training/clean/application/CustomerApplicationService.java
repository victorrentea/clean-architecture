package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.model.Site;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
//@Service
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class  CustomerApplicationService{ // aka facade, orchestrator
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomerService registerCustomer;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    @GetMapping("{id}")
    public CustomerDto findById(@PathVariable long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();
//        Customer customer = customerService.findById(id).orElseThrow();

        // mapping logic TODO move somewhere else
       return CustomerDto.builder()
               .id(customer.getId())
               .name(customer.getName())
               .email(customer.getEmail())
               .siteId(customer.getSite().getId())
               .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
               .build();
    }

    @Transactional
    public void register(CustomerDto dto) { // TODO use different models for read vs write (Lite CQRS)
        Customer customer = new Customer(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setCreationDate(LocalDate.now());
        customer.setSite(new Site().setId(dto.getSiteId()));

        // validation TODO explore alternatives
        if (customer.getName().length() < 5) {
            throw new IllegalArgumentException("Name too short");
        }
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }

        registerCustomer.register(customer);

        sendRegistrationEmail(customer);
    }



    public void update(long id, CustomerDto dto) { // TODO move to Task-based Commands
        Customer customer = customerRepo.findById(id).orElseThrow();
        // CRUD part
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setSite(new Site().setId(dto.getSiteId()));

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
