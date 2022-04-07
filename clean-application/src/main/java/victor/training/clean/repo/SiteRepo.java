package victor.training.clean.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.entity.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
