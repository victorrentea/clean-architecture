package victor.training.clean.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.app.model.PolicyNotification;

public interface PolicyNotificationRepo extends JpaRepository<PolicyNotification, Long> {
}
