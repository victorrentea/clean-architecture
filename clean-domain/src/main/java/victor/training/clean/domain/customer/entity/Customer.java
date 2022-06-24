package victor.training.clean.domain.customer.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

// An anemic Domain Entity (fullly opened with getters and setters, no encapsulation)
@Entity

// *** LOMBOK BEST PRACTICES ***
// @Data - avoid. Instead:
@Getter
@ToString // @Exclude the child collections fields to avoid accidental lazy loading (Hibernate)
// @NoArgsConstructor(access = AccessLevel.PRIVATE) // PRO: keep the default constructor only for the persistence (Hibernate/nosql)
// @EqualsAndHashCode - usually a bad practice on Hibernate @Entity!
public class Customer {
	// KNOW this
	@Id
	@GeneratedValue
	private Long id;
	@Size(min = 5) // automatically checked at persist/merge or auto-flush byu hibernate
	private String name;
	@Email
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	@Valid
	private Site site;
	@Version
	private Long version;

	protected Customer() {} // just for hibernate
	public Customer(String name) {
		setName(name); // the best

//		Validator validator = hocusPocus();
// 1: get a bean from spring via a static method.
// 2: [evil] make @Autowired work in @ENtity ?!!!!!!!! @Configurable (DO NOT USE IT!!)
//		Set<ConstraintViolation<Customer>> violations
//				= validator.validate(this);
//		if (!violations.isEmpty()) {
//			throw new IllegalArgumentException(violations.toString());
//		}

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

	public Customer setName(String name) {
		if (name.length() < 5) {
			throw new IllegalArgumentException("Name too short");
		}
		this.name = name;
		return this;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public Customer setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public Customer setSite(Site site) {
		this.site = site;
		return this;
	}

	public Customer setVersion(Long version) {
		this.version = version;
		return this;
	}
}
