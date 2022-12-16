package victor.training.clean.application;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

import java.time.format.DateTimeFormatter;
import java.util.List;

//@Service
@RestController
@RequestMapping("customer")
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;

    @Operation(description = "Customer Search")
    @PostMapping("search")

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    @GetMapping("{id}")
//    @Secured
//    @Timed
//    @Cacheable("search")
    public CustomerDto findById(@PathVariable long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

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
    @PostMapping("")
    public void register(@Validated @RequestBody CustomerDto dto) {
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

        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // TODO Where can I move this little logic? (... operating on the state of a single entity)
        int discountPercentage = 3;
        if (customer.isGoldMember()) {
            discountPercentage += 1;
        }
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
        customerRepo.save(customer);
        // Heavy business logic
        quotationService.quoteCustomer(customer);

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
