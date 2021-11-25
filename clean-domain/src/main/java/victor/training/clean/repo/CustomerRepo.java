package victor.training.clean.repo;

import org.springframework.data.repository.Repository;
import victor.training.clean.entity.Customer;

import java.util.Optional;

public interface CustomerRepo
	extends Repository<Customer, Long>
//	extends JpaRepository<Customer, Long>
{
	boolean existsByEmail(String email);
	Customer save(Customer entity);
	Optional<Customer> findById(Long aLong);
}
