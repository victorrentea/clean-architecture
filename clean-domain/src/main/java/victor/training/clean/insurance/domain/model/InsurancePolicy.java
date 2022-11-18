package victor.training.clean.insurance.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class InsurancePolicy {
   @Id
   @GeneratedValue
   private Long id;

//   @ManyToOne
//   private Customer customer; // NO: intermodule

   private Long customerId; // option1) keep the ID from the other module

   // the customerName cannot change on a policy
   private String customerName; // option2) snapshot on this module data from another module

   private BigDecimal valueInEur;

}
