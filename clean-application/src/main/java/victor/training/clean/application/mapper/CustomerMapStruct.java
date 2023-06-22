package victor.training.clean.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.application.dto.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
    @Mapping(target = "createdDateStr", source = "createdDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "countryId", source = "country.id")
    CustomerDto toDto(Customer customer);
}
