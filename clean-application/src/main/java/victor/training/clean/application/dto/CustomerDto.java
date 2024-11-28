package victor.training.clean.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Customer.Status;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
// Dto used to both QUERY and COMMAND use-cases ?
public record CustomerDto(
    Long id, // GET only (assigned by backend)

    @NotBlank
        @Size(min =1)
    String name,
    String email,
    Long countryId,

    String shippingAddressCity, // GET only (updated via dedicated endpoint)
    String shippingAddressStreet, // GET only (updated via dedicated endpoint)
    String shippingAddressZip, // GET only (updated via dedicated endpoint)

    String createdDate, // GET only (assigned by backend)

    Boolean gold, // GET & PUT
    String goldMemberRemovalReason, // GET & PUT(if gold changed true->false)
    boolean canReturnOrders, // GET only (computed by backend)

    Status status,
    String legalEntityCode,
    Boolean discountedVat // GET only (fetched by backend)
) {

  public Customer asCustomer() {
    // only doable if your Dtos are still under your control
    Customer customer = new Customer();
    customer.setEmail(email());
    customer.setName(name());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(countryId()));
    customer.setLegalEntityCode(legalEntityCode());
    return customer;
  }

  @AssertTrue(message = "Shipping address can either be fully present (city, street, zip) or fully absent")
  @JsonIgnore
  public boolean isShippingAddressValid() { // multi-field validation with javax annotations
    return shippingAddressCity != null && shippingAddressStreet != null && shippingAddressZip != null
           || shippingAddressCity == null && shippingAddressStreet == null && shippingAddressZip == null;
    //     or write this if in the first layer of logic 💖
  }

  public static CustomerDto fromEntity(Customer customer) {
    return CustomerDto.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .createdDate(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
        .status(customer.getStatus())
        .gold(customer.isGoldMember())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode().orElse(null))
        .discountedVat(customer.isDiscountedVat())
        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressZip(customer.getShippingAddress().zip())

        //.canReturnOrders(TODO)
        .build();
  }
}
