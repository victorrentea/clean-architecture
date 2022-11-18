package victor.training.clean.customer.application;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.CustomerMapper;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.common.domain.model.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.customer.domain.service.CustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.customer.domain.repo.SiteRepo;
import victor.training.clean.insurance.domain.service.QuotationService;

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
    private final CustomerMapper customerMapper;

    @Operation(description = "Customer Search")
    @PostMapping("search")
    public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long customerId) {
        return new CustomerDto(customerRepo.findById(customerId).orElseThrow());
    }

    // [PERHAPS] put the @PostMapping also
    // maybe @Transactional

    public void register(Customer customer) {
//        Customer customer = customerMapper.mapToEntity(dto);

        // i want my Dto in my AS because there are usecases for which the dto coming in
        // does not cleanly map only to ONE domain object. -> make me create more Value Objects in my Domain

        validate(customer);

        customerService.register(customer);

        // orchestration from "above"
//        quotationService.quoteCustomer(customer.getId());

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
//