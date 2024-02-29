package victor.training.clean.domain.model;

// Value Object domain
public record ShippingAddress(
    String city,
    String street,
    String zip) {
}