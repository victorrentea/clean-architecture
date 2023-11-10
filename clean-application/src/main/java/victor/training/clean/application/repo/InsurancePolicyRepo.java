package victor.training.clean.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.application.entity.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
  InsurancePolicy findByCustomerId(long customerId);
}
