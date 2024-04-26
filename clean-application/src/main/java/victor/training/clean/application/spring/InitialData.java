package victor.training.clean.application.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CountryRepo;
import victor.training.clean.domain.repo.CustomerRepo;

import java.time.LocalDate;

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
    System.out.println("Saved customer with id " + id);
  }

}
