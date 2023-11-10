package victor.training.clean.in.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import victor.training.clean.application.entity.Customer;
import victor.training.clean.in.rest.dto.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
    @Mapping(target = "createdDateStr", source = "createdDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "countryId", source = "country.id")
    CustomerDto toDto(Customer customer);

}
