package victor.training.clean.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import victor.training.clean.core.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
  boolean existsByEmail(String email);

  boolean existsByLegalEntityCode(String legalEntityCode);
}
