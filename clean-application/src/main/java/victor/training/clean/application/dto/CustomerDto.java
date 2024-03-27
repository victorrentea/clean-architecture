package victor.training.clean.application.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import victor.training.clean.domain.model.Customer;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
// Dto used to both QUERY and COMMAND use-cases ?
public record CustomerDto(
    Long id, // GET only (server-assigned)

    @Size(max=50)
    String name,
    String emailAddress,
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
        .emailAddress(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .createdDateStr(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        .shippingAddressStreet(customer.getShippingAddressStreet())
        .shippingAddressCity(customer.getShippingAddressCity())
        .shippingAddressZip(customer.getShippingAddressZip())
        .discountPercentage(0) // TODO
        .build();
  }
}
