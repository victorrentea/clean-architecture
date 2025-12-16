package victor.training.clean.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Customer.Status;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
public record CustomerDto(
    Long id, // only used on GET response (assigned by backend)

    @NotNull
    @NotBlank
    @Size(min = 4)
    String name,
    @Email
    String emailAddress,
    Long countryId,

    String shippingAddressCity, // GET only (updated via dedicated endpoint)
    String shippingAddressStreet, // GET only (updated via dedicated endpoint)
    String shippingAddressZip, // GET only (updated via dedicated endpoint)

    String createdDate, // GET only (assigned by backend)

    Boolean gold, // GET & PUT
    String goldMemberRemovalReason, // GET & PUT (if gold changed true->false)
    boolean canReturnOrders, // GET only (computed by backend)

    Status status,
    String legalEntityCode,
    Boolean discountedVat // GET only (fetched by backend)
) {

  public static CustomerDto fromEntity(Customer customer) {
    return builder()
            .id(customer.getId())
            .name(customer.getName())
            .emailAddress(customer.getEmail())
            .countryId(customer.getCountry().getId())
            .status(customer.getStatus())
            .createdDate(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
            .gold(customer.isGoldMember())

            .shippingAddressStreet(customer.getShippingAddress().street())
            .shippingAddressCity(customer.getShippingAddress().city())
            .shippingAddressZip(customer.getShippingAddress().zip())

            .canReturnOrders(customer.canReturnOrders())
            .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
            .legalEntityCode(customer.getLegalEntityCode().orElse(null))
            .discountedVat(customer.isDiscountedVat())
            .build();
  }

  @AssertTrue(message = "Shipping address can either be fully present (city, street, zip) or fully absent")
  @JsonIgnore
  public boolean isShippingAddressValid() { // multi-field validation annotations
    boolean allAreSet = shippingAddressCity != null && shippingAddressStreet != null && shippingAddressZip != null;
    boolean allAreNull = shippingAddressCity == null && shippingAddressStreet == null && shippingAddressZip == null;
    return allAreSet || allAreNull;
  }
}
