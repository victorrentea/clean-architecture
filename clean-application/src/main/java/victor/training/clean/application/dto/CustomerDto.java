package victor.training.clean.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import victor.training.clean.domain.model.Customer;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
// Dto used to both QUERY and COMMAND use-cases ?
public record CustomerDto(
    Long id, // GET only (server-assigned)

    String name,
    @Email // 200 characets regex
    @NotNull
    String email,
    Long countryId,

    String shippingAddressCity, // GET only (updated via dedicated endpoint)
    String shippingAddressStreet, // GET only (updated via dedicated endpoint)
    String shippingAddressZip, // GET only (updated via dedicated endpoint)

    String createdDateStr, // GET only (server-assigned)

    Boolean gold, // GET & PUT
    String goldMemberRemovalReason, // GET & PUT(if gold changed true->false)
    int discountPercentage, // GET only (server-side computed)

    String legalEntityCode,
    Boolean discountedVat // GET only (server-side fetched)
) {
  public CustomerDto fromEntity(Customer customer) {
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
        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressZip(customer.getShippingAddress().zip())
        .discountPercentage(0) // TODO
        .build();
  }

}
