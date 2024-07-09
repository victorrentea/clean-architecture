package victor.training.clean.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
// Dto used to both QUERY and COMMAND use-cases ?
public record CustomerDto(
    Long id, // Q

    @Size(min = 5, max = 100, message = "Name must be between 3 and 100 characters")
    String name, // QC
    @Email
    String email, // QC
    Long countryId, // QC

    String shippingAddressCity,
    String shippingAddressStreet,
    @Schema(description = "ZIP code of the shipping address", example = "123456")
    String shippingAddressZip,

    String createdDateStr, // Q

    Boolean gold, // Q
    String goldMemberRemovalReason, // Q
    boolean canReturnOrders,

    String legalEntityCode,
    Boolean discountedVat // GET only (fetched by backend)
) {

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
        .createdDateStr(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())

        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressZip(customer.getShippingAddress().zip())

        //.canReturnOrders(TODO)
        .build();
  }

  public Customer toEntity() {
    Customer customer = new Customer();
    customer.setEmail(email());
    customer.setName(name());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(countryId()));
    customer.setLegalEntityCode(legalEntityCode());
    return customer;
  }
}
