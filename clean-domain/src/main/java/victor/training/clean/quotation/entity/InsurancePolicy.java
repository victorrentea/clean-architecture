package victor.training.clean.quotation.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class InsurancePolicy {
   @Id
   private Long id;

   private String customerName; // possible inconsistencies. replication.
//   @ManyToOne
   private Long customerId;

   private BigDecimal value;
}
