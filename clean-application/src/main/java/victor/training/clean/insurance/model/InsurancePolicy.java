package victor.training.clean.insurance.model;

import lombok.Getter;
import lombok.Setter;
import victor.training.clean.customer.model.Customer;

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
   // - object link cross-modules. will discourage the owners of Customer to change the entity, seeing how much code breaks if they do.
   // + call back to the source whenver you need the data of a customer < challenge this.
      // obvious fix : call customer module back for the data (+1 coupling, +1 indirection +1 InternalCustomerDto ....) = boilerplate.
         // do i really need that always?

   private Long customerId; // + FK in DB, but without Object links

   private String customerName;

   private BigDecimal valueInEur;

}


//@Cacheable("") for static data

class Appointment {
   //   private Clinic clinic; // 200 fields. change only at deploy time
   private Long clinicId;

   private Long invoiceCountryId;
   //   @ManyToOne
   //   private Country invoiceCountry; // naive OOP Entity modeling
}


// <curstomer:name id="${}" />
