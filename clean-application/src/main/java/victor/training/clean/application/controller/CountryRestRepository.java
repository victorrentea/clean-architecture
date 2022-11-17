package victor.training.clean.application.controller;

import lombok.*;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

// Brutal example of vertical slicing to enable flexible architecture per use-case: here- the lack of it :)
@RepositoryRestResource(collectionResourceRel = "countries", path = "countries")
public interface CountryRestRepository extends PagingAndSortingRepository<Country, Long> {
    List<Country> findByName(@Param("name") String name);
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
    private final CountryRestRepository countryRestRepository;
    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        countryRestRepository.save(new Country("Romania", "RO"));
        countryRestRepository.save(new Country("France", "FR"));
    }
}
