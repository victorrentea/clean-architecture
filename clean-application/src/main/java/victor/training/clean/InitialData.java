package victor.training.clean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.repo.CountryRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialData {
  private final CountryRepo countryRepo;

  @EventListener(ApplicationStartedEvent.class)
  public void onStartup() {
    log.info("Loading initial data");
    countryRepo.save(new Country("USA", "United States"));
    countryRepo.save(new Country("UK", "United Kingdom"));
    countryRepo.save(new Country("FRA", "France"));
  }
}
