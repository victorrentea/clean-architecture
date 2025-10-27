//package victor.training.clean.application.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//import victor.training.clean.application.dto.CustomerDto;
//import victor.training.clean.domain.model.Customer;
//
//import java.util.Optional;
//
//@Mapper(componentModel = "spring")
//public interface CustomerMapStruct {
//  @Mapping(target = "createdDate", source = "createdDate", dateFormat = "yyyy-MM-dd")
//  @Mapping(target = "countryId", source = "country.id")
//  @Mapping(target = "legalEntityCode", source = "legalEntityCode", qualifiedByName = "optionalToNull")
//  @Mapping(target = "shippingAddressCity", source = "shippingAddressCity")
//  CustomerDto toDto(Customer customer);
//
//  @Named("optionalToNull")
//  default <T> T optionalToNull(Optional<T> optional) {// OMG
//    return optional.orElse(null);
//  }
//}
