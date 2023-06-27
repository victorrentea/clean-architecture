package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.insurance.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
  InsurancePolicy findByCustomerId(long customerId);
}
