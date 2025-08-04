package victor.training.clean.in.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import victor.training.clean.in.rest.dto.CustomerDto;
import victor.training.clean.core.model.Customer;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
  @Mapping(target = "createdDate", source = "createdDate", dateFormat = "yyyy-MM-dd")
  @Mapping(target = "countryId", source = "country.id")

  // Customer#getLegalEntity() returns Optional<>
  @Mapping(target = "legalEntityCode", source = "legalEntityCode", qualifiedByName = "unwrapOpt")
  CustomerDto toDto(Customer customer);

  @Named("unwrapOpt")
  default <T> T unwrapOpt(Optional<T> optional) {// OMG
    return optional.orElse(null);
  }
}
