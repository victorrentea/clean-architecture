package victor.training.clean.insurance.domain.model;

import lombok.Getter;
import lombok.Setter;
import victor.training.clean.customer.domain.model.Customer;

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
   private Long customerId;

   private String customerName; // immutable data associated with the policy life.
   // if the customer name changes, we have to issue another policy

   private BigDecimal valueInEur;

}
