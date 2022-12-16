package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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

//@Service
@RestController
@RequestMapping("customer")
@ApplicationService // custom annotation
@RequiredArgsConstructor
public class CustomerApplicationService implements CustomerRestAPI {
    private final CustomerRepo customerRepo;
    private final EmailSender emailSender;
    private final SiteRepo siteRepo;
    private final CustomerSearchRepo customerSearchRepo;
    private final QuotationService quotationService;
    private final RegisterCustomerService registerCustomerServiceDegeabaInterface;

    @Override
    public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
        return customerSearchRepo.search(searchCriteria);
    }

//        Customer customer = customerService.findById(customerId).orElseThrow();
    @Override
    public CustomerDto findById(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        // mapping logic TODO move somewhere else - principala intrebare pe care ti-o pui fiecare ora intr-un ApplicationService
        // - intr-un mapper (scris al mano sau generat)
//       return customer.toDto(); // NICIODATA !!!!!!! pentru ca poluezi domeniul cu 'presentation' concern - M VC
        return new CustomerDto(customer);
    }

    @Override
    @Transactional
    public void register(CustomerDto dto) {
        Customer customer = new Customer();
        // TODO move undeva maparea
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setSite(siteRepo.getById(dto.getSiteId()));

        registerCustomerServiceDegeabaInterface.register(customer);
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
