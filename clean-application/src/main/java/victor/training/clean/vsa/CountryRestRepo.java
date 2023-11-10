package victor.training.clean.vsa;

import lombok.*;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import victor.training.clean.application.entity.Country;

import java.util.List;

// Blasphemy: expose CRUD directly from the DB - example of how VSA enables different architecture per use-case
@RepositoryRestResource // generally avoid ⚠️
public interface CountryRestRepo extends PagingAndSortingRepository<Country, Long>, JpaRepository<Country, Long> {
    List<Country> findByName(@Param("name") String name);
}

@Component
class DontAutoExposeRestRepos implements RepositoryRestConfigurer {
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
        // config.setExposeRepositoryMethodsByDefault(false);
    }
}

@Component
@RequiredArgsConstructor
class InitialCountries {
    private final CountryRestRepo countryRestRepo;
    @EventListener(ApplicationStartedEvent.class)
    public void insert() {
        countryRestRepo.save(new Country("Romania", "RO"));
        countryRestRepo.save(new Country("France", "FR"));
    }
}


