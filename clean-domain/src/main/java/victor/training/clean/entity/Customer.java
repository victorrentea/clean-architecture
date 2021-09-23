package victor.training.clean.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

//unchangeable
class ADto {
	private BDto b;
	String name;
	private String name1;
	private String name2;
}
class BDto {
	String name;
}

// Option1: The Wrapper
class A {
	private B b;
	private ADto dto;
	public String getName() {
		return dto.name;
	}
	boolean canBeRejected() {
		return dto.name.startsWith("SMTH");
	}

	public ADto getDto() {
		return dto;
	}
}
class B {
	private BDto dto;
}

// Option2:The Clone
class AEntity {
	private BEntity b;
	private String name;
	private String name1;
	private String name2;
	public String getName() {
		return name;
	}
	boolean canBeRejected() {
		return name.startsWith("SMTH");
	}
}
class BEntity {
	private String name;
}

@Data
@Entity
//1) mapping.xml  - hibernate BAD
//2) create separate CustomerEntity outside of domain
public class Customer {


	enum State {
		DRAFT, VALID, DELETED;
	}
	@Setter(AccessLevel.NONE)
	@Id
	private Long id;
	private String name;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	private State state;

	private LocalDateTime validationTime;
	public void validate(LocalDateTime now) {
		if (state == State.DRAFT) {
			throw new IllegalArgumentException("");
		}
		state = State.VALID;
		validationTime = now;
	}

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
	@ManyToOne
	private Site site;

	public boolean isGoldMember() {
		return goldMember;
	}

	public void setGoldMember(boolean goldMember) {
		this.goldMember = goldMember;
	}


}

