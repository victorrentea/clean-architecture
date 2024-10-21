package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
// Value Object (design pattern) - from DDD
// 1. immutable (readonly fields)
// 2. no PK/persistent Identity (vs Entity), no continuity of change
// [3.] always valid (self-guarding constraints)
public record ShippingAddress(String city, String street, String zip) {
//  public ShippingAddress {
//    if (city == null) {
//      throw new IllegalArgumentException("City must not be null");
//    }
//  }
}
