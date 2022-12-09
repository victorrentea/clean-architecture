package victor.training.clean.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	@Size(min = 3)@NotNull
	private String name;
	@Email
	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
	private Site site;

//	protected Customer() {}
	public int getDiscountPercentage() {
		// < 10 lines of logic, 50 if DDD is the mouth of Management.
		// ofc no repo/api calls inside => only loigic related to the sate of THIS entity.
	  int discountPercentage = 3;
		if (this.goldMember	) {
		discountPercentage += 1;
	  }
	  return discountPercentage;
	}
	//	List<Childretn> // lazy load

//	@JsonIgnore
//	public Site getSite() {
//		return site;
//	}
}
