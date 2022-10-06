package victor.training.clean.facade;

import victor.training.clean.domain.model.Customer;
import victor.training.clean.facade.dto.CustomerDto;

import java.time.format.DateTimeFormatter;

public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .siteId(customer.getSite().getId())
                .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
