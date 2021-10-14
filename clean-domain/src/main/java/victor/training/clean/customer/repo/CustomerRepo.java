package victor.training.clean.customer.repo;

import org.springframework.stereotype.Repository;
import victor.training.clean.customer.entity.Customer;

import java.util.Optional;

public interface CustomerRepo extends Repository /*extends JpaRepository<Customer, Long> */{
	Customer getCustomerByEmail(String email);
	Optional<Customer> findById(Long id);
	boolean existsByEmail(String email);

	void save(Customer customer);

}
