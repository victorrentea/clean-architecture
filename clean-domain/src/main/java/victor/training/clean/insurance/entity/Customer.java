package victor.training.clean.insurance.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Customer {
   private Long id;
   private String name;

   public Customer(Long id, String name) {
      this.id = id;
      this.name = name;
   }

   public Long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }


   public String toString() {
      return "Customer(id=" + this.getId() + ", name=" + this.getName() + ")";
   }
}
