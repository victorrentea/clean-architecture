package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;
import org.springframework.transaction.annotation.Transactional;

// every class you write in Java should be record. immutable and low-shit code ratio
// unless:
// 1) an ORM @Entity or
// 2) Spring @Service&friends (Aspecting doesn't work on final classes <==records)
@Embeddable
// Value Object = small, immutable, lacking identity (no continuity of change)
// the more VO you identify the DEEPER and more SEMANTIC RICH becomes your domain model
// @Value (lombok)
public record ShippingAddress(
    String city,
    String street,
    String zip
) {
//  @Transactional
//  public void method() {}
}
