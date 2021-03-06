package victor.training.clean.jpatricks;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Supplier {
   @Id
   @GeneratedValue
   private Long id;
   private String name;

}
