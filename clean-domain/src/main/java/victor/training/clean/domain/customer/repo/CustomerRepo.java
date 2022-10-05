package victor.training.clean.domain.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.customer.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	Customer getCustomerByEmail(String email);

	boolean existsByEmail(String email);

}
