package victor.training.clean.insurance.domain.entity;

import lombok.Getter;
import lombok.Setter;
import victor.training.clean.crm.domain.entity.Customer;

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
   private Long customerId; // !! + KEEP THE FK PLEASE 🙏 DO NOT SACRIFICE CONSISTENCY until you are going to extract
   // out a module in microservice NEXT MONTH

   private BigDecimal valueInEur;

}
