package victor.training.clean.domain.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.customer.model.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
