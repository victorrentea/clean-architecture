package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

//java17 ftw
@Embeddable // vers recente de Hib stiu record
public record ShippingAddress(
    String city,
    String street,
    String zip) {
}
