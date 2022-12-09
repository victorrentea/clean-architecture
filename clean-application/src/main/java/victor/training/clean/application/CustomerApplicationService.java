package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.crm.domain.entity.Customer;
import victor.training.clean.common.entity.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.crm.domain.service.RegisterCustomer;
import victor.training.clean.insurance.domain.service.QuotationService;
import victor.training.clean.common.infra.EmailSender;
import victor.training.clean.crm.domain.repo.CustomerRepo;
import victor.training.clean.crm.domain.repo.SiteRepo;

import java.util.List;

//@Service
@ApplicationService // custom annotation
@RestController
@RequestMapping("customer")

@RequiredArgsConstructor
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomer registerCustomer;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    @GetMapping("{id}")
    public CustomerDto findById(@PathVariable long customerId) {
        return new CustomerDto(customerRepo.findById(customerId).orElseThrow());
    }

    @Transactional
    public void register( CustomerDto dto) {
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setSite(siteRepo.getById(dto.getSiteId()));

        // TODO experiment all the ways to do validation
        if (customer.getName().length() < 5) {
            throw new IllegalArgumentException("Name too short");
        }
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }

        registerCustomer.register(customer);
        quotationService.quoteCustomer(customer.getId());

        sendRegistrationEmail(customer.getEmail());
    }


    private void sendRegistrationEmail(String emailAddress) {
        System.out.println("Sending activation link via email to " + emailAddress);
        Email email = new Email();
        email.setFrom("noreply");
        email.setTo(emailAddress);
        email.setSubject("Welcome");
        email.setBody("You'll like it! Sincerely, Team");
        emailSender.sendEmail(email);
    }
}
