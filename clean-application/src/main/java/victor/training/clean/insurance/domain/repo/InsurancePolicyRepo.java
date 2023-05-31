package victor.training.clean.insurance.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.insurance.domain.model.InsurancePolicy;

import java.util.Optional;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
  InsurancePolicy findByCustomerId(long customerId);

  boolean existsByCustomerName(String name);
}
