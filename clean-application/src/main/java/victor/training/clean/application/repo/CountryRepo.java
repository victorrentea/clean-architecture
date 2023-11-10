package victor.training.clean.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.application.entity.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {
}
