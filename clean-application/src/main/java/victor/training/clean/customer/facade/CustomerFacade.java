package victor.training.clean.customer.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.Facade;
import victor.training.clean.customer.model.Customer;
import victor.training.clean.customer.facade.dto.CustomerDto;
import victor.training.clean.customer.facade.dto.CustomerSearchCriteria;
import victor.training.clean.customer.facade.dto.CustomerSearchResult;
import victor.training.clean.customer.model.Email;
import victor.training.clean.customer.service.RegisterCustomerService;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.customer.repo.CustomerSearchRepo;
import victor.training.clean.customer.repo.SiteRepo;
import victor.training.clean.insurance.service.QuotationService;

import java.time.format.DateTimeFormatter;
import java.util.List;

//@Service
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
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        // TODO move mapping logic somewhere else
//        return new CustomerDto(customer);//ok
//        return customer.toDto(); // do not pollute Customer class Entity with Dto !
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

        registerCustomerService.register(customer);

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
