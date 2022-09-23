package victor.training.clean.facade;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.facade.dto.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
    @Mapping(target = "creationDateStr", source = "creationDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "siteId", source = "site.id")
    CustomerDto toDto(Customer customer);
}
