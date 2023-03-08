package victor.training.clean.verticalslice;

import lombok.*;
import org.springdoc.core.providers.RepositoryRestConfigurationProvider;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

// Brutal example of vertical slicing to enable flexible architecture per use-case: here- the lack of it :)
@RepositoryRestResource
public interface CountryCRUD extends PagingAndSortingRepository<Country, Long> {
    List<Country> findByName(@Param("name") String name);
}

@Component
class DontAutoExposeRepos implements RepositoryRestConfigurer {
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
        // config.setExposeRepositoryMethodsByDefault(false);
    }
}
@Entity
@Getter
@Setter
class Country {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String iso;
    public Country() {}

    public Country(String name, String iso) {
        this.name = name;
        this.iso = iso;
    }
}

@Component
@RequiredArgsConstructor
class InitialCountries {
    private final CountryCRUD countryCRUD;
    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        countryCRUD.save(new Country("Romania", "RO"));
        countryCRUD.save(new Country("France", "FR"));
    }
}
