package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// record TOT din java 17 mai putin @Entity si @Service&co
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip
) {}
