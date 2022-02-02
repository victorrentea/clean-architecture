package victor.training.clean.insurance.entity;

import lombok.Data;
import victor.training.clean.customer.entity.Customer;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Data
public class InsurancePolicy {
   @Id
   private Long id;

   @ManyToOne
   private Customer customer2;

//   private Long customerId;

   @Embedded
   private CustomerVO customer;

   private BigDecimal value;
}
