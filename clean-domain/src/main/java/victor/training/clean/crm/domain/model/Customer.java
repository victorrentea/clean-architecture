package victor.training.clean.crm.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	@Size(min = 5)
	@NotNull
	private String name;

//	private CustomerAddress address; // -6 campuri = ❤️

	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
//	@JsonIgnore
	private Site site;

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (this.goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
}
