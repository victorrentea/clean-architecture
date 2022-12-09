package victor.training.clean.crm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.crm.domain.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	Customer getCustomerByEmail(String email);

	boolean existsByEmail(String email);

}
