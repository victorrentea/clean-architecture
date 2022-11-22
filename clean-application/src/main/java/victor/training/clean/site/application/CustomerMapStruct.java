package victor.training.clean.site.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.site.application.dto.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
    @Mapping(target = "creationDateStr", source = "creationDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "siteId", source = "site.id")
    CustomerDto toDto(Customer customer);
}
