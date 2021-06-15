package victor.training.clean.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.user.model.Email;

public interface EmailRepo extends JpaRepository<Email, Long> {
}
