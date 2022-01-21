package victor.training.clean.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@Entity // HOLY DOMAIN ENTITY
public class Customer {
	@Setter(AccessLevel.NONE) // KNOW this
	@Id
	@GeneratedValue
	private Long id;
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
