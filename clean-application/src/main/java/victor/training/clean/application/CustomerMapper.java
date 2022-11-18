package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.SiteRepo;

@RequiredArgsConstructor
@Component
public class CustomerMapper {
  private final SiteRepo siteRepo;

  public Customer mapToEntity(CustomerDto dto) {
    Customer customer = new Customer();
    customer.setEmail(dto.getEmail());
    customer.setName(dto.getName());
    customer.setSite(siteRepo.getById(dto.getSiteId()));
    return customer;
  }
}