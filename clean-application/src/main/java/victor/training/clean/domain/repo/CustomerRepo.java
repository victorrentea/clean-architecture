package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
  boolean existsByEmail(String email);

  boolean existsByLegalEntityCode(String legalEntityCode);
}
