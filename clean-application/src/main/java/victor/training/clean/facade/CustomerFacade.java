package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import victor.training.clean.common.Facade;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.entity.Email;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.customer.service.RegisterCustomerService;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.insurance.service.QuotationService;
import victor.training.clean.repo.CustomerSearchRepo;

import java.util.List;

@Facade
@Transactional
@RequiredArgsConstructor
public class CustomerFacade {
	private final CustomerRepo customerRepo;
	private final EmailSender emailSender;
	private final CustomerSearchRepo customerSearchRepo;
	private final QuotationService quotationService;
	private final CustomerMapper customerMapper;
	private final RegisterCustomerService registerCustomerService;

	public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
		return customerSearchRepo.search(searchCriteria);
	}

	public CustomerDto findById(long customerId) {
		Customer customer = customerRepo.findById(customerId).get();
		return new CustomerDto(customer);
	}

	public void register( @Validated CustomerDto dto) {
		Customer customer = customerMapper.toEntity(dto);

		if (customerRepo.existsByEmail(customer.getEmail())) {
			throw new IllegalArgumentException("Email already registered");
		}

		Long customerId = registerCustomerService.register(customer);

		quotationService.doQuote(customerId);

		sendRegistrationEmail(customer.getEmail());
	}



	private void sendRegistrationEmail(String emailAddress) {
		System.out.println("Sending activation link via email to " + emailAddress);
		Email email = new Email();
		email.setFrom("noreply");
		email.setTo(emailAddress);
		email.setSubject("Welcome!");
		email.setBody("You'll like it! Sincerely, Team");
		emailSender.sendEmail(email);
	}
}
