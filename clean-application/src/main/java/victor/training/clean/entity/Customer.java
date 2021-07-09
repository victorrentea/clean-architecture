package victor.training.clean.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@Entity
public class Customer {
	@Setter(AccessLevel.NONE)
	@Id
	private Long id;
	@Length(min = 5)
	private String name;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

	public boolean isGoldMember() {
		return goldMember;
	}

	public void setGoldMember(boolean goldMember) {
		this.goldMember = goldMember;
	}

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (isGoldMember()) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
}