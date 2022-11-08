package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.facade.dto.CustomerDto;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final SiteRepo siteRepo;
    Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setSite(siteRepo.getById(dto.getSiteId()));
        return customer;
    }
}
