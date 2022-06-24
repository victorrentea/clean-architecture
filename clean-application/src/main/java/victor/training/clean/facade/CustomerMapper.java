package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.repo.SiteRepo;
import victor.training.clean.facade.dto.CustomerDto;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@Component
public class CustomerMapper {
    private final SiteRepo siteRepo;
    public CustomerDto toDto(Customer customer) {
       CustomerDto dto = new CustomerDto(customer);
       dto.name = customer.getName();
       dto.email = customer.getEmail();
       dto.creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
       dto.id = customer.getId();
       // can go to...
       // 1 a mapper
       // 2
       // 3
       // 4

       return dto;
    }

    public Customer toEntity(CustomerDto dto) {
       Customer customer = new Customer(dto.name);
       customer.setEmail(dto.email);
       customer.setSite(siteRepo.getById(dto.siteId));
       return customer;
    }
}
