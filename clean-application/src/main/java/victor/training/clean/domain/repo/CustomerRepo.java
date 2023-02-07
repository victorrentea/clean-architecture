package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.training.clean.domain.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	Customer getCustomerByEmail(String email);

//	@Query("SELECT c FROM Customer c WHERE c.goldMember = true")
	boolean existsByEmail(String email);

}
