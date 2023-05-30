package victor.training.clean.insurance.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.crm.domain.model.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {
}
