package victor.training.clean.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.user.entity.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
