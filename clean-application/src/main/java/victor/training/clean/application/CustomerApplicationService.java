package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;
import victor.training.clean.common.Facade;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.service.CustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.time.format.DateTimeFormatter;
import java.util.List;

//@Service
@RequestMapping("customer")
@RestController
@Transactional // probably too broad for high-TPS systems
@RequiredArgsConstructor
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final CustomerService customerService;

    @Operation(description = "Customer Search")
    @PostMapping("search")
    public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long customerId) {

        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // TODO move mapping logic somewhere else
       return CustomerDto.builder()
               .id(customer.getId())
               .name(customer.getName())
               .email(customer.getEmail())
               .siteId(customer.getSite().getId())
               .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
               .build();
    }

    public void register(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setSite(siteRepo.getById(dto.getSiteId()));

        validate(customer);

        customerService.register(customer);

        quotationService.quoteCustomer(customer);

        sendRegistrationEmail(customer.getEmail());
    }

    private void validate(Customer customer) {
        // TODO experiment all the ways to do validation
        if (customer.getName().length() < 5) {
            throw new IllegalArgumentException("Name too short");
        }
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }
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
