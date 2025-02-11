package victor.training.clean.domain.model;

//  static class AbstractAddress {
//  static class Address { // extra DRY is dangerous
//  static class PostalAddress { // extraneous word

// "VALUE OBJECT" DESIGN PATTERN
// 1) Immutable
// 2) No Identity (no PK)
// eg: Money{amount, currency}, Temperature{value, unit}, Point{x, y}

public record ShippingAddress(
    String city,
    String street,
    String zip) {
}
//public record ShippingAddress(
//    String legalEntityName,
//    String address,
//    String vatCode) {
//}