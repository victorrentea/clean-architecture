package victor.training.clean.domain.insurance.entity;

import lombok.Getter;
import lombok.Setter;
import victor.training.clean.domain.customer.entity.Customer;

import javax.persistence.*;
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

   // option 1 (the purest) : keep just the ID - don't duplicate data
   private Long customerId; // keep the FK

   // option 2 (keep a copy of WHAT I NEED) -> eventual consistency: how is that data updated? should that data be updated ?
//   private String customerName;
//   @Embedded
//   private InsuredCustomer customer; // {name, gender, dob}

   private BigDecimal valueInEur;
}
// Option 3
//@Entity
//class InsuredCustomer {
//   @Id
//   @GeneratedValue
//   private Long id;
//   private Long customerId;
//
//   @ManyToOne
//   private InsurancePolicy insurancePolicy;
//
//}
