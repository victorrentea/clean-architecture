package victor.training.clean.insurance.entity;

import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class InsurancePolicy { // aggregate
   @Id
   @GeneratedValue
   private Long id;

//   private Long customerId;
   // numele customerul
   @Embedded
   private Customer customer;

   private BigDecimal value;

   // 40 campuri
}
