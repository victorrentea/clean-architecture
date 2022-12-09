package victor.training.clean.insurance.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.insurance.domain.entity.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}
