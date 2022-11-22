package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.ApplicationService;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.shared.domain.model.Email;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.customer.domain.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.domain.repo.SiteRepo;
import victor.training.clean.shared.domain.service.QuotationService;

//@Service
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService { // ± handler/orchestrator
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
//    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final CustomerValidator customerValidator;

    // use-case optimized query: select Dtos from queries directly
//    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
//        return customerSearchRepo.search(searchCriteria);
//    }

    public CustomerDto findById(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // mapping logic TODO move somewhere else to reduce the size of the facade
        // 💡 mapper hand written or  MapStruct
        //       return mapper.toDto(customer);

        // WRONG: return customer.toDto(); // NO !

        // 💡 OK. It's never a problem to depend on your domain:
        return new CustomerDto(customer);
    }

    @Transactional
    public void register(CustomerDto dto) {
        Customer customer = new Customer(dto.getName());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setSite(siteRepo.getById(dto.getSiteId()));

        // DDD: The Domain Model (JDO) data structures shoud enforce their own consistency.

        // risky to 'remember to validate'
        // it's risky to enforce validity of your domain OUTSIDE of the MODEL
        customerValidator.validate(customer);

        customer = registerCustomerService.register(customer,dto.customerId,dto.cuponId);
        customerRepo.save(customer);

//        quotationService.quoteCustomer(customer);



        sendRegistrationEmail(customer.getEmail());
    }

    // The orchestrator.
    // the facade that is telling other services what to do

    // more coupling in the orchestrator = OK
    // less coupling in the service containing domain complexity

    private final RegisterCustomerService registerCustomerService;


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
