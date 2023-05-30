package victor.training.clean.insurance.domain.model;

import lombok.Getter;
import lombok.Setter;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.insurance.domain.repo.Country;

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

   @ManyToOne
   private Customer customer;

//   private Long customerId; // BUT! preserve the FK while still in a monolithical DB

   @ManyToOne
   private Country country;

   private BigDecimal valueInEur;

}
