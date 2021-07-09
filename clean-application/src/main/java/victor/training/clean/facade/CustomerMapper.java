package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.customer.repo.SiteRepo;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
   private final SiteRepo siteRepo;

   public Customer toEntity(CustomerDto dto) {
      Customer customer = new Customer();
      customer.setEmail(dto.email);
      customer.setName(dto.name);
      customer.setSite(siteRepo.getOne(dto.siteId));
      return customer;
   }
}
