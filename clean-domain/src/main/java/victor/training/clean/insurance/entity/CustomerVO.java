package victor.training.clean.insurance.entity;


import javax.persistence.Embeddable;

@Embeddable
public class CustomerVO { // replicate data
   private long id;
   private String name;

   private CustomerVO() {} // just for hibernate

   public CustomerVO(long id, String name) {
      this.id = id;
      this.name = name;
   }

   public long getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}
