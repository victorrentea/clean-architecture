package victor.training.clean.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.core.model.PolicyNotification;

public interface PolicyNotificationRepo extends JpaRepository<PolicyNotification, Long> {
}
