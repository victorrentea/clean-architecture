package victor.training.clean.entity;

import lombok.Data;

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
   private Customer customer;

   private BigDecimal value;
}
