package victor.training.clean.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.entity.Customer;
import victor.training.clean.facade.dto.CustomerDto;
import victor.training.clean.repo.SiteRepo;

@Component
public class CustomerMapper {
   @Autowired
   private SiteRepo siteRepo;

   public Customer toEntity(CustomerDto dto) {
      Customer customer = new Customer();
      customer.setEmail(dto.email);
      customer.setName(dto.name);
      customer.setSite(siteRepo.getOne(dto.siteId));
      return customer;
   }
}
