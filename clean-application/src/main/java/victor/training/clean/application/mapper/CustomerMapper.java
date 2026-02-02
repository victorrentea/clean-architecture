package victor.training.clean.application.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class CustomerMapper {

  private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
    return zip;
  }

  @Named("optionalToNull")
  public <T> T optionalToNull(Optional<T> optional) {// OMG
    return optional.orElse(null);
  }
}
