package victor.training.clean.insurance.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.insurance.domain.model.PolicyNotification;

public interface PolicyNotificationRepo extends JpaRepository<PolicyNotification, Long> {
}
