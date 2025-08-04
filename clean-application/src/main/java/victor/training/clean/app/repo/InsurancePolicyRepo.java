package victor.training.clean.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.app.model.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
  InsurancePolicy findByCustomerId(long customerId);
}
