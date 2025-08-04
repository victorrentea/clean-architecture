package victor.training.clean.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.core.model.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
  InsurancePolicy findByCustomerId(long customerId);
}
