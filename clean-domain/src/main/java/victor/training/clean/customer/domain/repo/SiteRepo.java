package victor.training.clean.customer.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.customer.domain.model.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
