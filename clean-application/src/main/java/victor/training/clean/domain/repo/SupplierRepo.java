package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.Country;

public interface SupplierRepo extends JpaRepository<Country, Long> {
}
