package victor.training.clean.quotation.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class InsurancePolicy {
   @Id
   @GeneratedValue
   private Long id;

   private Long customerId;// DDD friendly
//   @Embedded
//   private Customer customer;

//   @ManyToOne
//   private Customer customer;

   private BigDecimal value;
}
