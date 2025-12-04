package victor.training.clean.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Customer.Status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
public record CustomerDto( // ~ External Event
    Long id, // only used on GET response (assigned by backend)

    @NotNull
                           // (1) do payload validation ASAP⭐️ with @ in Dto
                           //    Unfortunately you will repeat these in Domain Model= accepted DRY violation
                           // (2) do business rules validation (eg hitting the DB) later in the service layer
    @NotBlank
    @Size(min = 4)
    String name,
    @Email
    String email,
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

  public static CustomerDto fromEntity(Customer customer, boolean canReturnOrders) {
    return builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .status(customer.getStatus())
        .createdDate(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())

        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressZip(customer.getShippingAddress().zip())

        .canReturnOrders(canReturnOrders)
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode().orElse(null))
        .discountedVat(customer.isDiscountedVat())
        .build();
  }

  @AssertTrue(message = "Shipping address can either be fully present (city, street, zip) or fully absent")
  @JsonIgnore
  public boolean isShippingAddressValid() { // multi-field validation annotations
//    if (primucamp==true) { aia tre fie true }
    boolean allAreSet = shippingAddressCity != null && shippingAddressStreet != null && shippingAddressZip != null;
    boolean allAreNull = shippingAddressCity == null && shippingAddressStreet == null && shippingAddressZip == null;
    return allAreSet || allAreNull;
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
