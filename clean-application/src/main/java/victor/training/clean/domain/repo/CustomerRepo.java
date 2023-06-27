package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import victor.training.clean.domain.model.customer.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
  boolean existsByEmail(String email);

  boolean existsByLegalEntityCode(String legalEntityCode);
}
