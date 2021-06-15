package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.entity.Customer;
import victor.training.clean.entity.Email;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.repo.SiteRepo;
import victor.training.clean.service.QuotationService;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerFacade {
	private final CustomerRepo customerRepo;
	private final EmailSender emailSender;
	private final SiteRepo siteRepo;
	private final CustomerSearchRepo customerSearchRepo;
	private final QuotationService quotationService;

	public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
		return customerSearchRepo.search(searchCriteria);
	}

	public CustomerDto findById(long customerId) {
		Customer customer = customerRepo.findById(customerId).get();
		CustomerDto dto = new CustomerDto();
		dto.name = customer.getName();
		dto.email = customer.getEmail();
		dto.creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
		dto.id = customer.getId();
		return dto;
	}

	public void register(CustomerDto dto) {
		Customer customer = new Customer();
		customer.setEmail(dto.email);
		customer.setName(dto.name);
		customer.setSite(siteRepo.getOne(dto.siteId));

		if (customer.getName().trim().length() < 5) {
			throw new IllegalArgumentException("Name too short");
		}

		if (customerRepo.existsByEmail(customer.getEmail())) {
			throw new IllegalArgumentException("Email already registered");
		}

		// Heavy business logic
		// Heavy business logic
		// Heavy business logic
		int discountPercentage = 3;
		if (customer.isGoldMember()) {
			discountPercentage += 1;
		}
		System.out.println("Biz Logic with discount " + discountPercentage);
		// Heavy business logic
		// Heavy business logic
		customerRepo.save(customer);
		// Heavy business logic

		quotationService.requoteCustomer(customer);

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
