package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.facade.dto.CustomerDto;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    public final SiteRepo siteRepo;


    public Customer fromDto(CustomerDto dto) {
        Customer customer = new Customer(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setSite(siteRepo.getById(dto.getSiteId()));
        return customer;
    }
}