package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.service.CustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.util.List;

//@Service
@RestController
@RequestMapping("customer")
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService implements CustomerApplicationServiceApiCuMetadate {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final CustomerService customerService;


    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    @Override
    public CustomerDto findById(long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();
        return new CustomerDto(customer);
       // 1) in ctor Dto, ca si-asa statea Dtoul degeaba
        // 2) in alta clasa mapper/ transformers.CustomerTransformer
//        return customer.toDto(); // 3) -> NU CUMVA
    }

    // ( cum testam metoda asta:
    // a) cu @Mockuri in jur - NU ASA CA TE VOR RANI TESTELE
    // b) ASA: end-to-end @SPringBootTest cu o DB in mem (H2) sau in docker (@Testcontainer)
    @Transactional
//    @PostMapping
    public void register(CustomerDto dto) { // TODO use different models for read vs write (Lite CQRS)
        Customer customer = dto.toEntity();
        customerService.register(customer);
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
            auditGoldMemberRemoval(customer, dto.getGoldMemberRemovalComment());
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
