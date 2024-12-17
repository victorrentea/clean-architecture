package victor.training.clean.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
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

  public Customer toEntity() {
    // this method could go:
    // - stay here if simple
    // - DTO 💖 only possible if the DTO is editable.
    //     - DTO might not be changeable when it's:
    //     a) generated = you develop your API WSDL/swagger-contract-first
    //     b) packaged in a client-jar that my java clients depend on also
    // - DM "Customer" = WRONG because I couple my holy DM to ONE of the way I present this data
    // - CustomerMapper (hand written or MapStruct-generated)
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
}
