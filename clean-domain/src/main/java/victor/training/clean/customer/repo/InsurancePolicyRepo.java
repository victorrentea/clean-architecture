package victor.training.clean.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.customer.entity.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}
