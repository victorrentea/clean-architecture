package victor.training.clean.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.application.dto.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapStruct {
    @Mapping(target = "creationDateStr", source = "creationDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "siteId", source = "site.id")
//    @Mapping(target = "shipmentAddressCity", source = "shipmentAddress.city")
    CustomerDto toDto(Customer customer);

//    default String codTata() {cod}
}
