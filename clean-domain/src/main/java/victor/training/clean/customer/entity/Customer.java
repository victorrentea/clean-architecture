package victor.training.clean.customer.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

// An anemic Domain Entity (fully opened with getters and setters, no encapsulation)
@Entity

// *** LOMBOK BEST PRACTICES ***
// @Data - avoid. Instead:
@Getter @Setter
@ToString // @Exclude the child collections fields to avoid accidental lazy loading (Hibernate)
// @NoArgsConstructor(access = AccessLevel.PRIVATE) // PRO: keep the default constructor only for the persistence (Hibernate/nosql)
// @EqualsAndHashCode - usually a bad practice on Hibernate @Entity!
public class Customer {
	@Setter(AccessLevel.NONE) // KNOW this
	@Id
	private Long id;
	@NotNull
	@Size(min = 5)
	private String name;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

	@Setter(AccessLevel.NONE)
	private Status status = Status.DRAFT;
	@Setter(AccessLevel.NONE)
	private LocalDateTime activationTime;

	public enum Status {
		DRAFT,
		ACTIVE,
		BANNED,
		DELETED
	}

	public void activate() {
		if (status == Status.BANNED) {
			throw new IllegalStateException("Cannot activate a banned customer");
		}
		status = Status.ACTIVE;
		activationTime= LocalDateTime.now();
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
