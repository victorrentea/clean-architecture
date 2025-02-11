package victor.training.clean.domain.model;

//  static class AbstractAddress {
//  static class Address { // extra DRY is dangerous
//  static class PostalAddress { // extraneous word

// "VALUE OBJECT" DESIGN PATTERN
// 1) Immutable
// 2) No Identity (no PK)

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