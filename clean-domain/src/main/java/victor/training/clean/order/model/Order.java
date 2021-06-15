package victor.training.clean.order.model;


import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Entity
@Table(name = "ORDERS") // SQL keyword collision
public class Order {
   @Id
   @GeneratedValue
   private Long id;

   @CreatedDate
   private LocalDateTime createTime;

   private String clientId; // ID of an externally-managed person ?..

//   -- if you don't expose the ID of the inner
//   OrderLines to anyone (and you shouldn't!)
   // then you are free to model the child OrderLine entity as :
   // 1) Embeddable + @ElementCollection  in parent -- CONS: every time yo change any of the lines,
      //    ALL lines of the order are REMOVED and RE-INSERTED
   // 2) normal @Entity
   @OneToMany(cascade = CascadeType.ALL)
   @JoinColumn // otherwise generates a join table
   private List<OrderLine> orderLines = new ArrayList<>();

   protected Order() {} // for hibernate eyes only

   public Order(String clientId) {
      this.clientId = clientId;
   }


   public void add(OrderLine orderLine) {
      orderLines.add(orderLine);
   }

   public String clientId() {
      return clientId;
   }

   public List<OrderLine> orderLines() {
      return unmodifiableList(orderLines); // or Guava's ImmutableList
   }

   public Long id() {
      return id;
   }

}



