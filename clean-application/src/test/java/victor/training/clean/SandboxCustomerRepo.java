package victor.training.clean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;

import java.util.List;

/**
 * Test sandbox repository placed next to the ArchUnit test as requested.
 * Contains two variants of a "search" method: one returning a DTO (good)
 * and one returning an entity (bad) used to exercise the rule manually.
 */
public interface SandboxCustomerRepo extends JpaRepository<Customer, Long> {
  // Example that follows ADR-007 (returns DTO)
  @Query("SELECT new victor.training.clean.application.dto.CustomerDto(id,name) FROM Customer")
  List<CustomerDto> searchAsDto();

  // Intentionally bad example (returns entity) used to exercise the ArchUnit rule
  @Query("SELECT c FROM Customer c")
  List<Customer> searchAsEntity();
}
