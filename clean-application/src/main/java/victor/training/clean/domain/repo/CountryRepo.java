package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.customer.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {
}
