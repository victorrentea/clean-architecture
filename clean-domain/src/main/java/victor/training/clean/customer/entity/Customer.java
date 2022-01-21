package victor.training.clean.customer.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import victor.training.clean.entity.Site;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity // HOLY DOMAIN ENTITY
public class Customer {
	@Setter(AccessLevel.NONE) // KNOW this
	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 5)
	// javax.validator is the API
	// hibernate validator is the only implem known
	private String name;
	private String email;

//	@Embedded
//	private COntactDetails {name, email}
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

	private Customer() {} // just for hibernate

	public Customer(String name) {
		setName(name);

//		Validator validator = magicStaticFunction();
//		validator.validate(this);
	}

	public void setName(String name) {
		this.name = Objects.requireNonNull(name);
		if (this.name.length() < 5) throw new IllegalArgumentException();
	}

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
