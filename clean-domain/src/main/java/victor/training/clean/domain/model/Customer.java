package victor.training.clean.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	@NotNull
	private CustomerName name;

//	private String firstName, lastName;


	//	private String name;
	@NotNull
	@Email
	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
	private Site site;

  public int getDiscountPercentage() {
	  // even in non-official DDD projects <7 LOC, offcial 50 LOC, this is good to do. OOP.
      int discountPercentage = 3;
	  if (goldMember) {
          discountPercentage += 1;
      }
      return discountPercentage;
  }
}
