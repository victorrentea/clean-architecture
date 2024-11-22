package victor.training.clean.application.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import victor.training.clean.application.controller.dto.CustomerDto;
import victor.training.clean.domain.model.Customer;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
  @Mapping(target = "createdDate", source = "createdDate", dateFormat = "yyyy-MM-dd")
  @Mapping(target = "countryId", source = "country.id")
  // needed if Customer#getLegalEntity returns Optional<>
//   @Mapping(target = "legalEntityCode", source = "legalEntityCode", qualifiedByName = "unwrap") // OMG!!
  CustomerDto toDto(Customer customer);

  @Named("unwrap")
  default <T> T unwrap(Optional<T> optional) {// OMG
    return optional.orElse(null);
  }
}
