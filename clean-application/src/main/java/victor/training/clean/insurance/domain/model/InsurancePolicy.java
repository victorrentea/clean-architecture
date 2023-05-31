package victor.training.clean.insurance.domain.model;

import lombok.Getter;
import lombok.Setter;
import victor.training.clean.common.country.Country;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class InsurancePolicy {
   @Id
   @GeneratedValue
   private Long id;

//   @ManyToOne
//   private Customer customer;

   private Long customerId; // BUT! preserve the FK while still in a monolithical DB

   private String customerName; // duplicate data OK for business

//   @ManyToOne
   private Long countryId;

   private BigDecimal valueInEur;

}
