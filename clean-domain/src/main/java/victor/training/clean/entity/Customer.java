package victor.training.clean.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
public class Customer {
	@Setter(AccessLevel.NONE)
	@Id
	private Long id;
//	@Size(min = 5)
	@Setter(AccessLevel.NONE)
	private String name;
//	@UniqueEmail
	@Email
	private String email;

	private String createdBy;
	private LocalDate creationDate;

//	private UserAudit creation; {String, LocalDate}

	private boolean goldMember;
	@ManyToOne
	private Site site;

	private Customer() {}
	public Customer(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public boolean isGoldMember() {
		return goldMember;
	}

	public void setGoldMember(boolean goldMember) {
		this.goldMember = goldMember;
	}

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
}
