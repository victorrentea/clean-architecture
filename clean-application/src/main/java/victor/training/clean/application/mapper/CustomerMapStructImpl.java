package victor.training.clean.application.mapper;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javax.annotation.processing.Generated;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-02T23:49:04+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Amazon.com Inc.)"
)
@Component
public class CustomerMapStructImpl {

  private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @org.mapstruct.Mapping(target = "createdDate", source = "createdDate", dateFormat = "yyyy-MM-dd")
  public CustomerDto toDto(Customer customer) {
    if (customer == null) {
      return null;
    }

    CustomerDto.CustomerDtoBuilder customerDto = CustomerDto.builder();

      if (customer.getCreatedDate() != null) {
      customerDto.createdDate(dateTimeFormatter_yyyy_MM_dd_0159776256.format(customer.getCreatedDate()));
    }
    customerDto.countryId(customerCountryId(customer));
    customerDto.legalEntityCode(unwrapOpt(customer.getLegalEntityCode()));
    customerDto.id(customer.getId());
    customerDto.name(customer.getName());
    customerDto.email(customer.getEmail());
    customerDto.goldMemberRemovalReason(customer.getGoldMemberRemovalReason());
    customerDto.status(customer.getStatus());
    customerDto.discountedVat(customer.isDiscountedVat());

    return customerDto.build();
  }

  private Long customerCountryId(Customer customer) {
    if (customer == null) {
      return null;
    }
    Country country = customer.getCountry();
    if (country == null) {
      return null;
    }
    long id = country.getId();
    return id;
  }

  @Named("unwrapOpt")
  public <T> T unwrapOpt(Optional<T> optional) {// OMG
    return optional.orElse(null);
  }
}
