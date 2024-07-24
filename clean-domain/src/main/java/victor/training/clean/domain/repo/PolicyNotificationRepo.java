package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.InsurancePolicy;
import victor.training.clean.domain.model.PolicyNotification;

public interface PolicyNotificationRepo extends JpaRepository<PolicyNotification, Long> {
}
