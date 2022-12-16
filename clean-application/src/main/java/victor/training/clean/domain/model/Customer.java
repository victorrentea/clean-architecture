package victor.training.clean.domain.model;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
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
