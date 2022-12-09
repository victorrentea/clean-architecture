package victor.training.clean.crm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.crm.domain.entity.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
