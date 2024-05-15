package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// record tot mai putin @Entity ORM sau @Service&friends
// VALUE OBJECT (DESIGN PATTERN) =
// + IMMUTABLE (NO SETTERS)
// + NO ID PERSISTENT (fata de @Entity)
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip
) { }
