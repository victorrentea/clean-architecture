package victor.training.clean.shared.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.shared.domain.model.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
