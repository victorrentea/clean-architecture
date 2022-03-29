package victor.training.clean.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.customer.entity.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}
