package victor.training.clean.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.application.entity.PolicyNotification;

public interface PolicyNotificationRepo extends JpaRepository<PolicyNotification, Long> {
}
