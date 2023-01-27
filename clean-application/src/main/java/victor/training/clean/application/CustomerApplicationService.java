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
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.util.List;
//class CustomerService {
//    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
//        return customerSearchRepo.search(searchCriteria);
//    }
//    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
//        return customerSearchRepo.search(searchCriteria);
//    }
//}
//@Service
@ApplicationService // custom annotation
@RequiredArgsConstructor
@RestController
// AKA FACADE
public class CustomerApplicationService  /*implements CustomerAPiPtOpenAPI*/ {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomerService registerCustomerService;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria); // Relaxed Layer Architecture. am voie. ca sa nu pun boilerplate in Domain Se§rvice
    }

    @GetMapping("{id}")
    public CustomerDto findById(@PathVariable long id) {
        Customer customer = customerRepo.findById(id).orElseThrow();

        //        return customer.toDto(); < niciodata pentru ca ar CUPLA ce-ai mai sfant (Entity de Domain) cu Mizeria (API)
        // mapping logic TODO move somewhere else
        // a) Mapper (autogenerat sau scris de mana) <- classic solution; merge
        // b) Dto constructor (daca ai control pe Dto = nu le generezi)
       return new CustomerDto(customer);
    }

    @Transactional
    public void register(CustomerDto dto) { // TODO use different models for read vs write (Lite CQRS)
        Customer customer = dto.toCustomer();

        registerCustomerService.register(customer);

        quotationService.quoteCustomer(customer);

        sendRegistrationEmail(customer);
    }

    @Transactional
    public void update(CustomerDto dto) { // TODO move to Task-based Commands
        Customer customer = customerRepo.findById(dto.getId()).orElseThrow();
        // CRUD part
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmailAddress());
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

//        customerRepo.save(customer); // not required by the ORM because of @Transactional
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
