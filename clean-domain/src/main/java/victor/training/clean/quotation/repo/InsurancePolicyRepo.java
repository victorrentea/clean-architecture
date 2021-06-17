package victor.training.clean.quotation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.quotation.entity.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}
