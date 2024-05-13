package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import victor.training.clean.domain.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
  boolean existsByEmailIgnoreCase(String email);

  // what to keep in DB:
  // NOT NULL YES
  // UNIQUE YES
  // TRIGGERS / CHECK CONSTRAINTS rareley

  // Why should I repeat that in DB once I have them in Java ?
  // DATA INTEGRITY despite BAD DATAFIXES

  boolean existsByLegalEntityCode(String legalEntityCode);
}
