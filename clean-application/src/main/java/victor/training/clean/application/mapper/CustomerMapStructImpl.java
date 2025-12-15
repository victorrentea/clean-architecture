package victor.training.clean.application.mapper;

import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;

import org.springframework.stereotype.Component;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2025-12-15T13:41:01+0200",
        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CustomerMapStructImpl implements CustomerMapStruct {

  private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  public CustomerDto toDto(Customer customer) {
    if (customer == null) {
      return null;
    }

    CustomerDto.CustomerDtoBuilder customerDto = CustomerDto.builder();

    if (customer.getCreatedDate() != null) {
      customerDto.createdDate(dateTimeFormatter_yyyy_MM_dd_0159776256.format(customer.getCreatedDate()));
    }
    customerDto.countryId(customerCountryId(customer));
    customerDto.legalEntityCode(optionalToNull(customer.getLegalEntityCode()));
    customerDto.shippingAddressCity(customerShippingAddressCity(customer));
    customerDto.shippingAddressStreet(customerShippingAddressStreet(customer));
    customerDto.shippingAddressZip(customerShippingAddressZip(customer));
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

  private String customerShippingAddressCity(Customer customer) {
    if (customer == null) {
      return null;
    }
    Customer.ShippingAddress shippingAddress = customer.getShippingAddress();
    if (shippingAddress == null) {
      return null;
    }
    String city = shippingAddress.city();
    if (city == null) {
      return null;
    }
    return city;
  }

  private String customerShippingAddressStreet(Customer customer) {
    if (customer == null) {
      return null;
    }
    Customer.ShippingAddress shippingAddress = customer.getShippingAddress();
    if (shippingAddress == null) {
      return null;
    }
    String street = shippingAddress.street();
    if (street == null) {
      return null;
    }
    return street;
  }

  private String customerShippingAddressZip(Customer customer) {
    if (customer == null) {
      return null;
    }
    Customer.ShippingAddress shippingAddress = customer.getShippingAddress();
    if (shippingAddress == null) {
      return null;
    }
    String zip = shippingAddress.zip();
    if (zip == null) {
      return null;
    }
    return zip;
  }
}
