package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

// record tot mai putin @Entity ORM sau @Service&friends
// VALUE OBJECT (DESIGN PATTERN) =
// + IMMUTABLE (NO SETTERS)
// + NO ID PERSISTENT (fata de @Entity)
@Embeddable
public record ShippingAddress(
    @NotNull
    String city,
    @NotNull
    String street,
    @NotNull
    String zip
) { }
