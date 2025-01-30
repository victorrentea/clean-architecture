package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

//1) why immutable
//2) how about code consistency!
// DESIGN PATTERN: VALUE OBJECT
// 1) Immutable
// 2) Equals and hashCode based on all fields
// 3) No identity (PK) ⭐️
@Embeddable // record or @lombok.Value
public record ShippingAddress(String city, String street, String zip) {}
