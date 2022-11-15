package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
