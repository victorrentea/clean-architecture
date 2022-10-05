package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.user.model.Email;
import victor.training.clean.domain.customer.service.RegisterCustomerService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.domain.customer.repo.SiteRepo;
import victor.training.clean.domain.insurance.service.QuotationService;

import java.util.List;

//@Service
@Slf4j
@Facade
@Transactional
@RequiredArgsConstructor
public class CustomerFacade {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomerService registerCustomerService;

    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

    public CustomerDto findById(long customerId) {
        return new CustomerDto(customerRepo.findById(customerId).orElseThrow());
    }

    public void register(CustomerDto dto) {
        Customer customer = new Customer(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setSite(siteRepo.getById(dto.getSiteId()));

        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
            // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
        }
        customerRepo.save(customer);
        log.info("Persist the customer");

        registerCustomerService.registerCustomer(customer);


        // Heavy business logic
//        quotationService.quoteCustomer(customer);

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
