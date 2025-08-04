package victor.training.clean.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.core.model.Country;
import victor.training.clean.core.model.Customer;
import victor.training.clean.core.repo.CountryRepo;
import victor.training.clean.core.repo.CustomerRepo;

import static java.time.LocalDate.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialData {
  private final CustomerRepo customerRepo;
  private final CountryRepo countryRepo;

  @EventListener(ApplicationStartedEvent.class)
  @Transactional
  public void onStartup() {
    Country country = countryRepo.save(new Country());
    Long id = customerRepo.save(new Customer()
        .setName("John Doe")
        .setCountry(country)
        .setCreatedDate(now())
    ).getId();
    MDC.put("testkey", "testvalue"); // search this in application.json.log when activating example-logback.xml
    log.debug("Saved customer with id " + id);
  }

}
