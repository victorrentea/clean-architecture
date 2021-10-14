package victor.training.clean.facade.mapper;

import org.springframework.stereotype.Component;
import victor.training.clean.entity.Customer;
import victor.training.clean.facade.dto.CustomerDto;

import java.text.SimpleDateFormat;

@Component
public class CustomerMapper {
   public CustomerDto toDto(Customer entity) {
      CustomerDto dto = new CustomerDto();
      dto.name = entity.getName();
      dto.email = entity.getEmail();
      dto.creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(entity.getCreationDate());
      dto.id = entity.getId();
      return dto;
   }
}
