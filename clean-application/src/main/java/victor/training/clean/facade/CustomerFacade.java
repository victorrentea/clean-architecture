package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import victor.training.clean.entity.Customer;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.facade.mapper.CustomerMapper;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.repo.CustomerSearchRepo;
import victor.training.clean.service.CustomerService;
import victor.training.clean.service.NotificationService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerFacade {
	private final CustomerRepo customerRepo;
	private final CustomerSearchRepo customerSearchRepo;
	private final CustomerMapper customerMapper;
	private final CustomerService customerService;
	private final NotificationService notificationService;

//	public CustomerDto search(Long id) {
//		return new CustomerDto(customerRepo.findById(id).get());
//	}
	public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
		return customerSearchRepo.search(searchCriteria);
	}

	public CustomerDto findById(long customerId) {
		Customer entity = customerRepo.findById(customerId).get();
		return new CustomerDto(entity);
	}

	public void register(@Validated CustomerDto dto) {
		Customer customer = customerMapper.toEntity(dto);
//		Customer customer = dto.toEntity(); // valid

		if (customer.getName().trim().length() < 5) {
			throw new IllegalArgumentException("Name too short");
		}

		if (customerRepo.existsByEmail(customer.getEmail())) {
			throw new IllegalArgumentException("Email already registered");
		}

		customerService.register(customer);
	}



}

