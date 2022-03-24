package victor.training.clean.insurance.entity;

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

   private Long customerId;

   private String customerName; // eventual consistnecy > what if it changes on the master source ?!
//   @ManyToOne
//   private Customer customer; // this dependency crosses the boundary

   private BigDecimal valueInEur;


}
