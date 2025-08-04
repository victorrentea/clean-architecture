package victor.training.clean.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.core.model.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {
}
