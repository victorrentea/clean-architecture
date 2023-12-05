package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
// Dto used to both QUERY and COMMAND use-cases ?
public record CustomerDto(
    Long id, // GET only (server-assigned)

    @Schema(description = "this actually is the name!")
    @Size(min = 5)// SHOCK 90% of developers don't know that NULL is ok vs @Size(min=5)
    @NotNull
    String name,
    @Email
    String email,
    Long countryId,

    String shippingAddressCity, // GET only (updated via dedicated endpoint)
    String shippingAddressStreet, // GET only (updated via dedicated endpoint)
    Integer shippingAddressZipCode, // GET only (updated via dedicated endpoint)

    String createdDateStr, // GET only (server-assigned)

    Boolean gold, // GET & PUT
    String goldMemberRemovalReason, // GET & PUT(if gold changed true->false)
    int discountPercentage, // GET only (server-side computed)

    String legalEntityCode,
    Boolean discountedVat // GET only (server-side fetched)
) {

  public static CustomerDto fromEntity(Customer customer) {
    return builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .createdDateStr(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())

        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressZipCode(customer.getShippingAddress().zipCode())

        .discountPercentage(customer.getDiscountPercentage())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        .build();
  }

  public  Customer asEntity() {
    Customer customer = new Customer();
    customer.setEmail(email());
    customer.setName(name());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(countryId()));
    customer.setLegalEntityCode(legalEntityCode());
    return customer;
  }
}
