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

   private String customerName; // --> Eventual Consistency

//   @ManyToOne(fetch = FetchType.LAZY)
//   private Customer customer;
   private Long customerId; // keep the FK in DB

   private BigDecimal value;
}

//interface SomeRepo {
//   @Query("SELECT new bla.bla.MyVO(c,p) FROM Customer c JOIN InsurancePolicy p ON p.customerId = c.id")
//   public void finder();
//}
//class Danger {
//   public void method() {
//      List<InsurancePolicy> policies;
//
//      for (InsurancePolicy policy : policies10000) {
//         policy.getCustomer() // N+1 queries 1000 SELECT
//      }
//   }
//}