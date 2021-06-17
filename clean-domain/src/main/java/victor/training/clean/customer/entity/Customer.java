package victor.training.clean.customer.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
public class Customer {

	enum Status {
		DRAFT, ACTIVE, DELETED
	}
	@Setter(AccessLevel.NONE)
	@Id
	private Long id;
//	@Size(min = 5)
	@Setter(AccessLevel.NONE)
	private String name;
//	@UniqueEmail
	@Email
	private String email;
	@Enumerated(EnumType.STRING)
	private Status status = Status.DRAFT;

	private String createdBy;
	private LocalDate creationDate;

//	private UserAudit creation; {String, LocalDate}


//	public Customer setStatus(Status status) {
//		this.status = status;
//		return this;
//	}
public void activate() {
	if (status != Status.DRAFT) {
		throw new IllegalArgumentException();
	}
	status = Status.ACTIVE;
}

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
