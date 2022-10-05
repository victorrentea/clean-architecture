package victor.training.clean.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}
