package victor.training.clean.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Customer.Status;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Schema(example = """
    {
        "id": 123,
        "name": "John Doe",
        "email": "john@example.com",
        "countryId": 1,
        "shippingAddressCity": "New York",
        "shippingAddressStreet": "123 Main St",
        "shippingAddressZip": "10001",
        "createdDate": "2025-08-14T12:00:00Z",
        "gold": true,
        "goldMemberRemovalReason": null,
        "canReturnOrders": true,
        "status": "ACTIVE",
        "legalEntityCode": "ABC123",
        "discountedVat": false
      }
    """)
public record CustomerDto(
    Long id, // only used on GET response (assigned by backend)

    @NotNull
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

  @JsonIgnore
  @AssertTrue(message = "Shipping address can either be fully present (city, street, zip) or fully absent")
  public boolean isShippingAddressValid() { // multi-field validation annotations
    boolean allAreSet = shippingAddressCity != null && shippingAddressStreet != null && shippingAddressZip != null;
    boolean allAreNull = shippingAddressCity == null && shippingAddressStreet == null && shippingAddressZip == null;
    return allAreSet || allAreNull;
  }
}
