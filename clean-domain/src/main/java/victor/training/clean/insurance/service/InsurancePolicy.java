package victor.training.clean.insurance.service;

import lombok.Data;

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

   private Long customerId;

   private BigDecimal value;

   // 40 campuri
}
