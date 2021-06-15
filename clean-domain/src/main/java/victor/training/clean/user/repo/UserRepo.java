package victor.training.clean.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.user.model.User;
import victor.training.clean.user.model.User.UserId;

public interface UserRepo extends JpaRepository<User, UserId> {
}
