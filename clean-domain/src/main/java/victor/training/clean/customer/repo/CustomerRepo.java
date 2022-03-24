package victor.training.clean.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.customer.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	Customer getCustomerByEmail(String email);

//	@Query("SELECT s FROM Customer x")
	boolean existsByEmail(String email);
}
