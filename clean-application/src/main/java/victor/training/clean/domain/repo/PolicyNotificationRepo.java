package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.insurance.PolicyNotification;

public interface PolicyNotificationRepo extends JpaRepository<PolicyNotification, Long> {
}
