package victor.training.clean.shared.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.shared.domain.model.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}
