package victor.training.clean.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.app.model.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {
}
