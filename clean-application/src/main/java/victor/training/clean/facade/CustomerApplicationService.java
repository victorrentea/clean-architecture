package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.CustomerName;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.RegisterCustomerService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.domain.service.QuotationService;

import java.util.List;

//@Service
@Facade
@Transactional // probably too broad for high-TPS systems
@RequiredArgsConstructor
public class CustomerApplicationService {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomerService registerCustomer;

//    @GetMapping("{id}") // merging controller with the first class with logic beanth it
//    is only doable if you expose a single CHANNEL (eg REST)
    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        // to search there were 2 optiojns:
        // 1) fetch Customer Entity from DB
        // 2) (performance) only fetch 2 string that you display, no 20 filds + 2 JOINs
        // #2 wins = "Use-Case Optimized Query" - for READING dta only

        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // TODO move mapping logic somewhere else
        // - manual mapper
        // - automapper (mapStruct)
        // - inside the Dto
//        return customer.toDto();// NEVER: polluting entity with presentation - API model
       return new CustomerDto(customer); // ok for API models to depend on Domain objects !!!
    }

//    public void customerGetsMarried(Customer customer, String lastNameOfHim) {
//        customer.setName(lastNameOfHim);
//    }

    public void register(CustomerDto dto) {
        //mapping : if big> move out
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(new CustomerName(dto.getName()));
        customer.setSite(siteRepo.getById(dto.getSiteId()));

        // validation
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }

        registerCustomer.register(customer);


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
