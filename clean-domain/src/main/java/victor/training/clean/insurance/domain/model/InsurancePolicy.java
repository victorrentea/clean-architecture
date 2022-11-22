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

   // Option1:
   private Long customerId; // But, please 🙏 keep the FK !!!
   // (don't sacrifice consistency in a Modulity) - that's what keeps today many teams from
   // breaking out microservices;

   private BigDecimal valueInEur;

}
