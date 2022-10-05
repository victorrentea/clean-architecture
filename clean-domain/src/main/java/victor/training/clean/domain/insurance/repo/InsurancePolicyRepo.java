package victor.training.clean.domain.insurance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.insurance.model.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}
