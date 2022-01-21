package victor.training.clean.quotation.entity;


import lombok.Getter;

import javax.persistence.Embeddable;

//ValueObject
@Embeddable
@Getter
public class Customer {
   private String name, email;
private Customer() {}
   public Customer(String name, String email) {
      this.name = name;
      this.email = email;
   }
}
